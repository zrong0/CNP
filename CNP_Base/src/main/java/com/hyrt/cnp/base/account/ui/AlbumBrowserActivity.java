package com.hyrt.cnp.base.account.ui;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.hyrt.cnp.base.R;
import com.hyrt.cnp.base.account.adapter.PhoneAlbumAdapter;
import com.jingdong.common.frame.BaseActivity;

import net.oschina.app.AppContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zoe on 2014-04-16.
 */
public class AlbumBrowserActivity extends BaseActivity{

    private static final String TAG = "AlbumBrowserActivity";
    private GridView mGridView;
    private TextView actionBarTitleCancel;
    private TextView actionBarTitleText;
    private TextView actionBarTitleSubmit;

    private int type = 0;//0:多选相册;1.单选相册

    private PhoneAlbumAdapter mAdapter;

    private List<String> imgagePaths = new ArrayList<String>();
    private List<String> checkeds = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_browser);

        Intent intent = getIntent();
        List<String> tempCheckeds = intent.getStringArrayListExtra("checkeds");
        if(tempCheckeds != null){
            checkeds.addAll(tempCheckeds);
            type = 0;
        }else{
            type = 1;
        }

        findView();
        setListener();
        mAdapter = new PhoneAlbumAdapter(imgagePaths, checkeds, type, this);
        mGridView.setAdapter(mAdapter);
        loadData();
    }

    public void loadData(){
        ContentResolver mContentResolver = getContentResolver();
        String[] projection = { MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.IMAGE_ID,
                MediaStore.Images.Thumbnails.DATA};
        Cursor mAlbumCursor = mContentResolver.query(
                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                projection, null, null, null);
        imgagePaths.clear();
        if(mAlbumCursor.moveToFirst()){
            int _id;
            int image_id;
            String image_path;
            int _idColumn = mAlbumCursor.getColumnIndex(MediaStore.Images.Thumbnails._ID);
            int image_idColumn = mAlbumCursor.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID);
            int dataColumn = mAlbumCursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);

            do {
                _id = mAlbumCursor.getInt(_idColumn);
                image_id = mAlbumCursor.getInt(image_idColumn);
                image_path = mAlbumCursor.getString(dataColumn);
                imgagePaths.add(image_path);
            } while (mAlbumCursor.moveToNext());
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initTitleview() {
        actionBar.hide();
    }

    public void setListener(){
     actionBarTitleSubmit.setOnClickListener(mOnClickListener);
     actionBarTitleCancel.setOnClickListener(mOnClickListener);
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.action_bar_title_cancel){
                finish();
            }else if(view.getId() == R.id.action_bar_title_submit){
                Intent data = new Intent();
                data.putStringArrayListExtra("checkeds", mAdapter.checkeds);
                setResult(AppContext.getInstance().RESULT_FOR_PHONE_ALBUM, data);
                finish();
            }
        }
    };

    public void findView(){
        mGridView = (GridView) findViewById(R.id.gv_phone_album);
        actionBarTitleCancel = (TextView) findViewById(R.id.action_bar_title_cancel);
        actionBarTitleText = (TextView) findViewById(R.id.action_bar_title_text);
        actionBarTitleSubmit = (TextView) findViewById(R.id.action_bar_title_submit);
    }
}
