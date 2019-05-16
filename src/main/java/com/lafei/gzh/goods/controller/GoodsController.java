package com.lafei.gzh.goods.controller;

import com.lafei.gzh.goods.bean.Goods;
import com.lafei.gzh.goods.service.GoodsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;

@Controller
public class GoodsController {
    // 注入signService
    @Resource(name = "goodsService")
    private GoodsService goodsService;

    @RequestMapping(value ="/get_brief_good")
    @ResponseBody
    public Goods get_brief_good(String barCode) throws Exception {
        Goods good=goodsService.queryBriefGoodByCode(barCode);
        return good;
    }
}
