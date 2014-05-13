package com.hyrt.cnp.dynamic.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.hyrt.cnp.dynamic.R;
import com.hyrt.cnp.dynamic.ui.MyForwardListActivity;
import com.hyrt.cnp.dynamic.ui.MyItListActivity;
import com.hyrt.cnp.dynamic.ui.MycommentListActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by GYH on 14-3-12.
 */
public class AboutmeFragment extends Fragment{

    private View rootview;

    private ListView listView;
    private String[] strs = new String[]{"@我的","评论","转发"};
    private int[] imageids=new int[]{R.drawable.cnp_aboutme_wode,R.drawable.cnp_aboutme_pl,R.drawable.cnp_aboutme_zf};

    public static AboutmeFragment instantiation(int position) {
        AboutmeFragment fragment = new AboutmeFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview=inflater.inflate(R.layout.fragment_aboutme,container,false);
        initView(rootview);
        initData();
        return rootview;
    }

    private void initView(View view){
        listView=(ListView)view.findViewById(R.id.aboutme_listview);
    }

    private void initData(){

        List<Map<String, Object>> contents=new ArrayList<Map<String, Object>>();

        for (int i = 0; i < strs.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("imageid",imageids[i]);
            map.put("TITLE", strs[i]);
            contents.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(getActivity(),
                contents, R.layout.layout_aboutme_item,
                new String[] {"imageid","TITLE" }, new int[] {
                R.id.item_image,R.id.item_text});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setOnlisview(i);
            }
        });
    }

    private void setOnlisview(int id){
        switch (id){
            case 0:
                startActivity(new Intent().setClass(getActivity(), MyItListActivity.class));
                break;
            case 1:
                startActivity(new Intent().setClass(getActivity(), MycommentListActivity.class));
                break;
            case 2:
                startActivity(new Intent().setClass(getActivity(), MyForwardListActivity.class));
                break;
        }
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null)
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
    }
}
