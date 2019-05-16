package com.lafei.sms.controller;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.lafei.gzh.bean.Result;
import com.lafei.sms.service.SmsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
public class SmsController {
    // 注入smsService
    @Resource(name = "smsService")
    private SmsService smsService;
    /**
     * 发送验证码
     */
    @RequestMapping(value ="/sendsms")
    @ResponseBody
    public Result sendsms(String mobile, HttpSession session) throws Exception {
        Result result=new Result();
        try {
            ;
            String code = Integer.toString(smsService.birthNewcode());
            SendSmsResponse sendSms =smsService.sendSms(mobile,code);//填写你需要测试的手机号码
            session.setAttribute("SmsCode", code);
        }catch (Exception e){
            e.printStackTrace();
            result.setCode("-2000");
            result.setMessage("短信发送失败");
        }
        result.setCode("2000");
        result.setMessage("短信发送成功");
        return  result;
    }
}
