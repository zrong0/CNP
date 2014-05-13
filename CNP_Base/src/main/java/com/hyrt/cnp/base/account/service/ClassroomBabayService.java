package com.hyrt.cnp.base.account.service;

import com.hyrt.cnp.base.account.CNPClient;
import com.hyrt.cnp.base.account.model.ClassRoomBabay;

import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

/**
 * Created by GYH on 14-1-17.
 */
public class ClassroomBabayService{

    private CNPClient cnpClient;

    public ClassroomBabayService(CNPClient cnpClient){
        this.cnpClient = cnpClient;
    }

    public ClassRoomBabay.Model getclassroombabayData(RestTemplate restTemplate){
        cnpClient.configureRequest();
        HashMap<String, String> params = cnpClient.getParamsforGet();
        return  restTemplate.getForObject("http://api.chinaxueqian.com/classroom/babylist/?" +
                "token={token}&uuid={uuid}&cid={cid}",
                ClassRoomBabay.Model.class, params);
    }
}
