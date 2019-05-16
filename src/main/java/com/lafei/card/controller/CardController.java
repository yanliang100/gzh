package com.lafei.card.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lafei.card.service.CardService;
import com.lafei.gzh.bean.Result;
import com.lafei.sign.service.SignService;
import com.lafei.user.bean.User;
import com.lafei.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class CardController {
    // 注入UserService
    @Resource(name = "userService")
    private UserService userService;
    // 注入UserService
    @Resource(name = "cardService")
    private CardService cardService;
    // 注入signService
    @Resource(name = "signService")
    private SignService signService;

    @RequestMapping(value = "/get_hy_card")
    @ResponseBody
    public Map<String,Object> get_hy_card(String url) {
        Map<String,Object> result=new HashMap<String,Object>();
        JSONObject jsapiSign=signService.jsapiSign(url);
        String card_id=signService.getHuiyuanCardId();
        JSONObject apiSign=signService.apiSign(card_id);
        result.put("jsapiSign",jsapiSign);
        result.put("apiSign",apiSign);
        return result;
    }
    @RequestMapping(value = "/exitCard")
    @ResponseBody
    public Result exitCard(HttpServletRequest req) {
        Result result=new Result();
        User user = userService.getUser(req);
        JSONObject jsonObject = cardService.getCardList(user.getWeixinOpenid());
        JSONArray card_list=(JSONArray)jsonObject.get("card_list");
        if(card_list.size()>0){
            Map<String,String> card=(Map<String,String>)card_list.get(0);
            result.setCode("4000");
            result.setMessage("已经绑定会员卡");
       }else{
            JSONObject sign=signService.apiSign(null);
            result.setCode("-4000");
            result.setMessage(JSON.toJSONString(sign));
        }
        return result;
    }
}
