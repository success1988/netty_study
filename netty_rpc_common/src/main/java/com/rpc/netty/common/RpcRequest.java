package com.rpc.netty.common;

public class RpcRequest {

	/**
	 * 请求id
	 */
	private String id;
	/**
	 * 类名
	 */
	private String className;
	/**
	 * 函数名称
	 */
	private String methodName;
	/**
	 * 参数类型
	 */
	private Class<?>[] parameterTypes;
	/**
	 * 参数列表
	 */
	private Object[] parameters;


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}
	public void setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}
	public Object[] getParameters() {
		return parameters;
	}
	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}
	
}
