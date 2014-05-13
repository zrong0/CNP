package com.hyrt.cnp.dynamic.ui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyrt.cnp.base.account.model.Album;
import com.hyrt.cnp.base.account.model.BaseTest;
import com.hyrt.cnp.base.account.model.DynamicPhoto;
import com.hyrt.cnp.dynamic.R;
import com.hyrt.cnp.dynamic.fragment.MyAblumsFragment;
import com.hyrt.cnp.dynamic.request.DynamicPhotoListRequest;
import com.hyrt.cnp.dynamic.request.MyAlbumRequest;
import com.hyrt.cnp.dynamic.requestListener.DynamicPhotoListRequestListener;
import com.hyrt.cnp.dynamic.requestListener.MyAlbumRequestListener;
import com.jingdong.app.pad.adapter.MySimpleAdapter;
import com.jingdong.common.frame.BaseActivity;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by Zoe on 14-4-12.
 */
public class DynamicPhotoListActivity extends BaseActivity{

    private GridView gridView;
    private MySimpleAdapter classRoomAdapter;
    private DynamicPhoto.Model model;
    private String  Category;
    private TextView bottom_num;


    private DynamicPhoto selectPhoto;
    private Dialog mPhotoSelctDialog;

    private ArrayList<String> imageUrls = new ArrayList<String>();
    private boolean midified = false;

    private static final int RESULT_FOR_ADD_ALBUM = 103;

    private Album mAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroomphotolist);
        Intent intent = getIntent();
        mAlbum = (Album)intent.getSerializableExtra("album");
        if(mAlbum != null){
            titletext.setText(mAlbum.getAlbumName());
        }
        initView();
        loadData();
    }

    /**
     * 更新ui界面
     * */
    public void updateUI(DynamicPhoto.Model model){
        if(model==null){
            bottom_num.setText("暂无信息");
        }else{
            this.model=model;
            imageUrls.clear();
            for(int i=0,j=model.getData().size(); i<j; i++){
                imageUrls.add(model.getData().get(i).getImagepics());
            }
            String[] resKeys=new String[]{"getImagethpath","getTitle"};
            int[] reses=new int[]{R.id.gridview_image,R.id.gridview_name};
            classRoomAdapter = new MySimpleAdapter(this,model.getData(),R.layout.layout_item_gridview_image1,resKeys,reses);
            gridView.setAdapter(classRoomAdapter);
            bottom_num.setText("共 "+model.getData().size()+" 张");
        }
    }
    private void initView(){
        bottom_num=(TextView)findViewById(R.id.bottom_num);
        gridView =(GridView)findViewById(R.id.cnp_gridview);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectPhoto = model.getData().get(i);
                showPop3(gridView, imageUrls, i, DynamicPhotoListActivity.this, mShowPop3Listener);
//                showPop(gridView, selectPhoto.getImagepics());
            }
        });
    }

    private showPop3Listener mShowPop3Listener = new showPop3Listener() {
        @Override
        public void onClick(int type, int position) {
            if(type == 1 || type == 2){
                Intent intent = new Intent();
                Log.i("tag", "position:"+position);
                intent.setClass(DynamicPhotoListActivity.this, DynamicPhotoInfoActivity.class);
                intent.putExtra("dynamicPhoto", model.getData().get(position));
                intent.putExtra("album", mAlbum);
                if(type == 1){
                    intent.putExtra("etFocus", true);
                }
                startActivity(intent);
            }
        }
    };

    /*@Override
    public void showPop(View view, String bigImgPath) {
        View popView = this.getLayoutInflater().inflate(
                R.layout.layout_dynamic_photo_popupwindow, null);
        LinearLayout layout_photo_forward = (LinearLayout) popView.findViewById(R.id.layout_photo_forward);
        LinearLayout layout_photo_comment = (LinearLayout) popView.findViewById(R.id.layout_photo_comment);
        TextView tv_photo_detail = (TextView) popView.findViewById(R.id.tv_photo_detail);
        PhotoView pop_img = (PhotoView) popView.findViewById(R.id.pop_img);

        popView.setOnClickListener(mPopOnClickListener);
        layout_photo_forward.setOnClickListener(mPopOnClickListener);
        layout_photo_comment.setOnClickListener(mPopOnClickListener);
        tv_photo_detail.setOnClickListener(mPopOnClickListener);
        pop_img.setOnClickListener(mPopOnClickListener);

        popWin = new PopupWindow(popView, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        // 需要设置一下此参数，点击外边可消失
        popWin.setBackgroundDrawable(new BitmapDrawable());
        //设置点击窗口外边窗口消失
        popWin.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        popWin.setFocusable(true);
        popWin.setTouchable(true);
        popWin.showAtLocation(view, Gravity.CENTER, 0, 0);
        showDetailImage1(bigImgPath, pop_img, false, true);
    }*/

    private View.OnClickListener mPopOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.layout_photo_forward){
                Intent intent = new Intent();
                intent.setClass(DynamicPhotoListActivity.this, DynamicPhotoInfoActivity.class);
                intent.putExtra("dynamicPhoto", selectPhoto);
                intent.putExtra("album", mAlbum);
                startActivity(intent);
            }else if(view.getId() == R.id.layout_photo_comment){
                Intent intent = new Intent();
                intent.setClass(DynamicPhotoListActivity.this, DynamicPhotoInfoActivity.class);
                intent.putExtra("dynamicPhoto", selectPhoto);
                intent.putExtra("album", mAlbum);
                startActivity(intent);
            }else if(view.getId() == R.id.tv_photo_detail){
                Intent intent = new Intent();
                intent.setClass(DynamicPhotoListActivity.this, DynamicPhotoInfoActivity.class);
                intent.putExtra("dynamicPhoto", selectPhoto);
                intent.putExtra("album", mAlbum);
                startActivity(intent);
            }else if(view.getId() == R.id.pop_img){

            }

            selectPhoto = null;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if(popWin != null){
            popWin.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("新建相册")
                .setIcon(R.drawable.ic_setting)
                .setShowAsAction(
                        MenuItem.SHOW_AS_ACTION_ALWAYS);
        this.mymenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getTitle().equals("新建相册")){
            if(mPhotoSelctDialog == null){
                mPhotoSelctDialog = new Dialog(this, R.style.MyDialog);
                mPhotoSelctDialog.setContentView(R.layout.layout_dynamic_photo_dialog);
                mPhotoSelctDialog.getWindow().setLayout(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                LinearLayout layout_dialog_parent = (LinearLayout) mPhotoSelctDialog.findViewById(R.id.layout_dialog_parent);
                TextView tv_alter_album = (TextView) mPhotoSelctDialog.findViewById(R.id.tv_alter_album);
                TextView tv_del_album = (TextView) mPhotoSelctDialog.findViewById(R.id.tv_del_album);
                TextView tv_cancle_dialog = (TextView) mPhotoSelctDialog.findViewById(R.id.tv_cancle_dialog);

                View.OnClickListener mLayoutOnClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(view.getId() == R.id.tv_alter_album){
                            Intent intent = new Intent();
                            intent.setClass(DynamicPhotoListActivity.this, AddAlbumActivity.class);
                            intent.putExtra("album", mAlbum);
                            DynamicPhotoListActivity.this
                                    .startActivityForResult(intent, RESULT_FOR_ADD_ALBUM);
                        }else if(view.getId() == R.id.tv_del_album){
                            MyAlbumRequestListener requestListener =
                                    new MyAlbumRequestListener(DynamicPhotoListActivity.this);
                            requestListener.setListener(mAlbumRequestListener);
                            MyAlbumRequest request = new MyAlbumRequest(
                                    BaseTest.class,
                                    DynamicPhotoListActivity.this,
                                    mAlbum.getPaId()+"");
                            (DynamicPhotoListActivity.this).spiceManager.execute(
                                    request, request.getcachekey(), 1,
                                    requestListener.start());
                        }
                        mPhotoSelctDialog.dismiss();
                    }
                };
                layout_dialog_parent.setOnClickListener(mLayoutOnClickListener);
                tv_alter_album.setOnClickListener(mLayoutOnClickListener);
                tv_del_album.setOnClickListener(mLayoutOnClickListener);
                tv_cancle_dialog.setOnClickListener(mLayoutOnClickListener);
            }
            mPhotoSelctDialog.show();
            return true;
        }else if(item.getItemId() == android.R.id.home){
            if(midified){
                setResult(RESULT_FOR_ADD_ALBUM);
            }
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_BACK == event.getKeyCode()){
            if(midified){
                setResult(RESULT_FOR_ADD_ALBUM);
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    MyAlbumRequestListener.RequestListener mAlbumRequestListener = new MyAlbumRequestListener.RequestListener() {
        @Override
        public void onRequestSuccess(Object data) {
            setResult(RESULT_FOR_ADD_ALBUM);
            finish();
        }

        @Override
        public void onRequestFailure(SpiceException e) {

        }
    };

    private void loadData(){
        DynamicPhotoListRequestListener sendwordRequestListener = new DynamicPhotoListRequestListener(this);
        sendwordRequestListener.setListener(mPhotoListRequestListener);
        DynamicPhotoListRequest schoolRecipeRequest=new DynamicPhotoListRequest(DynamicPhoto.Model.class,this,mAlbum.getPaId());
        spiceManager.execute(schoolRecipeRequest, schoolRecipeRequest.getcachekey(), DurationInMillis.ONE_SECOND * 10,
                sendwordRequestListener.start());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_FOR_ADD_ALBUM
                && data != null
                && data.getSerializableExtra("album") != null){
            mAlbum = (Album) data.getSerializableExtra("album");
            titletext.setText(mAlbum.getAlbumName());
            midified = true;
        }
    }

    private DynamicPhotoListRequestListener.RequestListener mPhotoListRequestListener =
            new DynamicPhotoListRequestListener.RequestListener() {
        @Override
        public void onRequestSuccess(DynamicPhoto.Model data) {
            updateUI(data);
        }

        @Override
        public void onRequestFailure(SpiceException e) {
        }
    };

    public void finishActivity(){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        model=null;
        classRoomAdapter=null;
    }
}
