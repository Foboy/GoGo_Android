package com.foboy.gogo_android.common;

public class UrlUtils {
	//:8080/GoGoTown/trunk/crm
	private final static String base = "http://192.168.0.62:81/crm/index.php?url=";
	public final static String Login = base+"user/applogin";
	public final static String GetCatalogs = base+"cash/getcatalogs";
	public final static String GetGoInfo = base+"cash/getgoinfo";
	public final static String SendValidateCode = base+"cash/sendvalidatecode";
	public final static String GoPayValid = base+"cash/gopayvalid";
	public final static String GetBillList = base+"cash/getbills";
}
