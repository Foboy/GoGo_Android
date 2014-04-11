package com.foboy.gogo_android;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foboy.gogo_android.common.HttpUtils;
import com.foboy.gogo_android.common.PayMethodType;
import com.foboy.gogo_android.common.UrlUtils;
import com.foboy.gogo_android.models.EntityModel;
import com.foboy.gogo_android.widget.LoadingMaskView;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class PayMethodActivity extends Activity {
	
	private TextView customerName;
	private TextView goCoin;
	private TextView amount;
	private LoadingMaskView lmv;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymethod);
        TextView title = (TextView) findViewById(R.id.frame_main_header_title);
        title.setText("支付清单");
        
        customerName = (TextView)findViewById(R.id.customernametv);
        goCoin = (TextView)findViewById(R.id.gocointv);
        amount = (TextView)findViewById(R.id.amounttv);
        
        lmv = new LoadingMaskView(PayMethodActivity.this);
        
        Button gogopaybtn = (Button)findViewById(R.id.gogopaybtn);
        
        GlobalConfig config = GlobalConfig.getInstance();
        customerName.setText(config.getCustomName());
        goCoin.setText(config.getCustomGoCoin() + "");
        amount.setText(config.getAmount() +"元");
        
        //GO币折现金额 不够
        if(config.getCustomGoCoin()*config.getProportion() < config.getAmount())
        {
        	goCoin.setTextColor(getResources().getColor( R.color.Red));
        	gogopaybtn.setTextColor(getResources().getColor( R.color.Gray));
        	gogopaybtn.setEnabled(false);
        }
        

        gogopaybtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				GlobalConfig.getInstance().setPayMethod(PayMethodType.GoGoPay);
				
				lmv.show();
				
		        String urlString = UrlUtils.SendValidateCode;
		        RequestParams params = new RequestParams(); // 绑定参数
		        params.put("customer_id", GlobalConfig.getInstance().getCusomerId()+"");
		        HttpUtils.post(urlString, params, new BaseJsonHttpResponseHandler<EntityModel>() {
		            @Override
		            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, EntityModel response) {
		            		if(response.isSuccess())
		            		{
		        				Intent it = new Intent(PayMethodActivity.this,ValidActivity.class);
		        				startActivity(it);
		            		}
		            		else
		            		{
		            			Toast.makeText(PayMethodActivity.this,  response.getErrorMessage(), Toast.LENGTH_LONG).show();
		            		}
		            		lmv.hide();
		            }

		            @Override
		            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, EntityModel errorResponse) {

		            		Toast.makeText(PayMethodActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
		            		lmv.hide();
		            }

		            @Override
		            protected EntityModel parseResponse(String rawJsonData) throws Throwable {
		                return new ObjectMapper().readValues(new JsonFactory().createParser(rawJsonData), EntityModel.class).next();
		            }

		        });

			}
		});

        ImageButton topbackbtn = (ImageButton)findViewById(R.id.topbackbtn);
        topbackbtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//Intent it = new Intent(PayMethodActivity.this,MainActivity.class);
				//it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				//startActivity(it);
				PayMethodActivity.this.finish();
			}
		});
    }
}
