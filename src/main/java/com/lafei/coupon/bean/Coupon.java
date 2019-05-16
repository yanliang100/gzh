package com.lafei.coupon.bean;


public class Coupon {
    private Integer id;

    private String name;

    private String code;

    private String typeMoney;

    private String sendType;

    private String goodsType;

    private Integer minAmount;

    private Integer maxAmount;

    private String sendStartDate;

    private String sendEndDate;

    private String useStartDate;

    private String useEndDate;

    private Integer minGoodsAmount;

    private Integer publishAmount;

    private Integer receiveAmount;

    private String createTime;

    private Byte enabled;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getTypeMoney() {
        return typeMoney;
    }

    public void setTypeMoney(String typeMoney) {
        this.typeMoney = typeMoney == null ? null : typeMoney.trim();
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType == null ? null : sendType.trim();
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType == null ? null : goodsType.trim();
    }

    public Integer getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(Integer minAmount) {
        this.minAmount = minAmount;
    }

    public Integer getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Integer maxAmount) {
        this.maxAmount = maxAmount;
    }

    public String getSendStartDate() {
        return sendStartDate;
    }

    public void setSendStartDate(String sendStartDate) {
        this.sendStartDate = sendStartDate == null ? null : sendStartDate.trim();
    }

    public String getSendEndDate() {
        return sendEndDate;
    }

    public void setSendEndDate(String sendEndDate) {
        this.sendEndDate = sendEndDate == null ? null : sendEndDate.trim();
    }

    public String getUseStartDate() {
        return useStartDate;
    }

    public void setUseStartDate(String useStartDate) {
        this.useStartDate = useStartDate == null ? null : useStartDate.trim();
    }

    public String getUseEndDate() {
        return useEndDate;
    }

    public void setUseEndDate(String useEndDate) {
        this.useEndDate = useEndDate == null ? null : useEndDate.trim();
    }

    public Integer getMinGoodsAmount() {
        return minGoodsAmount;
    }

    public void setMinGoodsAmount(Integer minGoodsAmount) {
        this.minGoodsAmount = minGoodsAmount;
    }

    public Integer getPublishAmount() {
        return publishAmount;
    }

    public void setPublishAmount(Integer publishAmount) {
        this.publishAmount = publishAmount;
    }

    public Integer getReceiveAmount() {
        return receiveAmount;
    }

    public void setReceiveAmount(Integer receiveAmount) {
        this.receiveAmount = receiveAmount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Byte getEnabled() {
        return enabled;
    }

    public void setEnabled(Byte enabled) {
        this.enabled = enabled;
    }
}
