package com.lafei.gzh.menu.controller;

import com.lafei.gzh.menu.service.MenuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class MenuController {
    // 注入messageService
    @Resource(name = "menuService")
    private MenuService menuService;

    @RequestMapping(value ="/createMenu")
    @ResponseBody
    public String createMenu(){
        return menuService.init();
    }
}
