package com.lafei.gzh.price.controller;

import com.lafei.gzh.goods.service.GoodsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.Map;

@Controller
public class PriceController {

    // goodsService
    @Resource(name = "goodsService")
    private GoodsService goodsService;

    /**
     * 扫一扫询价
     * @return
     */
    @RequestMapping(value = "/price")
    public String price() {
        return "price";
    }

    @RequestMapping(value = "/ask_price")
    public String ask_price(Model model, @RequestParam String barCode) {
        try {
            Map<String,Object> goods=goodsService.queryByBarCode(barCode);
            model.addAttribute("goods",goods);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ask_price";
    }
}
