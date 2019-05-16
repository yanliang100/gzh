package com.lafei.card.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lafei.gzh.bean.AccessToken;
import com.lafei.util.NetWorkUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("cardService")
public class CardService {
    // 第三方用户唯一凭证
    @Value("${spring.weixin.appId}")
    private String appId;
    // 密令appSecret
    @Value("${spring.weixin.appSecret}")
    private String appSecret;

    @Value("${spring.weixin.huiyuan_card_id}")
    private String huiyuan_card_id;

    @Value("${spring.weixin.huiyuan_card_list}")
    private String huiyuan_card_list;

    public JSONObject getCardList(String weixinOpenid){
        String url = String.format(huiyuan_card_list, AccessToken.tokenName);
        Map<String,String> params = new HashMap<String,String>();
        params.put("openid", weixinOpenid);
        params.put("card_id", huiyuan_card_id);
        String result=NetWorkUtil.httpRequest(url,"POST",JSON.toJSONString(params));
        return JSONObject.parseObject(result);
    }

}
