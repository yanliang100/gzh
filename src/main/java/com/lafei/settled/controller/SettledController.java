package com.lafei.settled.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SettledController {

    /**
     * 代卖服务
     */
    @RequestMapping(value ="/settled_in")
    public String settled_in() throws Exception {

        return "Settled_in";
    }
}
