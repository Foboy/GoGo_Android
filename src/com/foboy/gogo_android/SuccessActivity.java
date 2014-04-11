package com.foboy.gogo_android;

import java.util.Date;

import com.foboy.gogo_android.common.PayMethodType;
import com.foboy.gogo_android.common.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SuccessActivity extends Activity {
	
	private TextView customerName;
	private TextView catalogName;
	private TextView amount;
	private TextView paymethodName;
	private TextView payDate;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        
        customerName = (TextView)findViewById(R.id.successnametv);
        catalogName = (TextView)findViewById(R.id.successcattv);
        amount = (TextView)findViewById(R.id.successamounttv);
        paymethodName = (TextView)findViewById(R.id.successpaymethodtv);
        payDate = (TextView)findViewById(R.id.successdatetv);
        
        GlobalConfig config = GlobalConfig.getInstance();
        customerName.setText(config.getCustomName());
        amount.setText(config.getAmount() +"元");
        if(config.getPayMethod() == PayMethodType.GoGoPay)
        {
        	paymethodName.setText("GO币支付");
        }
        else
        {
        	paymethodName.setText("现金支付");
        }
        catalogName.setText(config.getFirstCatalogName());
        payDate.setText(StringUtils.dateToString(new Date()));
        
        Button backbtn = (Button)findViewById(R.id.backtohome);
        backbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it = new Intent(SuccessActivity.this,MainActivity.class);
				it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(it);
			}
		});
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    
    	if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) 
    	{
			Intent it = new Intent(SuccessActivity.this,MainActivity.class);
			it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(it);
            return false;
        }
    	return false;
    }

}
