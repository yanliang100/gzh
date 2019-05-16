package com.lafei.gzh.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lafei.gzh.index.bean.AccessToken;
import com.lafei.gzh.index.bean.ApiTicket;
import com.lafei.gzh.index.bean.JsApiTicket;
import com.lafei.gzh.util.NetWorkUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class WeixinSchedule {
    @Value("${spring.weixin.appId}")
    private String appId;
    @Value("${spring.weixin.appSecret}")
    private String appSecret;
    @Value("${spring.weixin.access_token_url}")
    private String access_token_url;
    @Value("${spring.weixin.jsapi_ticket_url}")
    private String jsapi_ticket_url;
    @Value("${spring.weixin.api_ticket_url}")
    private String api_ticket_url;

    @Scheduled(fixedDelay=7000000)
    public void scheduled(){
        //获取access_token
        String token=getAccessToken();
        //获取jsapi_ticket
        getJsapiTicket(token);
        //获取api_ticket
        getApiTicket(token);
    }

    public String getAccessToken(){
        /**
         * 接口地址为https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET，其中grant_type固定写为client_credential即可。
         */
        //此请求为https的get请求，返回的数据格式为{"access_token":"ACCESS_TOKEN","expires_in":7200}
        String url = String.format(access_token_url, appId, appSecret);
        String result = NetWorkUtil.getHttpsResponse(url, "");
        System.out.println("获取到的access_token="+result);

        //使用FastJson将Json字符串解析成Json对象
        JSONObject json = JSON.parseObject(result);
        AccessToken.tokenName=json.getString("access_token");
        AccessToken.expireSecond=json.getInteger("expires_in");
        return AccessToken.tokenName;
    }

    public String getJsapiTicket(String token){
        String url = jsapi_ticket_url.replace("ACCESS_TOKEN" ,token );
        JSONObject result = NetWorkUtil.httpsRequest(url,"GET",null);
        System.out.println("获取到的jsapi_ticket="+result);

        String jsapi_ticket = result.getString("ticket");
        int activeTime=Integer.parseInt(result.getString("expires_in"));
        JsApiTicket.jsapiTicket=jsapi_ticket;
        JsApiTicket.activeTime=activeTime-600;
        return JsApiTicket.jsapiTicket;
    }

    public String getApiTicket(String token){
        String url = api_ticket_url.replace("ACCESS_TOKEN" ,token );
        JSONObject result = NetWorkUtil.httpsRequest(url,"GET",null);
        System.out.println("获取到的api_ticket="+result);

        String api_ticket = result.getString("ticket");
        int activeTime=Integer.parseInt(result.getString("expires_in"));
        ApiTicket.apiTicket=api_ticket;
        ApiTicket.activeTime=activeTime-600;
        return ApiTicket.apiTicket;
    }
}
