package com.lafei.user.service;

import com.alibaba.fastjson.JSONObject;
import com.lafei.gzh.bean.AccessToken;
import com.lafei.gzh.bean.Result;
import com.lafei.user.bean.User;
import com.lafei.user.mapper.UserMapper;
import com.lafei.util.IsNullUtil;
import com.lafei.util.NetWorkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Service("userService")
public class UserService{
    @Value("${spring.weixin.appId}")
    private String appId;
    @Value("${spring.weixin.appSecret}")
    private String appSecret;
    @Value("${spring.weixin.get_weixin_user_url}")
    private String get_weixin_user_url;
    @Value("${spring.weixin.access_user_url}")
    private String access_user_url;

    @Resource
    private UserMapper userMapper;

    private static Logger log = LoggerFactory.getLogger(NetWorkUtil.class);
    /**
     * 获取用户信息
     *
     * @param accessToken 接口访问凭证
     * @param openId 用户标识
     * @return UserInfo
     */
    public User getUserInfo(String accessToken, String openId) {
        User user = null;
        String requestUrl = access_user_url.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
        // 获取用户信息
        JSONObject jsonObject = NetWorkUtil.httpsRequest(requestUrl, "GET", null);

        if (null != jsonObject) {
            try {
                user = new User();
                // 用户的标识
                user.setWeixinOpenid(jsonObject.getString("openid"));
                // 关注状态（1是关注，0是未关注），未关注时获取不到其余信息
                user.setSubscribe(jsonObject.getInteger("subscribe"));
                // 用户关注时间
                user.setSubscribetime(jsonObject.getString("subscribe_time"));
                // 昵称
                user.setNickname(jsonObject.getString("nickname"));
                // 用户的性别（1是男性，2是女性，0是未知）
                user.setGender(jsonObject.getInteger("sex"));
                // 用户所在国家
                user.setCountry(jsonObject.getString("country"));
                // 用户所在省份
                user.setProvince(jsonObject.getString("province"));
                // 用户所在城市
                user.setCity(jsonObject.getString("city"));
                // 用户的语言，简体中文为zh_CN
                user.setLanguage(jsonObject.getString("language"));
                // 用户头像
                user.setHeadimgurl(jsonObject.getString("headimgurl"));
            } catch (Exception e) {
                if (0 == user.getSubscribe()) {
                    log.error("用户{}已取消关注", user.getWeixinOpenid());
                } else {
                    int errorCode = jsonObject.getInteger("errcode");
                    String errorMsg = jsonObject.getString("errmsg");
                    log.error("获取用户信息失败 errcode:{} errmsg:{}", errorCode, errorMsg);
                }
            }
        }
        return user;
    }

    /**
     * 获取用户的openId
     * @param code
     * @return 返回封装的微信用户的对象
     */
    public User getTheCode(String code) {
        Map<String , String> authInfo = new HashMap<>();
        String openId = "";
        if (code != null)
        {
            // 调用根据用户的code得到需要的授权信息
            authInfo= oauth2GetOpenid(code);
            //获取到openId
            openId = authInfo.get("Openid");
        }
        // 获取基础刷新的接口访问凭证（目前还没明白为什么用authInfo.get("AccessToken");这里面的access_token就不行）
        //获取到微信用户的信息
        User user = getUserInfo(AccessToken.tokenName,openId);

        return user;
    }

    /**
     * 进行用户授权，获取到需要的授权字段，比如openId
     * @param code 识别得到用户id必须的一个值
     * 得到网页授权凭证和用户id
     * @return
     */
    public Map<String, String> oauth2GetOpenid(String code) {
        //拼接用户授权接口信息
        String requestUrl = get_weixin_user_url.replace("APPID", appId).replace("SECRET", appSecret).replace("CODE", code);
        //存储获取到的授权字段信息
        Map<String, String> result = new HashMap<String, String>();
        try {
            JSONObject OpenidJSONO = NetWorkUtil.httpsRequest(requestUrl,"GET",null);
            //OpenidJSONO可以得到的内容：access_token expires_in  refresh_token openid scope
            String Openid = String.valueOf(OpenidJSONO.get("openid"));
            String AccessToken = String.valueOf(OpenidJSONO.get("access_token"));
            //用户保存的作用域
            String Scope = String.valueOf(OpenidJSONO.get("scope"));
            String refresh_token = String.valueOf(OpenidJSONO.get("refresh_token"));
            result.put("Openid", Openid);
            result.put("AccessToken", AccessToken);
            result.put("scope", Scope);
            result.put("refresh_token", refresh_token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public User getUser(HttpServletRequest req){
        HttpSession session=req.getSession();
        User user=(User) session.getAttribute("currentUser");
        if(IsNullUtil.isNull(user)) {
            String code = req.getParameter("code");
            try {
                //得到当前用户的信息(具体信息就看user这个javabean)
                user = getTheCode(code);
                //将获取到的用户信息，放入到session中
                session.setAttribute("currentUser", user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return user;
    }
    public int update(User user) throws Exception {
       return userMapper.update(user);
    }

    @Transactional
    public Result registeUser(HttpSession session, User user){
        Result result=new Result();
        try{
            userMapper.insert(user);
            session.setAttribute("currentUser", user);
        }catch (Exception e){
            e.printStackTrace();
            result.setCode("-1000");
            result.setMessage("注册失败");
        }
        result.setCode("1000");
        result.setMessage("注册成功");
        return result;

    }

    public User findByWeixinOpenid(String weixinOpenid) throws Exception {
        return userMapper.selectByWeixinOpenid(weixinOpenid);
    }

    @Transactional
    public Result psdPasswd(HttpServletRequest req, User user) throws Exception {
        userMapper.modifyPasswd(user);
        req.getSession().removeAttribute("currentUser");
        Result result=new Result();
        result.setCode("7000");
        result.setMessage("密码修改成功");
        return result;
    }

    public Result loginByMobileAndPasswd(HttpServletRequest req,User user) throws Exception {
        Result result=new Result();
        user=userMapper.selectByMobileAndPasswd(user);
        if(user!=null){
            user.setIsOnline("true");
            userMapper.update(user);
            req.getSession().setAttribute("currentUser", user);
            result.setCode("8000");
            result.setMessage("登录成功");
        }else{
            result.setCode("-8000");
            result.setMessage("手机号或密码错误");
        }
        return result;
    }
}
