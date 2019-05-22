package com.msb.query;

import com.personal.common.base.page.BasePageQueryParam;

import java.math.BigDecimal;
import java.util.Map;

public class DictQueryParam extends BasePageQueryParam {
    private String pid;
    private String[] arrays;
    private int inter;
    private Integer inter2;
    private float float1;
    private BigDecimal bigDecimal;
    private Map<String,String> maps;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String[] getArrays() {
        return arrays;
    }

    public void setArrays(String[] arrays) {
        this.arrays = arrays;
    }

    public int getInter() {
        return inter;
    }

    public void setInter(int inter) {
        this.inter = inter;
    }

    public Integer getInter2() {
        return inter2;
    }

    public void setInter2(Integer inter2) {
        this.inter2 = inter2;
    }

    public float getFloat1() {
        return float1;
    }

    public void setFloat1(float float1) {
        this.float1 = float1;
    }

    public BigDecimal getBigDecimal() {
        return bigDecimal;
    }

    public void setBigDecimal(BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal;
    }

    public Map<String, String> getMaps() {
        return maps;
    }

    public void setMaps(Map<String, String> maps) {
        this.maps = maps;
    }
}
