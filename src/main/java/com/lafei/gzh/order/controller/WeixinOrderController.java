package com.lafei.gzh.order.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WeixinOrderController {

    /**
     * 订单
     */
    @RequestMapping(value ="/orders")
    public String orders() throws Exception {

        return "orders";
    }

    /**
     * 订单评价
     */
    @RequestMapping(value ="/comment")
    public String comment() throws Exception {

        return "comment";
    }

    /**
     * 物流查询
     */
    @RequestMapping(value ="/wuliu_list")
    public String wuliu_list() throws Exception {

        return "wuliu_list";
    }

    /**
     * 物流查询
     */
    @RequestMapping(value ="/wuliu")
    public String wuliu() throws Exception {

        return "wuliu";
    }
}
