package com.lafei.gzh.trans.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lafei.gzh.index.bean.AccessToken;
import com.lafei.gzh.util.BaiduHttpUtil;
import com.lafei.gzh.util.Base64Util;
import com.lafei.gzh.util.NetWorkUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;

@Service("baiduOCRService")
public class BaiduOCRService {
    @Value("${spring.weixin.baidu_app_id}")
    private String baidu_app_id;
    @Value("${spring.weixin.baidu_secret}")
    private String baidu_secret;
    @Value("${spring.weixin.baidu_token_url}")
    private String baidu_token_url;
    @Value("${spring.weixin.baidu_bce_url}")
    private String baidu_bce_url;
    @Value("${spring.weixin.get_media_url}")
    private String get_media_url;

    public String getJpaWords(String mediaId) throws Exception {
        byte[] imgData=NetWorkUtil.getMedia(get_media_url,mediaId, AccessToken.tokenName);
        String words="";
        String url = String.format(baidu_token_url, baidu_app_id, baidu_secret);
        JSONObject json= NetWorkUtil.httpsRequest(url,"POST",null);
        String access_token=json.get("access_token").toString();
        String imgStr = Base64Util.encode(imgData);
        String params = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(imgStr, "UTF-8")+"&language_type=JAP";
        /**
         * 线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
         */
        String result = BaiduHttpUtil.post(baidu_bce_url, access_token, params);
        if(result!=null && !result.isEmpty()){
            JSONObject resultJap=JSONObject.parseObject(result);
            JSONArray jsonArray= (JSONArray) resultJap.get("words_result");

            for(Object jsona:jsonArray){
                JSONObject row=(JSONObject)jsona;
                words+=row.get("words");
            }
        }
        return words;
    }
}
