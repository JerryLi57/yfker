package com.yfker.common.exception;

import com.yfker.common.constant.ErrorEnum;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @description: 自定义 ServiceException 异常
 * @author: lijiayu
 * @date: 2020-03-11 20:29
 **/
@Component
@Data
public class ServiceException extends RuntimeException {

    private String msg;

    private int code;

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException() {
        super();
    }

    public ServiceException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public ServiceException(ErrorEnum errorEnum) {
        super(errorEnum.getMsg());
        this.code = errorEnum.getCode();
        this.msg = errorEnum.getMsg();
    }

    public ServiceException(ErrorEnum errorEnum, String msg) {
        super(msg);
        this.code = errorEnum.getCode();
        this.msg = msg;
    }
}
