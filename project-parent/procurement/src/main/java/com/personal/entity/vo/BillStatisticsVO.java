package com.personal.entity.vo;

/**
 * 账单统计试图对象
 * @author ylw
 * @date 18-7-23 下午2:17
 * @param
 * @return
 */
public class BillStatisticsVO{
    // 间隔时间
    private String time;
    // 总条数（以time为间隔单位）
    private String ct;
    // 总价格（以time为间隔单位）
    private String tp;
    // 实收总价格（以time为间隔单位）
    private String atp;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public String getTp() {
        return tp;
    }

    public void setTp(String tp) {
        this.tp = tp;
    }

    public String getAtp() {
        return atp;
    }

    public void setAtp(String atp) {
        this.atp = atp;
    }
}
