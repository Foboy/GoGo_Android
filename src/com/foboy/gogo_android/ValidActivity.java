package com.foboy.gogo_android;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foboy.gogo_android.common.HttpUtils;
import com.foboy.gogo_android.common.StringUtils;
import com.foboy.gogo_android.common.UrlUtils;
import com.foboy.gogo_android.models.EntityModel;
import com.foboy.gogo_android.models.GoInfoModel;
import com.foboy.gogo_android.widget.LoadingMaskView;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class ValidActivity extends Activity {
	private Button resendbtn;
	private EditText validationcode;
	private CountDownTimer cdt;
	private LoadingMaskView lmv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valid);
        
        lmv = new LoadingMaskView(ValidActivity.this);
        
        validationcode = (EditText)findViewById(R.id.validationcodeet);
        Button confirmbtn = (Button)findViewById(R.id.payconfirmbtn);
        confirmbtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				payCommit();
			}
		});
        resendbtn = (Button)findViewById(R.id.resendbtn);
        resendbtn.setVisibility(View.VISIBLE);
        resendbtn.setEnabled(true);
        resendbtn.setText("重发");
        resendbtn.setTextColor(getResources().getColor(R.color.White));
        resendbtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				resendbtn.setEnabled(false);
				resendbtn.setTextColor(getResources().getColor(R.color.FontGray));
				
		        String urlString = UrlUtils.SendValidateCode;
		        RequestParams params = new RequestParams(); // 绑定参数
		        params.put("customer_id", GlobalConfig.getInstance().getCusomerId()+"");
		        HttpUtils.post(urlString, params, new BaseJsonHttpResponseHandler<EntityModel>() {
		            @Override
		            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, EntityModel response) {
		            		if(response.isSuccess())
		            		{
		            			Toast.makeText(ValidActivity.this,  "重发成功", Toast.LENGTH_SHORT).show();
		            		}
		            		else
		            		{
		            			Toast.makeText(ValidActivity.this,  response.getErrorMessage(), Toast.LENGTH_LONG).show();
		            		}
		            }

		            @Override
		            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, EntityModel errorResponse) {

		            		Toast.makeText(ValidActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
		            }

		            @Override
		            protected EntityModel parseResponse(String rawJsonData) throws Throwable {
		                return new ObjectMapper().readValues(new JsonFactory().createParser(rawJsonData), EntityModel.class).next();
		            }

		        });
				// TODO Auto-generated method stub
				cdt = new CountDownTimer(60*1000,1000){ 
					@Override 
					public void onFinish() { 
						resendbtn.setEnabled(true);
						resendbtn.setText("重发");
						resendbtn.setTextColor(getResources().getColor(R.color.White));
					} 

					@Override 
					public void onTick(long arg0) { 
					//每1000毫秒回调的方法 
						resendbtn.setText(arg0/1000 +"秒");
					} 

				}.start();
			}
		});
        
        ImageButton topbackbtn = (ImageButton)findViewById(R.id.topbackbtn);
        topbackbtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//Intent it = new Intent(ValidActivity.this,PayMethodActivity.class);
				//it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				//startActivity(it);
				ValidActivity.this.finish();
			}
		});
    }

    private void payCommit()
    {
		if(!isInputPassed())
			return;
		
        String urlString = UrlUtils.GoPayValid;
        RequestParams params = new RequestParams(); // 绑定参数
        params.put("customer_id", GlobalConfig.getInstance().getCusomerId()+"");
        params.put("amount", GlobalConfig.getInstance().getAmount()+"");
        params.put("code", validationcode.getText().toString());
        params.put("shop_id", GlobalConfig.getInstance().getShopId()+"");
        try {
			params.put("type_ids", new ObjectMapper().writeValueAsString(GlobalConfig.getInstance().getCatalogIds()));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        HttpUtils.post(urlString, params, new BaseJsonHttpResponseHandler<GoInfoModel>() {

        	
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, GoInfoModel response) {
            		if(response.isLogin() && response.isSuccess())
            		{

        				Intent it = new Intent(ValidActivity.this,SuccessActivity.class);
        				startActivity(it);
            		}
            		else
            		{
            			Toast.makeText(ValidActivity.this,  response.getErrorMessage(), Toast.LENGTH_LONG).show();
            		}
            		lmv.hide();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, GoInfoModel errorResponse) {

            		Toast.makeText(ValidActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
            		lmv.hide();
            }

            @Override
            protected GoInfoModel parseResponse(String rawJsonData) throws Throwable {
                return new ObjectMapper().readValues(new JsonFactory().createParser(rawJsonData), GoInfoModel.class).next();
            }

        });
    }
    
    private boolean isInputPassed()
    {
		String code = validationcode.getText().toString();
		if(StringUtils.isEmpty(code)){
			Toast.makeText(ValidActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		return true;
    }
}
