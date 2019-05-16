package com.lafei.trans.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lafei.util.MD5;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Service("baiduTransService")
public class BaiduTransService {
    @Value("${spring.weixin.translate_app_id}")
    private String translate_app_id;
    @Value("${spring.weixin.translate_security_key}")
    private String translate_security_key;


    /**
     * 利用百度翻译的接口url，进行翻译功能
     * @param source  需要进行翻译的内容
     * @return  返回多种语言的翻译结果（中文，英式英语，美式英语）
     */
    public String translate(String source) throws UnsupportedEncodingException {
        //这个自己在百度开发服务中心找的，还有很多其他的API的接口
        String url = "http://api.fanyi.baidu.com/api/trans/vip/translate?q=KEYWORD&from=jp&to=zh&appid=APPID&salt=SALT&sign=SIGN";
        //一定要将需要进行翻译的内容转为utf-8，因为对于中文是无法进行http传输的
        url = url.replace("KEYWORD", URLEncoder.encode(source,"UTF-8"));
        url = url.replace("APPID", translate_app_id);
        //随机数
        String salt = String.valueOf(System.currentTimeMillis());
        url = url.replace("SALT", salt);
        // 加密前的原文
        String sign = translate_app_id + source + salt +translate_security_key;
        url = url.replace("SIGN", MD5.md5(sign));
        //返回结果为json格式
        JSONObject jsonObject = doGetStr(url);
        System.out.println("百度翻译:"+jsonObject);
        //翻译的源语言
        String fromLanguage = jsonObject.getString("from");
        //翻译的目标语言
        String toLanguage = jsonObject.getString("to");
        //翻译的结果
        JSONArray transResult = jsonObject.getJSONArray("trans_result");
        String transSrc = "";
        String transDis = "";
        if(transResult!=null){
            //拼接翻译结果
            StringBuilder stringBuilder = new StringBuilder();

            for(int i =0 ; i<transResult.size() ; i++){
                JSONObject o = (JSONObject) transResult.get(i);
                transSrc = o.getString("src");
                transDis = o.getString("dst");
                stringBuilder.append("翻译的内容为("+ fromLanguage +"):"+ transSrc +"\n");
                stringBuilder.append("翻译的结果为("+ toLanguage +"):"+ transDis +"\n");
            }
        }
        return transDis;
    }


    /**
     * Get请求，方便到一个url接口来获取结果
     * @param url
     * @return
     */
    public JSONObject doGetStr(String url) {
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        JSONObject jsonObject = null;
        try {
            HttpResponse response = defaultHttpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity, "UTF-8");
                jsonObject = JSONObject.parseObject(result);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
