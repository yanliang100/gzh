package com.lafei.gzh.index.controller;

import com.alibaba.fastjson.JSONObject;
import com.lafei.gzh.util.UploadMediaApiUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;

@Controller
public class UploadMediaController {
    @RequestMapping(value ="/UploadMedia")
    private String UploadMedia(){
        UploadMediaApiUtil uploadMediaApiUtil = new UploadMediaApiUtil();
        String appId = "wx172fc37da3d7e8e3";
        String appSecret = "32dd31f6abfe776b910454dac8ae7b23";
        String accessToken = uploadMediaApiUtil.getAccessToken(appId,appSecret);

        String filePath = "C:\\Users\\liang.yan\\Desktop\\数据可视化\\设计.jpg";
        File file = new File(filePath);
        String type = "IMAGE";
        JSONObject jsonObject = uploadMediaApiUtil.uploadMedia(file,accessToken,type);
        System.out.println(jsonObject.toString());
        return "test";
    }
}
