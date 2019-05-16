package com.lafei.gzh.ad.controller;

import com.lafei.gzh.ad.bean.Ad;
import com.lafei.gzh.ad.service.AdService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class AdController {
    // 注入userService
    @Resource(name = "adService")
    private AdService adService;

    @RequestMapping(value ="/fasola")
    public String fasola(){
        return "fasola";
    }

    @RequestMapping(value ="/yhhd")
    public String yhhd(Model model, HttpServletRequest req){
        try {
            Ad ad=adService.queryYhhd();
            if(ad!=null){
                model.addAttribute("content",ad.getContent());
            }else{
                model.addAttribute("content","暂无最新内容");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "yhhd";
    }
    @RequestMapping(value ="/xptj")
    public String xptj(Model model, HttpServletRequest req){
        try {
            Ad ad=adService.queryXptj();
            if(ad!=null){
                model.addAttribute("content",ad.getContent());
            }else{
                model.addAttribute("content","暂无最新内容");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "xptj";
    }

    @RequestMapping(value ="/spyh")
    public String spyh(Model model, HttpServletRequest req){
        try {
            Ad ad=adService.querySpyh();
            if(ad!=null){
                model.addAttribute("content",ad.getContent());
            }else{
                model.addAttribute("content","暂无最新内容");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "spyh";
    }
}
