package com.lafei.gzh.index.controller;
import com.lafei.gzh.index.service.TokenService;
import com.lafei.gzh.util.MessageUtil;
import com.lafei.gzh.menu.service.MenuService;
import com.lafei.gzh.message.service.MessageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class IndexController {
    // 注入messageService
    @Resource(name = "messageService")
    private MessageService messageService;
    // 注入messageService
    @Resource(name = "menuService")
    private MenuService menuService;
    // 注入tokenService
    @Resource(name = "tokenService")
    private TokenService tokenService;

    /**
     * 接收微信服务器发送请求时传递过来的参数
     */
    @RequestMapping(value ="/")
    @ResponseBody
    public String index(String signature, String timestamp, String nonce, String echostr, HttpServletRequest req)  {
        System.out.println("-----开始校验签名-----");
        /**
         * 将token、timestamp、nonce三个参数进行字典序排序
         * 并拼接为一个字符串
         */
        if("".equals(timestamp) || timestamp==null){
            System.out.println("校验签名timestamp异常:"+timestamp);
            return "";
        }
        if("".equals(nonce) || nonce==null){
            System.out.println("校验签名timestamp异常:"+nonce);
            return "";
        }
        String sortStr = tokenService.sort(timestamp,nonce);
        /**
         * 字符串进行shal加密
         */
        String mySignature = tokenService.shal(sortStr);
        /**
         * 校验微信服务器传递过来的签名 和  加密后的字符串是否一致, 若一致则签名通过
         */
        if(!"".equals(signature) && !"".equals(mySignature) && signature.equals(mySignature)){
            System.out.println("-----签名校验通过-----");

            System.out.println("开始构造消息");
            Map<String,String> map = MessageUtil.parseXml(req);
            //如果解析对象为空则为初始化状态，只是初始化菜单并返回echostr
            if(map.size()==0){
                return echostr;
            }
            String msgType = map.get("MsgType");
            //判断请求是否事件类型 event
            if(MessageUtil.REQ_MESSAGE_TYPE_EVENT.equals(msgType)){
                String eventType = map.get("Event");
                //若是关注事件  subscribe
                if(MessageUtil.EVENT_TYPE_SUBSCRIBE.equals(eventType)){
                    String result= messageService.processDefaultRequest(req,map);
                    return result;
                }

                //若是扫描事件 scan
                if(MessageUtil.EVENT_TYPE_SCAN.equals(eventType)){

                }
            }

            //若是文本事件
            if(MessageUtil.REQ_MESSAGE_TYPE_TEXT.equals(msgType)){
                String result= messageService.processRequest(req,map);
                return result;
            }
        }else {
            System.out.println("-----校验签名失败-----");
        }
        return "";

    }

    /**
     * 跳转开业界面
     */
    @RequestMapping(value ="/practice")
    public String practice() throws Exception {
        return "practice";
    }
}
