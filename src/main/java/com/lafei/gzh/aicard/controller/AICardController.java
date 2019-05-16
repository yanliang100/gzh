package com.lafei.gzh.aicard.controller;

import com.lafei.gzh.aicard.bean.AICard;
import com.lafei.gzh.aicard.service.AICardService;
import com.lafei.gzh.index.bean.Result;
import com.lafei.gzh.user.bean.User;
import com.lafei.gzh.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class AICardController {
    // 注入userService
    @Resource(name = "userService")
    private UserService userService;
    // 注入aiCardService
    @Resource(name = "aiCardService")
    private AICardService aiCardService;

    @RequestMapping(value ="/aicard")
    public String aicard(Model model,HttpServletRequest req){
        try{
            User currentUser=userService.getUser(req);
            if(!aiCardService.isJoinedAICard(currentUser)){
                aiCardService.joinAICard(currentUser);
            }
            AICard myaicard = aiCardService.findMyCard(currentUser);
            int cardCount=aiCardService.findAIUserCount();
            int praiseCount=aiCardService.findPraiseCount();
            List<AICard> aiCards=aiCardService.findAIUsers(0,5);
            model.addAttribute("myaicard",myaicard);
            model.addAttribute("cardCount",cardCount);
            model.addAttribute("praiseCount",praiseCount);
            model.addAttribute("aiCards",aiCards);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "aicard";
    }

    @RequestMapping(value ="/praise")
    @ResponseBody
    public Result praise(HttpServletRequest req){
        Result result=new Result();
        try{
            User currentUser=userService.getUser(req);
            result.setCode("5000");
            int state=aiCardService.praiseAICard(currentUser);
            result.setMessage(state==1?"点赞成功":"点赞失败");
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
