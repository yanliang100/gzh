package com.lafei.aicard.bean;

import java.util.Date;

public class AICard {
    public AICard(){};
    public AICard(String weixinOpenid,String headimageurl,String isJoin,String isPraise){
        this.weixinOpenid=weixinOpenid;
        this.headimageurl=headimageurl;
        this.isJoin=isJoin;
        this.isPraise=isPraise;
        this.joinTime=new Date();
    }
    private Integer id;

    private String weixinOpenid;

    private String headimageurl;

    private String isPraise;

    private String isJoin;

    private Date joinTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWeixinOpenid() {
        return weixinOpenid;
    }

    public void setWeixinOpenid(String weixinOpenid) {
        this.weixinOpenid = weixinOpenid == null ? null : weixinOpenid.trim();
    }

    public String getHeadimageurl() {
        return headimageurl;
    }

    public void setHeadimageurl(String headimageurl) {
        this.headimageurl = headimageurl == null ? null : headimageurl.trim();
    }

    public String getIsPraise() {
        return isPraise;
    }

    public void setIsPraise(String isPraise) {
        this.isPraise = isPraise == null ? null : isPraise.trim();
    }

    public String getIsJoin() {
        return isJoin;
    }

    public void setIsJoin(String isJoin) {
        this.isJoin = isJoin == null ? null : isJoin.trim();
    }

    public Date getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(Date joinTime) {
        this.joinTime = joinTime;
    }
}
