package com.foboy.gogo_android.models;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.foboy.gogo_android.common.ErrorType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityModel {
	private int id;
	private int errorCode;
	private String errorMsg;
	private String errorMessage;
	
	public int getId() {
		return id;
	}
	@JsonProperty("Id")
	public void setId(int id) {
		this.id = id;
	}
	@JsonProperty("Error")
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	@JsonProperty("ExMessage")
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	@JsonProperty("ErrorMessage")
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public boolean isLogin()
	{
		return this.errorCode != ErrorType.UnLogin;
	}
	
	public boolean isSuccess()
	{
		return this.errorCode == ErrorType.Success;
	}
	
}
