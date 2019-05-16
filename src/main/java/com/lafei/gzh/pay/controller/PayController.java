package com.lafei.gzh.pay.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lafei.gzh.ad.bean.Ad;
import com.lafei.gzh.ad.service.AdService;
import com.lafei.gzh.coupon.bean.Coupon;
import com.lafei.gzh.coupon.bean.UserCoupon;
import com.lafei.gzh.coupon.service.CouponService;
import com.lafei.gzh.pay.service.PayService;
import com.lafei.gzh.order.bean.Order;
import com.lafei.gzh.order.service.OrderService;
import com.lafei.gzh.pay.sdk.WXPayUtil;
import com.lafei.gzh.points.service.PointsRuleService;
import com.lafei.gzh.user.bean.User;
import com.lafei.gzh.user.service.UserService;
import com.lafei.gzh.util.CryptoUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class PayController {

    // 注入userService
    @Resource(name = "userService")
    private UserService userService;
    // 注入orderService
    @Resource(name = "orderService")
    private OrderService orderService;

    // 注入payService
    @Resource(name = "payService")
    private PayService payService;

    // 注入payService
    @Resource(name = "couponService")
    private CouponService couponService;

    // 注入pointsRuleService
    @Resource(name = "pointsRuleService")
    private PointsRuleService pointsRuleService;

    // 注入userService
    @Resource(name = "adService")
    private AdService adService;

    /**
     * 一键购物主页。
     * @return
     */
    @RequestMapping(value ="/pay")
    public String pay(HttpServletRequest req){
        try {
            User weixinUser = userService.getUser(req);
            String weixinOpenid= weixinUser.getWeixinOpenid();
            return payService.selectPay(weixinOpenid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    /**
     * 生成二维码页面
     */
    @RequestMapping("/pay_qr_code")
    public String pay_qr_code(Model model, @RequestParam String amount){
        try {
            BigDecimal orderPrice=BigDecimal.valueOf(Double.parseDouble(amount));
            Order order=orderService.birthNewOrderSn(orderPrice);
            model.addAttribute("order",order);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "pay_qr_code";
    }

    /**
     * 生成二维码图片并直接以流的形式输出到页面
     */
    @RequestMapping("/pay_qr_code.img")
    public void getQRCode(@RequestParam String orderSn, @RequestParam String orderPrice, HttpServletResponse response){
        Map<String,String> map=new HashMap<String,String>();
        map.put("orderSn",orderSn);
        map.put("orderPrice",orderPrice);
        map.put("sendTime",String.valueOf(System.currentTimeMillis()));
        map.put("duration","7200000");//付款有效时间2小时
        String jsonString = JSON.toJSONString(map);
        payService.generateQRCode(jsonString,response);
    }


    @RequestMapping("is_valid_wxpay")
    @ResponseBody
    public Map<String,Object> wx_pay_notify(@RequestParam String code){
        String contents= CryptoUtil.decode32(CryptoUtil.DEFAULT_SECRET_KEY3, code);
        JSONObject jsonObject = JSONObject.parseObject(contents);
        Map<String,Object> map = (Map<String,Object>)jsonObject;
        try{
            String orderSn=map.get("orderSn").toString();
            Order order=orderService.queryOrderByOrderSn(orderSn);
            boolean payStatus=order.getPayStatus();
            long startTime=Long.parseLong(map.get("sendTime").toString());
            long duration=Long.parseLong(map.get("duration").toString());
            long nowTime=System.currentTimeMillis();
            map.put("isValid",nowTime-startTime<duration?"1":(payStatus?"2":"-1"));
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 微信支付界面
     */
    @RequestMapping("unified_order")
    public String unified_order(HttpServletRequest req, Model model, String orderSn,String orderPrice){
        User currentUser=userService.getUser(req);
        String weixinOpenid=currentUser.getWeixinOpenid();
        try {
            List<Coupon> coupons=couponService.queryAvailableCoupons(weixinOpenid,orderPrice);
            if(coupons!=null && coupons.size()>0){
                model.addAttribute("coupon",coupons.get(0));
            }
            model.addAttribute("headimgurl",currentUser.getHeadimgurl());
            model.addAttribute("orderSn",orderSn);
            model.addAttribute("orderPrice",orderPrice);
            Ad ad=adService.queryYhhd();
            if(ad!=null){
                model.addAttribute("promotion",ad.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "unified_order";
    }

    /**
     * 请求微信支付
     * @param req
     * @param out_trade_no 此处订单编号orderSn_couponid
     * @param totalFee
     * @return
     */
    @RequestMapping("doUnifiedOrder")
    @ResponseBody
    public JSONObject doUnifiedOrder(HttpServletRequest req, @RequestParam String out_trade_no, @RequestParam String totalFee){
        User currentUser=userService.getUser(req);
        String openid=currentUser.getWeixinOpenid();

        JSONObject result=null;
        try {
            String prepay_id = payService.genetareUnifiedOrder(req,out_trade_no,totalFee,openid,"东方有泉家居馆商场买单");//获取prepay_id
            Map<String, String> getParams =payService.getUnifiedOrderInfo(prepay_id);
            result=JSONObject.parseObject(JSON.toJSONString(getParams));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 微信回调
     * 微信支付成功,微信发送的callback信息,请注意修改订单信息
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/cg")
    public String cg(HttpServletRequest request,HttpServletResponse response){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String now=df.format(new Date());// new Date()为获取当前系统时间

        InputStream is = null;
        try {
            is = request.getInputStream();//获取请求的流信息(这里是微信发的xml格式所有只能使用流来读)
            String xml = IOUtils.toString(is, "UTF-8");
            Map<String, String> notifyMap = WXPayUtil.xmlToMap(xml);//将微信发的xml转map

            if(notifyMap.get("return_code").equals("SUCCESS")){
                if(notifyMap.get("result_code").equals("SUCCESS")){
                    String total_fee = notifyMap.get("total_fee");//实际支付的订单金额:单位 分
                    BigDecimal actualPrice = (new BigDecimal(total_fee).divide(new BigDecimal("100"))).setScale(2);//将分转换成元-实际支付金额:元
                    String openid = notifyMap.get("openid");  //如果有需要可以获取
                    String out_trade_no = notifyMap.get("out_trade_no");//商户订单号
                    String[] sns=out_trade_no.split("_");
                    String orderSn=sns[1];

                    //更新order对应字段/已支付金额/状态码等
                    Order order=orderService.queryOrderByOrderSn(orderSn);
                    order.setUserId(openid);
                    order.setActualPrice(actualPrice);
                    order.setOrderStatus(true);
                    order.setPayStatus(true);
                    order.setPayTime(now);
                    order.setCallbackStatus("true");
                    orderService.update(order);

                    //更新优惠券状态
                    String couponId=sns.length>2?sns[2]:"";
                    if(!couponId.isEmpty()){
                        UserCoupon userCoupon=new UserCoupon();
                        userCoupon.setCouponId(Integer.parseInt(couponId));
                        userCoupon.setWeixinOpenid(openid);
                        userCoupon.setOrderSn(orderSn);
                        userCoupon.setState("1");
                        userCoupon.setUsedTime(now);
                        couponService.updateUserCoupon(userCoupon);
                    }

                    //添加用户积分数及添加积分记录表记录
                    User user=userService.findByWeixinOpenid(openid);
                    Integer rewardPoints= user.getRewardPoints();
                    rewardPoints+=pointsRuleService.getRewardPoints(actualPrice.intValue());
                    user.setRewardPoints(rewardPoints);
                    Integer quanDou= user.getQuanDou();
                    quanDou+=actualPrice.intValue()*5;
                    user.setQuanDou(quanDou);
                    user.setRegistSource("index");
                    userService.update(user);
                }
            }

            //告诉微信服务器收到信息了，不要在调用回调action了========这里很重要回复微信服务器信息用流发送一个xml即可
            response.getWriter().write("<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>");
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
