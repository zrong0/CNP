package com.hyrt.cnp.classroom.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.hyrt.cnp.base.account.model.ClassRoom;
import com.hyrt.cnp.base.account.utils.FaceUtils;
import com.hyrt.cnp.classroom.R;
import com.hyrt.cnp.classroom.adapter.ClassroomIndexAdapter;
import com.hyrt.cnp.classroom.request.ClassroomInfoRequest;
import com.hyrt.cnp.classroom.requestListener.ClassroomInfoRequestListener;
import com.jingdong.common.frame.BaseActivity;
import com.octo.android.robospice.persistence.DurationInMillis;

import roboguice.RoboGuice;

/**
 * Created by GYH on 14-1-16.
 */
public class ClassroomIndexActivity extends BaseActivity{
    private GridView gridView;
    private int[] imageResId;
    private String[] text={"班级公告","每日食谱","班级相册","班级成员"};
    private int[] bg;
    private Intent intent;
    private TextView renname,rennames;
    private ImageView classroomimage;
    private TextView classroominfo;
    @Inject
    @Named("schoolNoticeActivity")
    private Class schoolNoticeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroomindex);
        RoboGuice.getInjector(this.getApplicationContext()).injectMembers(this);
        initView();
        LoadData();
    }

    private void initView(){
        classroomimage=(ImageView)findViewById(R.id.classroomimage);
        classroominfo=(TextView)findViewById(R.id.classroomintro);
        renname=(TextView)findViewById(R.id.text_renname);
        rennames=(TextView)findViewById(R.id.text_rennames);
        imageResId = new int[] { R.drawable.classroom_notice, R.drawable.classroom_recipe,
                R.drawable.classroom_photo, R.drawable.classroom_member};
        bg = new int[]{R.color.classroomindex_notice,R.color.classroomindex_recipe,
                R.color.classroomindex_photo,R.color.classroomindex_member};
        gridView = (GridView) findViewById(R.id.schoolindexlist);
        ClassroomIndexAdapter schoolIndexAdapter=new ClassroomIndexAdapter(text,imageResId,bg,this);
        gridView.setAdapter(schoolIndexAdapter);
        gridView.setOnItemClickListener(new ItemClickListener());
    }

    private void LoadData(){
        ClassroomInfoRequestListener sendwordRequestListener = new ClassroomInfoRequestListener(this);
        ClassroomInfoRequest schoolRecipeRequest=new ClassroomInfoRequest(ClassRoom.Model2.class,this);
        spiceManager.execute(schoolRecipeRequest, schoolRecipeRequest.getcachekey(), DurationInMillis.ONE_SECOND * 10,
                sendwordRequestListener.start());
    }

    public void updateUI(ClassRoom.Model2 model2){
        renname.setText("班主任："+model2.getData().getRenname());
        rennames.setText("教师："+model2.getData().getRennames());
        titletext.setText(model2.getData().getRoomname());
        String bannerUrl = FaceUtils.getClassRoomImage(model2.getData().getClassroomID(),FaceUtils.FACE_SMALL);
        android.util.Log.i("tag", "bannerUrl:"+bannerUrl);
//        showDetailImage(bannerUrl,classroomimage,false);
        classroomimage.setImageResource(R.drawable.hua2);
        classroominfo.setText(model2.getData().getSignature());
    }

    class  ItemClickListener implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> arg0,View arg1,int arg2,long arg3) {
            int i = arg2;
            switch (i){
                case 0:
                    intent =new Intent();
                    intent.setClass(ClassroomIndexActivity.this,schoolNoticeActivity);
                    intent.putExtra("data","classroom");
                    startActivity(intent);
                    break;
                case 1:
                    startActivity(new Intent().setClass(ClassroomIndexActivity.this,ClassroomRecipeInfoActivity.class));
                    break;
                case 2:
                    intent =new Intent();
                    intent.setClass(ClassroomIndexActivity.this,ClassroomAlbumActivity.class);
                    intent.putExtra("Category","ClassroomIndexActivity");
                    startActivity(intent);
                    break;
                case 3:
                    startActivity(new Intent().setClass(ClassroomIndexActivity.this,ClassroomBabayActivity.class));
                    break;
            }
        }
    }
}
