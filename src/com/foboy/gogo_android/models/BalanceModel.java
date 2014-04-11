package com.foboy.gogo_android.models;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BalanceModel {
	private int balance;
	private String name;
	private int cid;
	private float proportion;
	public int getBalance() {
		return balance;
	}
	
	@JsonProperty("balance")
	public void setBalance(int balance) {
		this.balance = balance;
	}
	public String getName() {
		return name;
	}
	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}
	public float getProportion() {
		return proportion;
	}
	@JsonProperty("proportion")
	public void setProportion(float proportion) {
		this.proportion = proportion;
	}

	public int getCid() {
		return cid;
	}
	@JsonProperty("id")
	public void setCid(int cid) {
		this.cid = cid;
	}
	
}
