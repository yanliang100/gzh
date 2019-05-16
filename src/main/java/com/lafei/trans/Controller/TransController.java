package com.lafei.trans.Controller;

import com.lafei.trans.service.BaiduOCRService;
import com.lafei.trans.service.BaiduTransService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
public class TransController {

    // baiduOCRService
    @Resource(name = "baiduOCRService")
    private BaiduOCRService baiduOCRService;

    // baiduTransService
    @Resource(name = "baiduTransService")
    private BaiduTransService baiduTransService;

    @RequestMapping(value = "/trans")
    public String trans(Model model, String barCode) {
        return "trans";
    }

    @RequestMapping(value = "/bce_jpa")
    public String bce_jpa(Model model,String mediaId) {
        try{
            String source=baiduOCRService.getJpaWords(mediaId);
            String transResult = baiduTransService.translate(source);
            model.addAttribute("transResult",transResult);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "bce_jpa";
    }

}
