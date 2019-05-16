package com.lafei.gzh.bean;

/**
 * 1000:注册成功
 * -1000:注册失败
 * 2000:短信发送成功领券成功
 * -2000:短信发送失败
 * -3000:验证码不正确
 * 4000:已经绑定会员卡
 * -4000:未绑定会员卡
 * 5000:点赞成功
 * -5000:点赞失败
 * 6000:领券成功
 * -6000:领券失败
 * 7000:修改密码成功
 * -7000:修改密码失败
 * 8000:登录成功
 * -8000:登录失败
 */
public class Result {
    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
