package com.hyrt.cnp.dynamic.adapter;

import com.jingdong.app.pad.adapter.MySimpleAdapter;
import com.jingdong.common.frame.BaseActivity;

import java.util.List;

/**
 * Created by GYH on 14-1-8.
 */
public class DynamicPhotoInfoAdapter extends MySimpleAdapter {

    public DynamicPhotoInfoAdapter(BaseActivity context, List data, int layoutId, String[] resKeys, int[] reses) {
        super(context, data, layoutId, resKeys, reses);
    }
}
