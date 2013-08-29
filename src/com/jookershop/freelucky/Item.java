package com.jookershop.freelucky;


public class Item implements java.io.Serializable{
	String id;
	String title;
	String desc;
	int participator;
	int target;
	String imgUrl;
	Long opendate;
	int cid;
	
	
	
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public Long getOpendate() {
		return opendate;
	}
	public void setOpendate(Long opendate) {
		this.opendate = opendate;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getTarget() {
		return target;
	}
	public void setTarget(int target) {
		this.target = target;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public int getFunded() {
		return (participator * 100) / target;
	}
	
	public int getParticipator() {
		return participator;
	}
	public void setParticipator(int participator) {
		this.participator = participator;
	}
}
