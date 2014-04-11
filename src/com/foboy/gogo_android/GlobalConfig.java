package com.foboy.gogo_android;

import java.util.List;

import com.foboy.gogo_android.models.CatalogModel;

import android.app.Activity;

public class GlobalConfig {
    private GlobalConfig() {}
      
    private static GlobalConfig instance=null;
    //静态工厂方法 
    public synchronized  static GlobalConfig getInstance() {
         if (instance == null) {  
        	 instance = new GlobalConfig();
         }  
        return instance;
    }
    
    private MainActivity mainActivity;
    
    private boolean isLogin = false;
    private int shopId;
    private String customPhone;
    private String customName;
    private float amount;
    private float proportion;
    private int customGoCoin;
    private int cusomerId;
    private int payMethod;
    private CatalogModel[] catalogs;
    private List<Integer> catalogIds;
    private String firstCatalogName;
    
	public boolean isLogin() {
		return isLogin;
	}
	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}
	public String getCustomPhone() {
		return customPhone;
	}
	public void setCustomPhone(String customPhone) {
		this.customPhone = customPhone;
	}
	public String getCustomName() {
		return customName;
	}
	public void setCustomName(String customName) {
		this.customName = customName;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public int getCustomGoCoin() {
		return customGoCoin;
	}
	public void setCustomGoCoin(int customGoCoin) {
		this.customGoCoin = customGoCoin;
	}
	public MainActivity getMainActivity() {
		return mainActivity;
	}
	public void setMainActivity(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}
	public float getProportion() {
		return proportion;
	}
	public void setProportion(float proportion) {
		this.proportion = proportion;
	}
	public int getCusomerId() {
		return cusomerId;
	}
	public void setCusomerId(int cusomerId) {
		this.cusomerId = cusomerId;
	}
	public int getShopId() {
		return shopId;
	}
	public void setShopId(int shopId) {
		this.shopId = shopId;
	}
	public CatalogModel[] getCatalogs() {
		return catalogs;
	}
	public void setCatalogs(CatalogModel[] catalogs) {
		this.catalogs = catalogs;
	}
	public List<Integer> getCatalogIds() {
		return catalogIds;
	}
	public void setCatalogIds(List<Integer> catalogIds) {
		this.catalogIds = catalogIds;
	}
	public int getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(int payMethod) {
		this.payMethod = payMethod;
	}
	public String getFirstCatalogName() {
		return firstCatalogName;
	}
	public void setFirstCatalogName(String firstCatalogName) {
		this.firstCatalogName = firstCatalogName;
	}
   
    
    
}
