package com.hyrt.cnp.base.account.service;

import com.hyrt.cnp.base.account.CNPClient;
import com.hyrt.cnp.base.account.model.ItInfo;

import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

/**
 * Created by GYH on 2014/4/6.
 */
public class ItInfoService {

    private CNPClient cnpClient;

    public ItInfoService(CNPClient cnpClient){
        this.cnpClient = cnpClient;
    }

    public ItInfo.Model getMyitinfoData(RestTemplate restTemplate,String more){
        cnpClient.configureRequest();
        HashMap<String, String> params = cnpClient.getParamsforGet();
        params.put("more",more);
        return  restTemplate.getForObject("http://api.chinaxueqian.com/home/tips_at/?" +
                        "token={token}&uuid={uuid}&more={more}",
                ItInfo.Model.class, params);
    }

    public ItInfo.Model getForwardData(RestTemplate restTemplate,String more){
        cnpClient.configureRequest();
        HashMap<String, String> params = cnpClient.getParamsforGet();
        params.put("more",more);
        return  restTemplate.getForObject("http://api.chinaxueqian.com/home/tips_tran/?" +
                        "token={token}&uuid={uuid}&more={more}",
                ItInfo.Model.class, params);
    }

    public ItInfo.Model getCommentData(RestTemplate restTemplate,String more){
        cnpClient.configureRequest();
        HashMap<String, String> params = cnpClient.getParamsforGet();
        params.put("more",more);
        return  restTemplate.getForObject("http://api.chinaxueqian.com/home/tips_comment/?" +
                        "token={token}&uuid={uuid}&more={more}",
                ItInfo.Model.class, params);
    }
}
