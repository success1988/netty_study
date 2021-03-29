package com.rpc.netty.common;

public class RpcResponse {
	/**
	 * 请求id
	 */
	private String requestId;
	/**
	 * rpc调用失败的状态码
	 */
	private int code;
	/**
	 * rpc调用失败的原因
	 */
	private String errorMsg;
	/**
	 * 响应数据
	 */
	private Object data;

	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}

	public void setCodeMsg(CodeMsgEnum codeMsg){
		this.code = codeMsg.getCode();
		this.errorMsg = codeMsg.getErrorMsg();
	}
}
