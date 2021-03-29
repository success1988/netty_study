package com.rpc.netty.common;

/**
 * @Title：响应码枚举类
 * @Author：wangchenggong
 * @Date 2021/3/29 14:07
 * @Description
 * @Version
 */
public enum CodeMsgEnum {

    SUCCESS(0,"成功"),

    /**
     * 异常  5XXX
     */
    INTERNAL_SERVER_ERROR(5000, "服务处理异常,异常原因:%s"),
    CHANNEL_EXCEPTION(5001, "通道连接异常"),
    ;

    private CodeMsgEnum(int code, String errorMsg){
        this.code = code;
        this.errorMsg = errorMsg;
    }
    private int code;

    private String errorMsg;

    public int getCode(){
        return code;
    }

    public String getErrorMsg(){
        return errorMsg;
    }

    public CodeMsgEnum fillArg(String arg){
        this.errorMsg = String.format(this.errorMsg, arg);
        return this;
    }
}
