package com.foboy.gogo_android.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BillListModel extends EntityModel {
	private int pageSize;
	private List<BillModel> data;
	public int getPageSize() {
		return this.pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getRecordCount() {
		return this.data.size();
	}

	public List<BillModel> getData() {
		return data;
	}
	@JsonProperty("Data")
	public void setData(List<BillModel> billList) {
		this.data = billList;
	}
	
}
