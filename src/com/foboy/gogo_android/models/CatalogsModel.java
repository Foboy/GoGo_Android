package com.foboy.gogo_android.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CatalogsModel extends EntityModel {
	private CatalogModel[] data;

	public CatalogModel[] getData() {
		return data;
	}
	@JsonProperty("Data")
	public void setBalance(CatalogModel[] data) {
		this.data = data;
	}
}
