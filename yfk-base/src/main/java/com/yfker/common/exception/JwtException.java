package com.yfker.common.exception;

import com.yfker.common.constant.ErrorEnum;

/**
 * @description: JwtException
 * @author: lijiayu
 * @date: 2020-03-18 10:24
 **/
public class JwtException extends RuntimeException {

    private String msg;

    private int code;

    public JwtException(ErrorEnum errorEnum) {
        this.code = errorEnum.getCode();
        this.msg = errorEnum.getMsg();
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
