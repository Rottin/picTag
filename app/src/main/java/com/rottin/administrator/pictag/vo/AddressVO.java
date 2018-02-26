package com.rottin.administrator.pictag.vo;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AddressVO implements Serializable{
	private String name;
	private String tel;
	private String address;
	private boolean isDefault;
	public AddressVO(String name, String tel, String address , boolean isDefault) {
		super();
		this.name = name;
		this.tel = tel;
		this.address = address;
		this.setDefault(isDefault);
	}
	public AddressVO(String name, String tel, String address ) {
		// TODO Auto-generated constructor stub
		super();
		this.name = name;
		this.tel = tel;
		this.address = address;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public boolean isDefault() {
		return isDefault;
	}
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
	@Override
	public String toString() {
		return "AddressVO [name=" + name + ", tel=" + tel + ", address="
				+ address + ", isDefault=" + isDefault + "]";
	}
	
}
