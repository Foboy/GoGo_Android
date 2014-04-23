package com.foboy.gogo_android;

import java.util.Date;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import com.foboy.gogo_android.common.StringUtils;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foboy.gogo_android.common.ErrorType;
import com.foboy.gogo_android.common.HttpUtils;
import com.foboy.gogo_android.common.UrlUtils;
import com.foboy.gogo_android.models.CatalogsModel;
import com.foboy.gogo_android.models.EntityModel;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.foboy.gogo_android.widget.LoadingMaskView;
import com.foboy.gogo_android.widget.ProgressWheel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	private EditText username;
	private EditText password;
	private LoadingMaskView lmv;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        
        lmv = new LoadingMaskView(LoginActivity.this);
		
        username.setText("shouyin");
        password.setText("111111");
        
        Button btn = (Button)findViewById(R.id.loginbtn);
        
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(!LoginActivity.this.isInputPassed())
					return;
				lmv.show("正在登陆");
				
		        String urlString = UrlUtils.Login;
		        RequestParams params = new RequestParams(); // 绑定参数
		        params.put("user_name", username.getText().toString());
		        params.put("user_password", password.getText().toString());
		        HttpUtils.post(urlString, params, new BaseJsonHttpResponseHandler<CatalogsModel>() {

		        	
		            @Override
		            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, CatalogsModel response) {
		            		if(response.isSuccess())
		            		{
		            			GlobalConfig.getInstance().setLogin(true);
		            			GlobalConfig.getInstance().setShopId(response.getId());
		            			
		            			if(response.getData() == null && response.getData().length == 0)
		            			{
		            				Toast.makeText(LoginActivity.this,  "消费分类为空", Toast.LENGTH_SHORT).show();
		            			}
		            			else
		            			{
		            				Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
		            				GlobalConfig.getInstance().setCatalogs(response.getData());
		            			}
		            			LoginActivity.this.finish();
		            		}
		            		else
		            		{
		            			Toast.makeText(LoginActivity.this,  response.getErrorMessage(), Toast.LENGTH_LONG).show();
		            		}
		            		lmv.hide();

		            }

		            @Override
		            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, CatalogsModel errorResponse) {

		            		Toast.makeText(LoginActivity.this, "网络异常，请检查是否连接到网络", Toast.LENGTH_LONG).show();
		            		lmv.hide();
		            }

		            @Override
		            protected CatalogsModel parseResponse(String rawJsonData) throws Throwable {
		                return new ObjectMapper().readValues(new JsonFactory().createParser(rawJsonData), CatalogsModel.class).next();
		            }

		        });
			}
		});
    }
    
    private boolean isInputPassed()
    {
		String uname = username.getText().toString();
		if(StringUtils.isEmpty(uname)){
			Toast.makeText(LoginActivity.this, "请输入登陆账户", Toast.LENGTH_SHORT).show();
			return false;
		}
		String pw = password.getText().toString();
		if(StringUtils.isEmpty(pw)){
			Toast.makeText(LoginActivity.this, "请输入登陆密码", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
    }
    

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    
    	if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) 
    	{
    		finish();
    		GlobalConfig.getInstance().getMainActivity().finish();
            return false;
        }
    	return false;
    }
}
