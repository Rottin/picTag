package com.rottin.administrator.pictag.vo;

import java.io.Serializable;

@SuppressWarnings("serial")
public class GoodsVO implements Serializable{
	private String id;
	private int image;
	private String name;
	private String price;
	private String info;
	private String parentType;
	private String childType;
	private String images;
	public GoodsVO(String id, String images, String name, String price, String info,
			String parentType, String childType) {
		super();
		this.id = id;
		this.images = images;
		this.name = name;
		this.price = price;
		this.info = info;
		this.parentType = parentType;
		this.childType = childType;
	}
	public GoodsVO(String id, int image, String name, String price, String info,
			String parentType, String childType) {
		super();
		this.id = id;
		this.image = image;
		this.name = name;
		this.price = price;
		this.info = info;
		this.parentType = parentType;
		this.childType = childType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getImage() {
		return image;
	}
	public void setImage(int image) {
		this.image = image;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getParentType() {
		return parentType;
	}
	public void setParentType(String parentType) {
		this.parentType = parentType;
	}
	public String getChildType() {
		return childType;
	}
	public void setChildType(String childType) {
		this.childType = childType;
	}
	public String getImages() {
		return images;
	}
	public void setImages(String images) {
		this.images = images;
	}
}
