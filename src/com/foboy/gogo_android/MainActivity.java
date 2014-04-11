package com.foboy.gogo_android;


import java.util.List;

import org.apache.http.Header;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foboy.gogo_android.common.HttpUtils;
import com.foboy.gogo_android.common.StringUtils;
import com.foboy.gogo_android.common.UrlUtils;
import com.foboy.gogo_android.controls.TagListView;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.foboy.gogo_android.controls.TagListView.TagListener;
import com.foboy.gogo_android.models.CatalogModel;
import com.foboy.gogo_android.models.CatalogsModel;
import com.foboy.gogo_android.models.GoInfoModel;
import com.foboy.gogo_android.widget.LoadingMaskView;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MainActivity extends Activity implements TagListener{

	private EditText phone;
	private EditText amount;
	private LoadingMaskView lmv;
	private TagListView tagListView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GlobalConfig.getInstance().setMainActivity(this);
        
        phone = (EditText)findViewById(R.id.phoneet);
        amount = (EditText)findViewById(R.id.priceet);
        
        lmv = new LoadingMaskView(MainActivity.this);
        
        if(!GlobalConfig.getInstance().isLogin())
        {
        	this.toLogin();
        }
        else
        {
        	this.bindCatalogs();
        }
        
        TextView title = (TextView) findViewById(R.id.frame_main_header_title);
        title.setText("结账");
        
        tagListView = (TagListView) findViewById(R.id.tagview);
        

        
        Button btn = (Button)findViewById(R.id.topay);
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				if(!isInputPassed())
					return;
				
		        String urlString = UrlUtils.GetGoInfo;
		        RequestParams params = new RequestParams(); // 绑定参数
		        params.put("phone", phone.getText().toString());

		        HttpUtils.post(urlString, params, new BaseJsonHttpResponseHandler<GoInfoModel>() {

		        	
		            @Override
		            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, GoInfoModel response) {
		            		if(response.isLogin() && response.isSuccess())
		            		{
		            			GlobalConfig config =GlobalConfig.getInstance();
		            			config.setAmount(Float.parseFloat(amount.getText().toString()));
		            			config.setCustomGoCoin(response.getData().getBalance());
		            			config.setProportion(response.getData().getProportion());
		            			config.setCustomName(response.getData().getName());
		            			config.setCustomPhone(phone.getText().toString());
		            			config.setCusomerId(response.getData().getCid());
		        				Intent it = new Intent(MainActivity.this,PayMethodActivity.class);
		        				startActivity(it);
		            		}
		            		else
		            		{
		            			if(!response.isLogin())
		            				MainActivity.this.toLogin();
		            			else
		            				Toast.makeText(MainActivity.this,  response.getErrorMessage(), Toast.LENGTH_LONG).show();
		            		}
		            		lmv.hide();
		            }

		            @Override
		            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, GoInfoModel errorResponse) {

		            		Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
		            		lmv.hide();
		            }

		            @Override
		            protected GoInfoModel parseResponse(String rawJsonData) throws Throwable {
		                return new ObjectMapper().readValues(new JsonFactory().createParser(rawJsonData), GoInfoModel.class).next();
		            }

		        });

			}
		});
        
        Button billsbtn = (Button)findViewById(R.id.billsbtn);
        billsbtn.setVisibility(View.VISIBLE);
        billsbtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it = new Intent(MainActivity.this,BillsActivity.class);
				startActivity(it);
			}
		});
        ImageButton topbackbtn = (ImageButton)findViewById(R.id.topbackbtn);
        topbackbtn.setVisibility(View.GONE);
    }
    
    private void toLogin()
    {
		Intent it = new Intent(MainActivity.this,LoginActivity.class);
		startActivity(it);
    }
    
    private boolean isInputPassed()
    {
		String ph = phone.getText().toString();
		if(StringUtils.isEmpty(ph)){
			Toast.makeText(MainActivity.this, "请输入客户手机", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(!StringUtils.isPhone(ph))
		{
			Toast.makeText(MainActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
			return false;
		}
		String amt = amount.getText().toString();
		if(StringUtils.isEmpty(amt)){
			Toast.makeText(MainActivity.this, "请输入消费金额", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(!StringUtils.isNumber(amt)){
			Toast.makeText(MainActivity.this, "请输入正确的消费金额" +amt, Toast.LENGTH_SHORT).show();
			return false;
		}
		List<Integer> catalogids = this.tagListView.selectedTagIds();
		if(catalogids.size() == 0)
		{
			Toast.makeText(MainActivity.this, "请至少选择一种消费类型" +amt, Toast.LENGTH_SHORT).show();
			return false;
		}
		GlobalConfig.getInstance().setCatalogIds(catalogids);
		
		return true;
    }
    
    private void bindCatalogs()
    {
    	if(this.tagListView == null || this.tagListView.isBinded())
    		return;
    	CatalogModel[] catalogs = GlobalConfig.getInstance().getCatalogs();
    	if(catalogs != null)
    	{
	    	this.tagListView.setBinded(true);
			for(int i=0;i<catalogs.length;i++)
			{
				CatalogModel cat = catalogs[i];
				this.tagListView.addTag(cat.getName(), cat.getId());
			}
    	}
    }
    
  
    @Override
    public void onResume()
    {
    	super.onResume();
    	this.bindCatalogs();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
	@Override
	public void onAddedTag(String tag) {
		Log.d("tagview", "added tag " + tag);
	}

	@Override
	public void onRemovedTag(String tag) {
		Log.d("tagview", "removed tag " + tag);
	}
}
