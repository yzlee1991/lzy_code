package com.entity;

/**
 * author :lzy
 * date   :2018年4月11日下午2:42:41
 */

public class HackFile {

	private String mac;
	
	private String type;
	
	private Long size;
	
	private String name;
	
	private String clientPath;
	
	private String serverPath;
	
	private String data;

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getClientPath() {
		return clientPath;
	}

	public void setClientPath(String clientPath) {
		this.clientPath = clientPath;
	}

	public String getServerPath() {
		return serverPath;
	}

	public void setServerPath(String serverPath) {
		this.serverPath = serverPath;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "HackFile [mac=" + mac + ", type=" + type + ", size=" + size + ", name=" + name + ", clientPath="
				+ clientPath + ", serverPath=" + serverPath + "]";
	}
	
	
	
	
}
