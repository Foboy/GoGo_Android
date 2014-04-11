package com.foboy.gogo_android.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BillModel {
	
	private int id;
	private String name;
	private String phone;
	private String month;
	private int payMethod;
	private int goCoin;
	private float amount;
	private String date;
	private long createTime;
	private boolean displayMonth;
	
	public String getName() {
		if(name == null)
			return "";
		return name;
	}
	@JsonProperty("username")
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		if(phone == null)
			return "нч";
		return phone;
	}
	@JsonProperty("mobile")
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getPayMethod() {
		return payMethod;
	}
	@JsonProperty("pay_mothed")
	public void setPayMethod(int payMethod) {
		this.payMethod = payMethod;
	}
	public int getGoCoin() {
		return goCoin;
	}
	@JsonProperty("go_coin")
	public void setGoCoin(int goCoin) {
		this.goCoin = goCoin;
	}
	public float getAmount() {
		return amount;
	}
	@JsonProperty("amount")
	public void setAmount(float amount) {
		this.amount = amount;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public long getCreateTime() {
		return createTime;
	}
	@JsonProperty("create_time")
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public boolean isDisplayMonth() {
		return displayMonth;
	}
	public void setDisplayMonth(boolean displayMonth) {
		this.displayMonth = displayMonth;
	}

}
