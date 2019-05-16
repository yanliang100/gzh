package com.lafei.gzh.user.controller;

import com.lafei.gzh.coupon.service.CouponService;
import com.lafei.gzh.index.bean.Result;
import com.lafei.gzh.user.bean.User;
import com.lafei.gzh.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class UserController {

    // 注入userService
    @Resource(name = "userService")
    private UserService userService;
    //注入couponService
    @Resource(name = "couponService")
    private CouponService couponService;

    /**
     * 会员注册
     */
    @RequestMapping(value ="/registe")
    @ResponseBody
    public Result registe(String mobile, String passwd, String telcx, HttpServletRequest req){
        Result result=new Result();
        HttpSession session=req.getSession();
        String smsCode = (String) session.getAttribute("SmsCode");
        if(smsCode.equals(telcx)){
            User currentUser=userService.getUser(req);
            currentUser.setMobile(mobile);
            currentUser.setPassword(passwd);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
            String registerTime = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
            currentUser.setRegisterTime(registerTime);
            currentUser.setRegistSource("index");
            currentUser.setRewardPoints(100);//注册会员,赠送100积分
            //入库操作...
            result= userService.registeUser(session,currentUser);
            session.removeAttribute("SmsCode");
        }else{
            result.setCode("-3000");
            result.setMessage("验证码不正确");
        }
        return result;
    }

    /**
     * 我的会员卡
     * @param req
     * @param model
     * 如果不存在用户则进入注册界面
     * 如果存在用户：
     * 在线状态：进入会员卡界面
     * 离线状态：进入登陆界面
     * @return
     */
    @RequestMapping(value ="/membercard")
    public String personal(HttpServletRequest req, Model model){
        String url="";
        try{
            User currentUser=userService.getUser(req);
            String weixinOpenid=currentUser.getWeixinOpenid();
            User weixinUser=userService.findByWeixinOpenid(weixinOpenid);
            if(weixinUser!=null){
                boolean isOnline=Boolean.parseBoolean(weixinUser.getIsOnline());
                if(isOnline){
                    model.addAttribute("weixinUser", weixinUser);
                    int couponSize=couponService.queryCouponSize(null,weixinOpenid);
                    model.addAttribute("couponSize", couponSize);
                    url= "membercard";
                }else{
                    url= "login";
                }
            }else{
                url="regist";
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return url;
    }

    /**
     * 注册协议
     */
    @RequestMapping(value ="/xieyi")
    public String xieyi(){
        return "xieyi";
    }

    /**
     * 会员登录界面
     */
    @RequestMapping(value ="/login")
    public String login(){

        return "login";
    }

    /**
     * 会员登录
     */
    @RequestMapping(value ="/do_login")
    @ResponseBody
    public Result do_login(HttpServletRequest req,@RequestParam String mobile,@RequestParam String passwd){
        User user=new User();
        user.setMobile(mobile);
        user.setPassword(passwd);
        user.setIsOnline("true");
        try {
            return userService.loginByMobileAndPasswd(req,user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 会员登出
     */
    @RequestMapping(value ="/logout")
    public String logout(HttpServletRequest req){
        User currentUser=userService.getUser(req);
        String weixinOpenid=currentUser.getWeixinOpenid();
        try {
            User user=userService.findByWeixinOpenid(weixinOpenid);
            user.setIsOnline("false");
            user.setRegistSource("index");
            userService.update(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "login";
    }



    /**
     * 我的收藏
     */
    @RequestMapping(value ="/shoucang")
    public String shoucang() {

        return "shoucang";
    }

    /**
     * 地址列表
     */
    @RequestMapping(value ="/address_list")
    public String address_list(){

        return "address_list";
    }

    /**
     * 添加地址
     */
    @RequestMapping(value ="/address_edit")
    public String address_edit(){
        return "address_edit";
    }

    /**
     * 密码修改列表
     */
    @RequestMapping(value ="/password")
    public String password(){

        return "password";
    }

    /**
     * 密码修改
     */
    @RequestMapping(value ="/psd_change")
    public String psd_change() throws Exception {

        return "psd_change";
    }

    /**
     * 密码修改
     * 应该做安全性验证再修改密码
     */
    @RequestMapping(value ="/do_psd_change")
    @ResponseBody
    public Result do_psd_change(HttpServletRequest req,@RequestParam String passwd) throws Exception {
        User currentUser=userService.getUser(req);
        currentUser.setPassword(passwd);
        return userService.psdPasswd(req,currentUser);
    }

}
