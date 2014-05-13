package com.hyrt.cnp.classroom.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyrt.cnp.base.account.model.Comment;
import com.hyrt.cnp.base.account.model.Photo;
import com.hyrt.cnp.classroom.R;
import com.hyrt.cnp.classroom.adapter.ClassRoomAdapter;
import com.hyrt.cnp.classroom.request.ClassroomaddcommentRequest;
import com.hyrt.cnp.classroom.request.ClassroomcommentRequest;
import com.hyrt.cnp.classroom.requestListener.ClassroomaddcommentRequestListener;
import com.hyrt.cnp.classroom.requestListener.ClassroomcommentRequestListener;
import com.hyrt.cnp.base.view.Mylistview;
import com.jingdong.common.frame.BaseActivity;
import com.octo.android.robospice.persistence.DurationInMillis;

/**
 * Created by GYH on 14-1-22.
 */
public class ClassroomphotoinfoActivity extends BaseActivity{

    private ImageView imgphoto;
    private TextView albumname;
    private TextView photoname;
    private EditText editcommit;
    private TextView btnset;
    private Mylistview listView;
    private Photo photo;
    private ClassRoomAdapter classRoomAdapter;
    private boolean etFocus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroomphotoinfo);
        initView();
        initData();
        LoadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(etFocus){
            editcommit.requestFocus();
            etFocus = false;
        }
    }

    private void initView(){
        imgphoto=(ImageView)findViewById(R.id.img_photo);
        albumname=(TextView)findViewById(R.id.text_albumname);
        photoname=(TextView)findViewById(R.id.text_photoname);
        editcommit=(EditText)findViewById(R.id.edit_commit);
        btnset=(TextView)findViewById(R.id.btn_set);
        listView = (Mylistview)findViewById(R.id.commit_listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        btnset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editcommit.getText().toString().equals("")){
                    addcomment();
                }else{
                    Toast.makeText(ClassroomphotoinfoActivity.this,"评论不能为空！",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //TODO 专辑名称是什么
    private void initData(){
        Intent intent = getIntent();
        etFocus = intent.getBooleanExtra("etFocus", false);
        photo=(Photo)intent.getSerializableExtra("vo");
        photoname.setText("照片名称："+photo.getTitle());
        String Category=intent.getStringExtra("Category");
        if(Category.equals("ClassroomIndexActivity")){
            titletext.setText("班级相册");
            albumname.setText("专辑名称：班级专辑");
        }else if(Category.equals("BabayIndexActivity")){
            titletext.setText("动感相册");
            albumname.setText("专辑名称：个人专辑");
        }
        showDetailImage(photo.getImagepics(),imgphoto,false);
    }

    private void LoadData(){
        ClassroomcommentRequestListener sendwordRequestListener = new ClassroomcommentRequestListener(this);
        ClassroomcommentRequest schoolRecipeRequest=
                new ClassroomcommentRequest(Comment.Model.class,this,photo.getPhotoID()+"","15");
        spiceManager.execute(schoolRecipeRequest, schoolRecipeRequest.getcachekey(), 1000,
                sendwordRequestListener.start());
    }

    public void updateUI(Comment.Model model){
        String[] resKeys=new String[]{"getphotoImage","getUsername","getCreatdate2","getContent"};
        int[] reses=new int[]{R.id.comment_photo,R.id.text_name,R.id.text_time,R.id.text_con};
        classRoomAdapter = new ClassRoomAdapter(this,model.getData(),R.layout.layout_item_comment,resKeys,reses);
        listView.setAdapter(classRoomAdapter);
    }

    public void ShowSuccess(){
        Toast toast = Toast.makeText(ClassroomphotoinfoActivity.this, "添加评论成功", 0);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
//        Toast.makeText(ClassroomphotoinfoActivity.this,"添加评论成功",Toast.LENGTH_SHORT).show();
        editcommit.setText("");
        LoadData();//刷新
        //隐藏键盘
        ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                ClassroomphotoinfoActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
    private void addcomment(){
        Comment comment=new Comment();
        comment.set_id(photo.getUserID()+"");
        comment.setInfoID(photo.getPhotoID()+"");
        comment.setInfoTitle(photo.getTitle());
        comment.setInfoUserId(photo.getUserID()+"");
        comment.setInfoNurseryId(photo.getNurseryID()+"");
        comment.setInfoClassroomId(photo.getClassroomID()+"");
        comment.setSiteid("15");
        comment.setUrl(photo.getImagepics());
        comment.setLstatus("Y");
        comment.setContent(editcommit.getText().toString());
        comment.setReply("0");
        comment.setRecontent("");
        comment.setReuserId("");
        comment.setReusername("");
        comment.setRedate("");
        ClassroomaddcommentRequestListener sendwordRequestListener = new ClassroomaddcommentRequestListener(this);
        ClassroomaddcommentRequest schoolRecipeRequest=
                new ClassroomaddcommentRequest(Comment.Model3.class,this,comment);
        spiceManager.execute(schoolRecipeRequest, schoolRecipeRequest.getcachekey(), DurationInMillis.ONE_SECOND * 10,
                sendwordRequestListener.start());
    }
}
