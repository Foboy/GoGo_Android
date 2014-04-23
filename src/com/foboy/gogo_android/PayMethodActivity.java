package com.foboy.gogo_android;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foboy.gogo_android.adapter.PayMethodGridAdapter;
import com.foboy.gogo_android.common.HttpUtils;
import com.foboy.gogo_android.common.PayMethodType;
import com.foboy.gogo_android.common.StringUtils;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class PayMethodActivity extends Activity {
	
	private TextView customerName;
	private TextView goCoin;
	private TextView amount;
	private CheckBox gopaycb;
	private Button gogopaybtn;
	private GridView payGrid;
	private LoadingMaskView lmv;
	
	private PayMethodGridAdapter adapter;
	private ArrayList<HashMap<String, Object>> lstInfoItem = new ArrayList<HashMap<String, Object>>();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymethod);
        TextView title = (TextView) findViewById(R.id.frame_main_header_title);
        title.setText("支付清单");
        
        customerName = (TextView)findViewById(R.id.customernametv);
        goCoin = (TextView)findViewById(R.id.gocointv);
        amount = (TextView)findViewById(R.id.amounttv);
        gopaycb = (CheckBox)findViewById(R.id.useGocheckBox);
        payGrid = (GridView)findViewById(R.id.payInfoGridView);
        
        lmv = new LoadingMaskView(PayMethodActivity.this);
        
        gogopaybtn = (Button)findViewById(R.id.gogopaybtn);
        
        GlobalConfig config = GlobalConfig.getInstance();
        customerName.setText(config.getCustomName());
        goCoin.setText(config.getCustomGoCoin()+"");
        amount.setText(config.getAmount() +"元");
        
        adapter = new PayMethodGridAdapter(this, lstInfoItem,R.layout.item_pay_griditem);
        payGrid.setAdapter(adapter);
        
        initGrid(gopaycb.isChecked());
        

        gogopaybtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				GlobalConfig config =GlobalConfig.getInstance();
				if(gopaycb.isChecked() && config.getCustomGoCoin() >0)
				{
					GlobalConfig.getInstance().setPayMethod(PayMethodType.GoGoPay);
					
					lmv.show("提交订单");
					
			        String urlString = UrlUtils.SendValidateCode;
			        RequestParams params = new RequestParams(); // 绑定参数
			        params.put("phone", GlobalConfig.getInstance().getCustomPhone());
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
				else
				{
					
				}
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
        
        gopaycb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				initGrid(arg1);
			}
        	
        });
    }
    
    private void initGrid(boolean gopay)
    {
    	GlobalConfig config = GlobalConfig.getInstance();
    	
    	this.lstInfoItem.clear();
    	
    	HashMap<String, Object> amountTitle = new HashMap<String, Object>();  
    	amountTitle.put("content", "应付款");
    	amountTitle.put("color", getResources().getColor(R.color.Black));
    	lstInfoItem.add(amountTitle);  
    	
    	HashMap<String, Object> goCashTitle = new HashMap<String, Object>();  
    	goCashTitle.put("content", "GO币抵现");
    	goCashTitle.put("color", getResources().getColor(R.color.Black));
    	lstInfoItem.add(goCashTitle); 
    	
    	HashMap<String, Object> payTitle = new HashMap<String, Object>();  
    	payTitle.put("content", "实付款");
    	payTitle.put("color", getResources().getColor(R.color.Black));
    	lstInfoItem.add(payTitle); 

        HashMap<String, Object> amountCell = new HashMap<String, Object>();  
        amountCell.put("content", config.getAmount() +"元");
        amountCell.put("color", getResources().getColor(R.color.Black));
        lstInfoItem.add(amountCell);  
        
        HashMap<String, Object> goCashCell = new HashMap<String, Object>();    
        goCashCell.put("color", getResources().getColor(R.color.Black));
        lstInfoItem.add(goCashCell); 
        
        HashMap<String, Object> payCell = new HashMap<String, Object>();  
        payCell.put("color", getResources().getColor(R.color.Red));
        lstInfoItem.add(payCell);  
        
        
        if(!gopay)
        { 
        	goCashCell.put("content","0元");
        	payCell.put("content", config.getAmount() + "元");  
        }
        else if(config.getCustomGoCoin()*config.getProportion() >= config.getAmount())
        {
        	goCashCell.put("content",config.getAmount() +"元"); 
        	payCell.put("content", "0元");  
        }
        else
        {
        	float lklpayamount = config.getAmount() - (config.getCustomGoCoin()*config.getProportion());
        	goCashCell.put("content",StringUtils.toDecimal(config.getCustomGoCoin()*config.getProportion()) +"元"); 
        	payCell.put("content", StringUtils.toDecimal(lklpayamount) +"元");
        }

        this.adapter.notifyDataSetChanged();
    }
}
