package com.hyrt.cnp.dynamic.request;

import android.content.Context;

import com.google.inject.Inject;
import com.hyrt.cnp.base.account.model.Base;
import com.hyrt.cnp.base.account.request.BaseRequest;
import com.hyrt.cnp.base.account.service.DynamicService;

/**
 * Created by GYH on 14-1-23.
 */
public class BabayDynamicRequest extends BaseRequest {

    @Inject
    private DynamicService schoolListService;
    private String uid;
    private String more;
    private String did;
    private boolean isAll;
    private int type;//0:动态；1.转发

    public BabayDynamicRequest(Class clazz, Context context,String uid,String more) {
        super(clazz, context);
        this.uid=uid;
        this.more=more;
        isAll = false;
        this.type = 0;
    }

    public BabayDynamicRequest(Class clazz, Context context,String uid,String more, boolean isAll) {
        super(clazz, context);
        this.uid=uid;
        this.more=more;
        this.isAll = isAll;
        this.type = 0;
    }

    public BabayDynamicRequest(Class clazz, Context context,String uid, String did, String more) {
        super(clazz, context);
        this.uid=uid;
        this.more=more;
        this.type = 1;
        this.did = did;
    }

    @Override
    public Base run() {
        android.util.Log.i("tag", "run more :"+more+" type:"+type);
        if(type == 1){
            if("1".equals(more)){
                return schoolListService.getBabayDynamicData(getRestTemplate(),uid, did);
            }else{
                return schoolListService.getBabayDynamicData(getRestTemplate(),uid, did, more);
            }
        }else{
            if(more.equals("1")){
                return schoolListService.getBabayDynamicData(getRestTemplate(),uid, isAll);
            }else{
                return schoolListService.getBabayDynamicMoreData(getRestTemplate(), uid, more, isAll);
            }
        }

    }


    @Override
    public int compareTo(Object o) {
        return 0;
    }

    public String getcachekey(){
        return "Babydynamic"+uid+more;
    }
}
