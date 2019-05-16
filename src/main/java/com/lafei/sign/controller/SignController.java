package com.lafei.sign.controller;

import com.alibaba.fastjson.JSONObject;
import com.lafei.sign.service.SignService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;

@Controller
public class SignController {
    // 注入weixinUserService
    @Resource(name = "signService")
    private SignService signService;

    @RequestMapping(value = "/jsapisign")
    @ResponseBody
    public JSONObject jsApiSign(String url) {
        JSONObject result = signService.jsapiSign(url);
        return result;
    }

    @RequestMapping(value = "/apisign")
    @ResponseBody
    public JSONObject apiSign(String card_id) {
        JSONObject result = signService.apiSign(card_id);
        return result;
    }
}
