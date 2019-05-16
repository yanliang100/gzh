package com.lafei.gzh.points.bean;

public class PointsRule {
    private Short id;

    private String name;

    private Integer minAmount;

    private Integer typePoints;

    private String useStartDate;

    private String useEndDate;

    private Byte enabled;

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(Integer minAmount) {
        this.minAmount = minAmount;
    }

    public Integer getTypePoints() {
        return typePoints;
    }

    public void setTypePoints(Integer typePoints) {
        this.typePoints = typePoints;
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

    public Byte getEnabled() {
        return enabled;
    }

    public void setEnabled(Byte enabled) {
        this.enabled = enabled;
    }
}
