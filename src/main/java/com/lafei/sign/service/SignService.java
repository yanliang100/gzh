package com.lafei.sign.service;

import com.alibaba.fastjson.JSONObject;
import com.lafei.gzh.bean.ApiTicket;
import com.lafei.gzh.bean.JsApiTicket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.UUID;

@Service("signService")
public class SignService {
    // 第三方用户唯一凭证
    @Value("${spring.weixin.appId}")
    private String appId;
    // 密令appSecret
    @Value("${spring.weixin.appSecret}")
    private String appSecret;
    //card_id
    @Value("${spring.weixin.huiyuan_card_id}")
    private String huiyuan_card_id;
    //jsapi_ticket_url
    @Value("${spring.weixin.jsapi_ticket_url}")
    private String jsapi_ticket_url;

    public JSONObject jsapiSign( String url) {
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String signature = "";
        // 注意这里参数名必须全部小写，且必须有序
        String string1 = "jsapi_ticket=" + JsApiTicket.jsapiTicket + "&noncestr="
                + nonce_str + "&timestamp=" + timestamp + "&url=" + url;
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JSONObject json=new JSONObject();
        json.put("appId", appId);
        json.put("url", url);
        json.put("jsapi_ticket", JsApiTicket.jsapiTicket);
        json.put("nonceStr", nonce_str);
        json.put("timestamp", timestamp);
        json.put("signature", signature);
        json.put("signUrl", url);
        return json;
    }

    public JSONObject apiSign(String card_id) {
        String timestamp = create_timestamp();
        String signature = "";
        // 注意这里参数名必须全部小写，且必须有序
        String string1 = timestamp + ApiTicket.apiTicket + card_id;
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JSONObject json=new JSONObject();
        json.put("timestamp", timestamp);
        json.put("signature", signature);
        json.put("cardid", card_id);
        return json;
    }

    public  String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public  String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    public  String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

    public String getHuiyuanCardId() {
        return this.huiyuan_card_id;
    }
}
