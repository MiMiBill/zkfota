package com.zkys.pad.fota.http;

/**
 * Created by Administrator on 2016/12/24 0024.
 */

public class SimpleResponse {
    private int code;
    private String msg;
    private int total;

    public BaseBean toBaseBean(){
        BaseBean baseBean=new BaseBean();
        baseBean.setCode(code);
        baseBean.setMsg(msg);
        baseBean.setTotal(total);
        return baseBean;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
