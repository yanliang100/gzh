package com.lafei.gzh.zhaopin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ZhaopinController {

    @RequestMapping(value ="/zhaopin")
    public String zhaopin() throws Exception {
        return "zhaopin";
    }
}
