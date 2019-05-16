package com.lafei.pay.service;

import com.lafei.pay.sdk.WXPayUtil;
import com.lafei.user.bean.User;
import com.lafei.user.mapper.UserMapper;
import com.lafei.util.CryptoUtil;
import com.lafei.util.NetWorkUtil;
import com.lafei.zxing.ZXingQRCodeGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

@Service("payService")
public class PayService {
    @Value("${spring.weixin.appId}")
    private String appId;
    @Value("${spring.weixin.domain}")
    private String domain;
    @Value("${spring.weixin.appSecret}")
    private String appSecret;
    @Value("${spring.weixin.mch_id}")
    private String mch_id;
    @Value("${spring.weixin.api_key}")
    private String api_key;
    @Value("${spring.weixin.prepay_url}")
    private String prepay_url;
    @Value("${spring.weixin.notify_url}")
    private String notify_url;
    @Value("${spring.weixin.trade_type}")
    private String trade_type;

    @Resource
    private UserMapper userMapper;

    /**
     * produceQR:生成二维码. <br/>
     * @author lcma
     * @param response
     * @throws IOException
     * @since JDK 1.7
     */
    public void generateQRCode(String contents,HttpServletResponse response){
        try {
            OutputStream stream = response.getOutputStream();
            ZXingQRCodeGenerator.Encode_QR_CODE(CryptoUtil.encode32(CryptoUtil.DEFAULT_SECRET_KEY3, contents),stream);
            stream.flush();
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 根据配置选择是商家还是客户
     * @param weixinOpenid
     * @return
     */
    public String selectPay(String weixinOpenid) throws Exception {
        List<User> cashiers=userMapper.selectCashiers();
        for(User user:cashiers){
            if(user.getWeixinOpenid().equals(weixinOpenid)){
                //扫描商品并生成二维码
                return "pay_store";
            }
        }
        //扫一扫获取付款额
        return "pay_customer";
    }



    /**
     * 请求微信支付系统的接口，商户后台系统调用统一下单接口在微信支付服务后台生成预支付交易单
     * @param out_trade_no
     * @param totalFee
     * @param openid
     * @param body
     * @return
     * @throws Exception
     */
    public String genetareUnifiedOrder(HttpServletRequest req, String out_trade_no, String totalFee, String openid, String body) throws Exception {
        String nonce_str=WXPayUtil.generateNonceStr();
        String total_fee=(int)(Double.parseDouble(totalFee)*100)+"";

        TreeMap<String, String> requestParams = new TreeMap<String, String>();
        requestParams.put("appid", appId);//公众账号id
        requestParams.put("mch_id", mch_id); //商户号
        requestParams.put("body", body);//商品描述
        requestParams.put("trade_type", trade_type);
        requestParams.put("nonce_str", nonce_str);//随机字符串
        requestParams.put("notify_url", domain+notify_url);//通知地址
        requestParams.put("out_trade_no",System.currentTimeMillis()+"_"+out_trade_no);//订单号
        requestParams.put("total_fee", total_fee);//订单总金额
        requestParams.put("openid", openid);//用户标识
        String spbillCreateIp = req.getRemoteAddr();
        requestParams.put("spbill_create_ip", spbillCreateIp);//用户终端ip

        String sign = WXPayUtil.generateSignature(requestParams,api_key);//生成签名
        requestParams.put("sign", sign);

        String xml =WXPayUtil.mapToXml(requestParams);//最终的提交xml
        String unifiedorder = NetWorkUtil.httpRequest(prepay_url,"POST",xml);

        Map<String, String> map = WXPayUtil.xmlToMap(unifiedorder);
        String prepay_id = map.get("prepay_id");//prepay_id

        return prepay_id;
    }

    public Map<String, String> getUnifiedOrderInfo(String prepay_id) throws Exception {
        /**
         * 统一下单接口返回正常的prepay_id，再按签名规范重新生成签名后，将数据传输给APP。
         * 参与签名的字段名为appId，partnerId，prepayId，nonceStr，timeStamp，package。
         * 注意：package的值格式为Sign=WXPay
         */
        TreeMap<String, String> getParams = new TreeMap<String, String>();
        getParams.put("appId",  appId);
        getParams.put("timeStamp", WXPayUtil.getCurrentTimestamp()+"");
        getParams.put("nonceStr", WXPayUtil.generateNonceStr());
        getParams.put("package", "prepay_id="+prepay_id);
        getParams.put("signType", "MD5");
        String paySign  = WXPayUtil.generateSignature(getParams,api_key);
        getParams.put("paySign", paySign);

        return getParams;
    }
}
