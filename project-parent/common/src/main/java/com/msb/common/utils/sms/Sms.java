package com.msb.common.utils.sms;

import java.util.List;

public class Sms {
    private String RetTime;
    private int RetCode;
    private List<Msg> Rets;

    public String getRetTime() {
        return RetTime;
    }

    public void setRetTime(String retTime) {
        RetTime = retTime;
    }

    public int getRetCode() {
        return RetCode;
    }

    public void setRetCode(int retCode) {
        RetCode = retCode;
    }

    public List<Msg> getRets() {
        return Rets;
    }

    public void setRets(List<Msg> rets) {
        Rets = rets;
    }
}
