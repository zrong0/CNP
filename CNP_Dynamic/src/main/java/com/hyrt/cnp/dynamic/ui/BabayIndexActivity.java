package com.hyrt.cnp.dynamic.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.hyrt.cnp.base.account.model.BabyInfo;
import com.hyrt.cnp.base.account.model.Dynamic;
import com.hyrt.cnp.base.account.model.UserDetail;
import com.hyrt.cnp.base.account.utils.FaceUtils;
import com.hyrt.cnp.dynamic.R;
import com.hyrt.cnp.dynamic.adapter.DynamicAdapter;
import com.hyrt.cnp.dynamic.request.BabayDynamicRequest;
import com.hyrt.cnp.dynamic.request.BabayInfoRequest;
import com.hyrt.cnp.dynamic.requestListener.BabayDynamicRequestListener;
import com.hyrt.cnp.dynamic.requestListener.BabayInfoRequestListener;
import com.hyrt.cnp.base.view.XListView;
import com.jingdong.common.frame.BaseActivity;
import com.octo.android.robospice.persistence.DurationInMillis;

import java.util.ArrayList;

/**
 * Created by GYH on 14-1-20.
 */
public class BabayIndexActivity extends BaseActivity{

    @Inject
    @Named("classroomAlbumActivity")
    private Class schoolPhotoActivity;
    @Inject
    @Named("userInfoActivity")
    private Class userInfoActivity;
    private String STATE;
    final private String REFRESH="refresh";
    final private String ONLOADMORE="onLoadMore";
    final private String HASDATA="hasdata";
    private String more="1";
    private String moreType = "";
    private ImageView faceview;
    private ImageView imageviewback;
    private TextView nameview;
    private TextView introview;
//    private BabyInfo classRoomBabay;
    private XListView listView;

    private BabyInfo babyInfo;
    private boolean needLoad = false;

    private DynamicAdapter dynamicAdapter;

    private ArrayList<Dynamic> dynamics=new ArrayList<Dynamic>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_babayindex);
        actionBar.hide();
        initView();
        Intent intent=getIntent();
        needLoad = intent.getBooleanExtra("needLoad", false);
        babyInfo = (BabyInfo)intent.getSerializableExtra("vo");
        if(needLoad){
            loadBabayinfoData();
        }else{
            UpdataBabayinfo(babyInfo);
        }
    }

    private void initView(){
        imageviewback=(ImageView)findViewById(R.id.imageviewback);
        faceview=(ImageView)findViewById(R.id.face_iv);
        nameview =(TextView)findViewById(R.id.name_tv);
        introview=(TextView)findViewById(R.id.intro);
        listView = (XListView)findViewById(R.id.dynamic_listview);
        TextView all_daynamic=(TextView)findViewById(R.id.all_daynamic);
        TextView child_word=(TextView)findViewById(R.id.child_word);
        TextView daynamic_photos=(TextView)findViewById(R.id.daynamic_photos);
        TextView babay_information=(TextView)findViewById(R.id.babay_information);
        imageviewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        child_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("vo",babyInfo);
                intent.setClass(BabayIndexActivity.this,BabayWordActivity.class);
                startActivity(intent);
            }
        });

        daynamic_photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(BabayIndexActivity.this,schoolPhotoActivity);
                intent.putExtra("Category","BabayIndexActivity");
                intent.putExtra("vo",babyInfo);
                startActivity(intent);
            }
        });

        babay_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(babyInfo!=null){
//                    showBabayInfo();
//                }else{
//                    loadBabayinfoData();
//                }
                showBabayInfo();

            }
        });

        listView.setPullLoadEnable(true);
        listView.setPullRefreshEnable(true);
        listView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                if(STATE.equals(HASDATA)||STATE.equals(ONLOADMORE)){
                    Toast.makeText(BabayIndexActivity.this,"正在加载,请稍后!",Toast.LENGTH_SHORT).show();
                }else {
                    STATE=REFRESH;
                    more="1";
//                    Toast.makeText(BabayIndexActivity.this,"正在刷新,请稍后!",Toast.LENGTH_SHORT).show();
                    loadData();
                }
                listView.stopRefresh();
            }

            @Override
            public void onLoadMore() {
                if(STATE.equals(HASDATA)||STATE.equals(REFRESH)){
                    Toast.makeText(BabayIndexActivity.this,"正在加载,请稍后!",Toast.LENGTH_SHORT).show();
                }else {
                    loadMoreData();
//                    Toast.makeText(BabayIndexActivity.this,"onLoadMore",Toast.LENGTH_SHORT).show();
                }
                listView.stopLoadMore();
            }
        });
    }

    /**
     *
     * 更新ui界面
     * */

    public void updateUI(Dynamic.Model model){

        if(model==null&&dynamics.size()==0){
            LinearLayout linearLayout =(LinearLayout)findViewById(R.id.layout_bottom);
            linearLayout.setVisibility(View.VISIBLE);
            TextView bottom_num = (TextView)findViewById(R.id.bottom_num);
            bottom_num.setText("暂无信息");
        }else if(model==null){
            Toast.makeText(BabayIndexActivity.this,"已经全部加载",Toast.LENGTH_SHORT).show();
        }else{
            moreType = model.getMore();
            if(STATE.equals(REFRESH)){//如果正在刷新就清空
                dynamics.clear();
            }
            dynamics.addAll(model.getData());
            if(dynamicAdapter==null){
                String[] resKeys=new String[]{"getUserphoto","getUserName",
                        "getPosttime3","getContent2",
                        "getsPicAry0","getsPicAry1",
                        "getsPicAry2","getPosttime2","getTransmit2","getReview2","gettContent"};
                int[] reses=new int[]{R.id.dynamic_Avatar,R.id.dynamic_name,
                        R.id.dynamic_time,R.id.dynamic_context,
                        R.id.dynamic_image1,R.id.dynamic_image2,
                        R.id.dynamic_image3,R.id.dynamic_time2,R.id.dynamic_zf_num,R.id.dynamic_pl_num,R.id.dynamic_dcontext};
                dynamicAdapter = new DynamicAdapter(this,dynamics,R.layout.layout_item_dynamic,resKeys,reses);
                listView.setAdapter(dynamicAdapter);
            }else{
                dynamicAdapter.notifyDataSetChanged();
            }
        }
        STATE="";//清空状态
    }


    private void loadBabayinfoData(){
        BabayInfoRequestListener sendwordRequestListener = new BabayInfoRequestListener(this);
        BabayInfoRequest schoolRecipeRequest=new BabayInfoRequest(BabyInfo.Model.class,this,babyInfo.getUser_id()+"");
        spiceManager.execute(schoolRecipeRequest, schoolRecipeRequest.getcachekey(), DurationInMillis.ONE_SECOND * 10,
                sendwordRequestListener.start());
    }

    public void UpdataBabayinfo(BabyInfo babyInfo){
        showDetailImage(FaceUtils.getAvatar(babyInfo.getUser_id(),FaceUtils.FACE_BIG),faceview,false);
        nameview.setText(babyInfo.getRenname().toString());
        if(babyInfo.getIntro().equals("")){
            introview.setText("暂无签名");
        }else{
            introview.setText(babyInfo.getIntro());
        }
        //showBabayInfo();

        STATE=HASDATA;
        loadData();
    }

    public void loadData(){
        BabayDynamicRequestListener sendwordRequestListener = new BabayDynamicRequestListener(this);
        BabayDynamicRequest schoolRecipeRequest=new BabayDynamicRequest(
                Dynamic.Model.class,this,babyInfo.getUser_id()+"","1");
        spiceManager.execute(schoolRecipeRequest, schoolRecipeRequest.getcachekey(), DurationInMillis.ONE_SECOND * 10,
                sendwordRequestListener.start());
    }
    public void loadMoreData(){
        BabayDynamicRequestListener sendwordRequestListener = new BabayDynamicRequestListener(this);
        if(dynamics.size() > 0){
            more = dynamics.get(dynamics.size()-1).getPosttime();
            BabayDynamicRequest schoolRecipeRequest=new BabayDynamicRequest(
                    Dynamic.Model.class,this,babyInfo.getUser_id()+"",more);
            spiceManager.execute(schoolRecipeRequest, schoolRecipeRequest.getcachekey(), DurationInMillis.ONE_SECOND * 10,
                    sendwordRequestListener.start());
        }

    }

    public void showBabayInfo(){
        UserDetail.UserDetailModel userDetailModel=new UserDetail.UserDetailModel();
        UserDetail userDetail =new UserDetail();
        userDetail.setRenname(babyInfo.getRenname());
        userDetail.setBirthday(babyInfo.getBirthday());
        userDetail.setNurseryName(babyInfo.getNurseryName());
        userDetail.setSex(babyInfo.getSex());
        userDetail.setNationality(babyInfo.getNationality());
        userDetail.setBloodType(babyInfo.getBloodType());
        userDetail.setEthnic(babyInfo.getEthnic());
        userDetailModel.setData(userDetail);
        Intent intent = new Intent();
        intent.setClass(BabayIndexActivity.this,userInfoActivity);
        intent.putExtra("vo", userDetailModel);
        intent.putExtra("mybabayinfo",false);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0&&resultCode==1){
            STATE=REFRESH;
            more="1";
            loadData();
        }
    }
}
