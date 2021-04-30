package com.yfker.common.pojo;

import com.yfker.common.constant.ErrorEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: 通用的返回结果对象
 * @author: lijiayu
 * @date: 2020-03-07 13:34
 **/
@Data
@ApiModel(value = "返回的结果对象", description = "返回结果对象")
public class DataResult<T> {

    @ApiModelProperty(value = "分页对象信息", name = "page")
    private PageDto page;

    @ApiModelProperty(value = "响应数据", name = "data")
    private T data;

    @ApiModelProperty(value = "执行响应码", name = "code")
    private int code;

    /**
     * 返回结果的信息标识
     * 存放错误提示信息等
     */
    private String msg;


    public DataResult() {
        this.code = ErrorEnum.SUCCESS_CODE.getCode();
        this.msg = ErrorEnum.SUCCESS_CODE.getMsg();
    }

    public DataResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public DataResult(ErrorEnum errorEnum) {
        this.code = errorEnum.getCode();
        this.msg = errorEnum.getMsg();
    }

    public DataResult(ErrorEnum errorEnum, String msg) {
        this.code = errorEnum.getCode();
        this.msg = msg;
    }

    public static DataResult getErrorInstance() {
        DataResult result = new DataResult();
        result.code = ErrorEnum.SYS_500.getCode();
        result.msg = ErrorEnum.SYS_500.getMsg();
        return result;
    }
}
