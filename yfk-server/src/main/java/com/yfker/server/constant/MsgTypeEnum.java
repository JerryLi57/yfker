package com.yfker.server.constant;

/**
 * @description: Socket消息类型枚举
 * @author: lijiayu
 * @date: 2020-05-13 20:54
 **/
public enum MsgTypeEnum {

    /**
     * 消息类型
     */
    WEB(1, "来源于web端的消息"),
    APP(2, "来源于App端的消息");

    private int code;

    private String msg;

    MsgTypeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
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
}
