package com.yfker.common.constant;

/**
 * @description: ErrorEnum 错误枚举
 * @author: lijiayu
 * @date: 2020-03-11 20:54
 **/
public enum ErrorEnum {

    /**
     * 操作成功
     */
    SUCCESS_CODE(200, "操作成功"),
    SYS_400(400, "操作异常"),
    SYS_401(401, "暂未登录或token已经过期"),
    SYS_402(402, "请求被禁止"),
    SYS_403(403, "对不起! 您没有相关菜单的权限,请联系管理员"),
    SYS_404(404, "服务器找不到请求的网页"),
    SYS_500(500, "服务器内部错误"),
    SYS_503(503, "服务器目前无法使用"),
    SYS_504(504, "网关超时"),
    DATA_NOT_EXISTS(505, "数据不存在或已删除"),

    EVENT_REPORT_IS_COMPLETED(506,"信息接报已处理，不允许操作"),
    EVENT_REPORT_NOT_EXISTS(507,"接报信息不存在"),
    EVENT_NOT_EXISTS(508,"事件不存在"),



    AUTHORIZATION_HEADER_IS_EMPTY(600,"请求头中的token为空"),
    GET_TOKEN_KEY_ERROR(601,"远程获取TokenKey异常"),
    JWT_TOKEN_INVALID(602,"token校验异常"),
    USER_LOGOUT(603,"用户已登出,请重新登录"),
    USER_LOGIN_IN_OTHER_APP(604,"用户已在其它终端登录,如不是您本人登录请即时修改密码重新登录"),
    USER_NOT_LOGIN(605, "没有登录"),
    SYS_605(605,"您已重新切换了部门权限，请刷新当前页面重试"),
    SYS_700(700,"使用Redis缓存服务时，key不能为空"),

    FILE_DATA_NOT_EXISTS(900,"文件信息不存在"),
    DATA_UN_REMOVABLE(901,"记录已关联使用，无法删除"),

    PARAM_ILLEGAL(-10000, "参数非法"),
    SERVICE_ERROR(-10001, "Service服务异常"),
    SERVICE_REPEAT_DATA(-10002, "请勿保存重复数据");

    private int code;

    private String msg;

    ErrorEnum(int code, String msg) {
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
