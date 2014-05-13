package com.hyrt.cnp.dynamic.fragment;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyrt.cnp.base.account.CNPClient;
import com.hyrt.cnp.base.account.model.BabyInfo;
import com.hyrt.cnp.base.account.model.Dynamic;
import com.hyrt.cnp.base.account.utils.FaceUtils;
import com.hyrt.cnp.base.view.XListView;
import com.hyrt.cnp.dynamic.R;
import com.hyrt.cnp.dynamic.adapter.DynamicAdapter;
import com.hyrt.cnp.dynamic.request.BabayDynamicRequest;
import com.hyrt.cnp.dynamic.requestListener.MyIndexRequestListener;
import com.hyrt.cnp.dynamic.ui.BabayIndexActivity;
import com.hyrt.cnp.dynamic.ui.HomeInteractiveActivity;
import com.jingdong.common.frame.BaseActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.octo.android.robospice.persistence.DurationInMillis;

import net.oschina.app.AppContext;

import java.util.ArrayList;

/**
 * Created by GYH on 14-3-13.
 */
public class MyIndexFragment extends Fragment{

    private View rootView;
    private ImageView faceview;
    private ImageView imageviewback;
    private TextView nameview;
    private TextView introview;
    private XListView listView;
    private ImageView faceviewbg;

    private String STATE;
    final private String REFRESH="refresh";
    final private String ONLOADMORE="onLoadMore";
    final private String HASDATA="hasdata";

    private String more="1";

    private ArrayList<Dynamic> dynamics=new ArrayList<Dynamic>();
    private DynamicAdapter dynamicAdapter;
    private HomeInteractiveActivity activity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        activity = (HomeInteractiveActivity) getActivity();
        STATE = HASDATA;
        rootView=inflater.inflate(R.layout.fragment_myindex,container,false);
        dynamicAdapter = null;
        dynamics.clear();
        initView();
        loadData();
        if(dynamics.size()==0){
            loadData();
        }else{
            allDataUi();
        }
        return rootView;
    }

    /**
     * 初始化布局
     * */
    private void initView(){
        imageviewback=(ImageView)rootView.findViewById(R.id.imageviewback);
        faceview=(ImageView)rootView.findViewById(R.id.face_iv);
        nameview =(TextView)rootView.findViewById(R.id.name_tv);
        introview=(TextView)rootView.findViewById(R.id.intro);
        listView = (XListView)rootView.findViewById(R.id.dynamic_listview);
        faceviewbg=(ImageView)rootView.findViewById(R.id.imageView);
        imageviewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        android.util.Log.i("tag", "activity:"+activity);
        android.util.Log.i("tag", "activity.userDetail:"+activity.userDetail);
        if(activity.userDetail != null){
            nameview.setText(activity.userDetail.getData().getRenname());
            introview.setText(activity.userDetail.getData().getTimeText());

            String facePath = FaceUtils.getAvatar(
                    activity.userDetail.getData().getUser_id(),
                    FaceUtils.FACE_BIG);
            ImageLoader.getInstance().displayImage(
                    facePath + "?time=" + activity.userDetail.getData().getLogo(),
                    faceview, AppContext.getInstance().mNoCacheOnDiscImageloadoptions);
//            activity.showDetailImage(facePath + "?time=" +activity.userDetail.getData().getLogo(), faceview, false);

            //加载头像地址
            String faceBgPath = FaceUtils.getAvatar(activity.userDetail.getData().getUser_id(), FaceUtils.FACE_BG);
//            ImageLoader.getInstance().displayImage(
//                    faceBgPath,
//                    faceviewbg, AppContext.getInstance().mImageloaderoptions);
            activity.showDetailImage(faceBgPath, faceviewbg, true);
        }



        listView.setPullLoadEnable(true);
        listView.setPullRefreshEnable(true);
        listView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                if(STATE.equals(HASDATA)||STATE.equals(ONLOADMORE)){
                    Toast.makeText(getActivity(),"正在加载,请稍后!",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getActivity(),"正在加载,请稍后!",Toast.LENGTH_SHORT).show();
                }else {
                    loadData();
//                    Toast.makeText(BabayIndexActivity.this,"onLoadMore",Toast.LENGTH_SHORT).show();
                }
                listView.stopLoadMore();
            }
        });
    }

    /**
     * 加载数据
     * */
    private void loadData(){

        MyIndexRequestListener sendwordRequestListener = new MyIndexRequestListener(getActivity());
        BabayDynamicRequest schoolRecipeRequest=new BabayDynamicRequest(
                Dynamic.Model.class,getActivity(),"",more);
        activity.spiceManager.execute(schoolRecipeRequest, schoolRecipeRequest.getcachekey(), DurationInMillis.ONE_SECOND * 10,
                sendwordRequestListener.start());
    }

    /**
     * 将已有的数据放到listview中
     * */
    private void allDataUi(){
        String[] resKeys = new String[]{"getUserphoto", "getUserName",
                "getPosttime3", "getContent2",
                "getsPicAry0", "getsPicAry1",
                "getsPicAry2", "getPosttime2", "getTransmit2", "getReview2", "gettContent"};
        int[] reses = new int[]{R.id.dynamic_Avatar, R.id.dynamic_name,
                R.id.dynamic_time, R.id.dynamic_context,
                R.id.dynamic_image1, R.id.dynamic_image2,
                R.id.dynamic_image3, R.id.dynamic_time2, R.id.dynamic_zf_num, R.id.dynamic_pl_num, R.id.dynamic_dcontext};
        dynamicAdapter = new DynamicAdapter(activity, dynamics, R.layout.layout_item_dynamic, resKeys, reses);
        dynamicAdapter.setCallback(mDynamicAdapterCallback);
        listView.setAdapter(dynamicAdapter);
    }

    private DynamicAdapter.DynamicAdapterCallback mDynamicAdapterCallback = new DynamicAdapter.DynamicAdapterCallback() {
        @Override
        public void onFaceClick(int position) {
        }

        @Override
        public void onPhotoClick(int position, int PhotoPosition) {
            ((BaseActivity)getActivity()).showPop2(rootView, dynamics.get(position).getbPicAry2(), PhotoPosition, getActivity());
        }
    };

    /**
     *
     * 更新ui界面
     * */

    public void updateUI(Dynamic.Model model){

        if(model==null&&dynamics.size()==0){
            LinearLayout linearLayout =(LinearLayout)rootView.findViewById(R.id.layout_bottom);
            linearLayout.setVisibility(View.VISIBLE);
            TextView bottom_num = (TextView)rootView.findViewById(R.id.bottom_num);
            bottom_num.setText("暂无信息");
        }else if(model==null){
            Toast.makeText(getActivity(), "已经全部加载", Toast.LENGTH_SHORT).show();
        }else{
            more=model.getMore();
            if(STATE != null && STATE.equals(REFRESH)){//如果正在刷新就清空
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
                dynamicAdapter = new DynamicAdapter(activity,dynamics,R.layout.layout_item_dynamic,resKeys,reses);
                dynamicAdapter.setCallback(mDynamicAdapterCallback);
                if(listView != null){
                    listView.setAdapter(dynamicAdapter);
                }
            }else{
                dynamicAdapter.notifyDataSetChanged();
            }
        }
        STATE="";//清空状态
    }
}
