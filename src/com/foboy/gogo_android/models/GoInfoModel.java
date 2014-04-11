package com.foboy.gogo_android.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GoInfoModel extends EntityModel {
	private BalanceModel data;

	public BalanceModel getData() {
		return data;
	}
	@JsonProperty("Data")
	public void setBalance(BalanceModel data) {
		this.data = data;
	}
	
}
