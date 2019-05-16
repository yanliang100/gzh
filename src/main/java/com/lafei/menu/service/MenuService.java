package com.lafei.menu.service;

import com.alibaba.fastjson.JSONObject;
import com.lafei.gzh.bean.AccessToken;
import com.lafei.menu.bean.*;
import com.lafei.util.NetWorkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("menuService")
public class MenuService {
    // 第三方用户唯一凭证
    @Value("${spring.weixin.appId}")
    private String appId;
    // 密令appSecret
    @Value("${spring.weixin.appSecret}")
    private String appSecret;
    //domain
    @Value("${spring.weixin.domain}")
    private String domain;
    //menu_create_url
    @Value("${spring.weixin.menu_create_url}")
    private String menu_create_url;

    // 小程序唯一凭证
    @Value("${spring.weixin.mimiprogram_appid}")
    private String mimiprogram_appid;
    // 小程序唯一凭证
    @Value("${spring.weixin.mimiprogram_domain}")
    private String mimiprogram_domain;

    private static Logger log = LoggerFactory.getLogger(MenuService.class);

    public String init() {
        // 调用接口创建菜单
        Menu menu=getMenu(appId,domain);
        // 将菜单对象转换成json字符串
        String jsonMenu = JSONObject.toJSONString(menu);
        int result = createMenu(jsonMenu, AccessToken.tokenName,menu_create_url);
        // 判断菜单创建结果
        if (0 == result) {
            return jsonMenu;
        }
        return null;
    }

    /**
     * 组装菜单数据
     *
     * @return
     */
    private Menu getMenu(String appId,String domain) {


        ViewButton button11 = new ViewButton();
        button11.setName("一键购物");
        button11.setType("view");
        button11.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId+"&redirect_uri="+domain+"/pay&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect");

        ViewButton button12 = new ViewButton();
        button12.setName("新品推荐");
        button12.setType("view");
        button12.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId+"&redirect_uri="+domain+"/xptj&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect");
        //button12.setUrl("https://mp.weixin.qq.com/s/1hx-wSki_6phLGG-brhr4Q");

        ViewButton button13 = new ViewButton();
        button13.setName("商品优惠");
        button13.setType("view");
        button13.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId+"&redirect_uri="+domain+"/spyh&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect");
        //button13.setUrl("https://www.fasola.jp/zh-CN/special/souvenir/index.html");

        MiniProgramButton button14=new MiniProgramButton();
        button14.setName("微信商城");
        button14.setType("miniprogram");
        button14.setAppid(mimiprogram_appid);
        button14.setUrl(mimiprogram_domain);
        button14.setPagepath("pages/index/index");

        //一级下有3个子菜单
        ComplexButton mainBtn1 = new ComplexButton();
        mainBtn1.setName("FaSoLa");
        mainBtn1.setSub_button(new Button[] { button11, button12, button13, button14 });

        ViewButton button21 = new ViewButton();
        button21.setName("我的会员卡");
        button21.setType("view");
        button21.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId+"&redirect_uri="+domain+"/membercard&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect");

        ViewButton button22 = new ViewButton();
        button22.setName("领取优惠券");
        button22.setType("view");
        button22.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId+"&redirect_uri="+domain+"/coupon&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect");

        ViewButton button23 = new ViewButton();
        button23.setName("扫一扫查询");
        button23.setType("view");
        button23.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId+"&redirect_uri="+domain+"/price&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect");

        ViewButton button24 = new ViewButton();
        button24.setName("拍一拍翻译");
        button24.setType("view");
        button24.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId+"&redirect_uri="+domain+"/trans&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect");
        //包裹查询
        //button24.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId+"&redirect_uri="+domain+"/wuliu_list&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect");


        //一级下有4个子菜单
        ComplexButton mainBtn2 = new ComplexButton();
        mainBtn2.setName("会员服务");
        mainBtn2.setSub_button(new Button[] { button21, button22, button23, button24 });

        ViewButton button31 = new ViewButton();
        button31.setName("招贤纳士");
        button31.setType("view");
        button31.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId+"&redirect_uri="+domain+"/zhaopin&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect");

        ViewButton button32 = new ViewButton();
        button32.setName("AI智能名片");
        button32.setType("view");
        button32.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId+"&redirect_uri="+domain+"/aicard&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect");


        ViewButton button33 = new ViewButton();
        button33.setName("代卖服务");
        button33.setType("view");
        button33.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId+"&redirect_uri="+domain+"/settled_in&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect");

        /*ViewButton button34 = new ViewButton();
        button34.setName("创建菜单");
        button34.setType("view");
        button34.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId+"&redirect_uri="+domain+"/createMenu&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect");*/

        ComplexButton mainBtn3 = new ComplexButton();
        mainBtn3.setName("联系我们");
        mainBtn3.setSub_button(new Button[] { button31, button32, button33 });

        /**
         * 封装整个菜单
         */
        Menu menu = new Menu();
        menu.setButton(new Button[] { mainBtn1, mainBtn2, mainBtn3 });
        return menu;
    }

    /**
     * 创建菜单
     *
     * @param jsonMenu 菜单
     * @param accessToken 有效的access_token
     * @return 0表示成功，其他值表示失败
     */
    public int createMenu(String jsonMenu, String accessToken,String menu_create_url) {
        int result = 0;
        // 拼装创建菜单的url
        String url = menu_create_url.replace("ACCESS_TOKEN", accessToken);

        // 调用接口创建菜单
        JSONObject jsonObject = NetWorkUtil.httpsRequest(url, "POST", jsonMenu);
        if (null != jsonObject) {
            if (0 != jsonObject.getInteger("errcode")) {
                result = jsonObject.getInteger("errcode");
                log.error("创建菜单失败 errcode:{} errmsg:{}", jsonObject.getInteger("errcode"), jsonObject.getString("errmsg"));
            }else{
                log.info("创建菜单成功");
            }
        }
        return result;
    }
}
