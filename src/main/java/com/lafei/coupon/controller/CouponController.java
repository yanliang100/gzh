package com.lafei.coupon.controller;

import com.alibaba.fastjson.JSONObject;
import com.lafei.coupon.bean.Coupon;
import com.lafei.coupon.service.CouponService;
import com.lafei.goods.service.ChannelService;
import com.lafei.gzh.bean.AccessToken;
import com.lafei.gzh.bean.Result;
import com.lafei.sign.service.SignService;
import com.lafei.user.bean.User;
import com.lafei.user.service.UserService;
import com.lafei.util.NetWorkUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class CouponController {
    // 注入userService
    @Resource(name = "userService")
    private UserService userService;
    //注入signService
    @Resource(name = "signService")
    private SignService signService;
    //注入couponService
    @Resource(name = "couponService")
    private CouponService couponService;
    // 注入goodsService
    @Resource(name = "channelService")
    private ChannelService channelService;

    @RequestMapping(value ="/coupon")
    public String coupon(HttpServletRequest req,Model model) {
        try{
            User weixinUser = userService.getUser(req);
            String weixinOpenid=weixinUser.getWeixinOpenid();
            model.addAttribute("coupons0",couponService.queryCoupons(0,weixinOpenid));
            model.addAttribute("coupons1",couponService.queryCoupons(1,weixinOpenid));
            model.addAttribute("coupons2",couponService.queryCoupons(2,weixinOpenid));
            model.addAttribute("coupons3",couponService.queryCoupons(3,weixinOpenid));
            model.addAttribute("coupons4",couponService.queryCoupons(4,weixinOpenid));
            model.addAttribute("coupons5",couponService.queryCoupons(5,weixinOpenid));
            model.addAttribute("coupons6",couponService.queryCoupons(6,weixinOpenid));
            model.addAttribute("coupons7",couponService.queryCoupons(7,weixinOpenid));
            model.addAttribute("coupons8",couponService.queryCoupons(8,weixinOpenid));
        }catch (Exception e){
            e.printStackTrace();
        }
        return "coupon";
    }

    @RequestMapping(value ="/recieve_coupon")
    @ResponseBody
    public Result recieve_coupon(HttpServletRequest req) {
        Result result=new Result();
        User weixinUser=null;
        String weixinOpenid="";
        try{
            User currentUser=userService.getUser(req);
            weixinOpenid=currentUser.getWeixinOpenid();
            weixinUser=userService.findByWeixinOpenid(weixinOpenid);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(weixinUser!=null){
            boolean isOnline=Boolean.parseBoolean(weixinUser.getIsOnline());
            if(isOnline){
                try{
                    if(couponService.recieveCoupon(req.getParameter("id"),weixinOpenid)){
                        result.setCode("6000");
                        result.setMessage("领券成功");
                    }else{
                        result.setCode("-6000");
                        result.setMessage("领券失败");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else{
                //先登录
                result.setCode("-8000");
                result.setMessage("登录后再领取");
            }
        }else{
            //先注册
            result.setCode("-1000");
            result.setMessage("注册后再领取");
        }
        return result;
    }
    /**
     * 获取卡券货架
     * @return
     */
    public String get_landing_page() {
        String url="https://api.weixin.qq.com/card/landingpage/create?access_token="+ AccessToken.tokenName;
        String data="{  \n" +
                "\"banner\":\"http://mmbiz.qpic.cn/mmbiz/iaL1LJM1mF9aRKPZJkmG8xXhiaHqkKSVMMWeN3hLut7X7h  icFN\",\n" +
                "   \"page_title\": \"惠城优惠大派送\",\n" +
                "   \"can_share\": true,\n" +
                "   \"scene\": \"SCENE_NEAR_BY\",\n" +
                "   \"card_list\": [\n" +
                "       {\n" +
                "           \"card_id\": \"p1uQj5vgEfq8FMBi5bp33uOVGGN4\",\n" +
                "           \"thumb_url\": \"www.qq.com/a.jpg\"\n" +
                "       }\n" +
                "   ]\n" +
                "}";
        JSONObject json=NetWorkUtil.httpsRequest(url,"POST",data);;
        return json.get("url").toString();
    }

    /**
     * 获取优惠券
     */
    @RequestMapping(value ="/get_hy_coupon")
    @ResponseBody
    public Map<String,Object> get_hy_coupon(String url) throws Exception {
        Map<String,Object> result=new HashMap<String,Object>();
        JSONObject jsapiSign=signService.jsapiSign(url);
        Coupon coupon=couponService.queryTheLastCoupon();
        JSONObject apiSign=signService.apiSign(coupon.getCode());
        result.put("jsapiSign",jsapiSign);
        result.put("apiSign",apiSign);
        return result;
    }
}
