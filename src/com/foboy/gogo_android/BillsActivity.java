package com.foboy.gogo_android;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.foboy.gogo_android.R;
import com.foboy.gogo_android.adapter.ListViewBillAdapter;
import com.foboy.gogo_android.common.HttpUtils;
import com.foboy.gogo_android.common.IgnoreCaseNamingStrategy;
import com.foboy.gogo_android.common.StringUtils;
import com.foboy.gogo_android.common.UrlUtils;
import com.foboy.gogo_android.models.BillListModel;
import com.foboy.gogo_android.models.BillModel;
import com.foboy.gogo_android.models.CatalogsModel;
import com.foboy.gogo_android.widget.PullToRefreshListView;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class BillsActivity extends Activity {
	
	private View lvList_footer;
	
	private TextView lvList_foot_more;
	
	private ProgressBar lvList_foot_progress;
	
	private PullToRefreshListView lvList;
	
	
	private ListViewBillAdapter lvListAdapter;
	
	private List<BillModel> lvListData = new ArrayList<BillModel>();
	
	private Handler lvListHandler;
	
	private int lvListSumData;
	
	private final int PAGE_SIZE=20;
	private final int LISTVIEW_ACTION_INIT = 1;
	private final int LISTVIEW_DATA_MORE = 1;
	private final int LISTVIEW_ACTION_SCROLL = 3;
	private final int LISTVIEW_ACTION_REFRESH = 2;
	private final int LISTVIEW_DATATYPE_BILLS = 8;
	private final int LISTVIEW_DATA_FULL = 3;
	private final int LISTVIEW_DATA_EMPTY = 4;
	
	int preMonth = 0;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills);
        TextView title = (TextView) findViewById(R.id.frame_main_header_title);
        title.setText("消费账单");
        ImageButton topbackbtn = (ImageButton)findViewById(R.id.topbackbtn);
        topbackbtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//Intent it = new Intent(PayMethodActivity.this,MainActivity.class);
				//it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				//startActivity(it);
				BillsActivity.this.finish();
			}
		});
        this.initFrameListView();
        
    }
	
	   /**
     * 初始化所有ListView
     */
    private void initFrameListView()
    {
    	//初始化listview控件
		this.initListView();
		//加载listview数据
		this.initFrameListViewData();
    }
    /**
     * 初始化所有ListView数据
     */
    private void initFrameListViewData()
    {
        //初始化Handler 
        
    	lvListHandler = this.getLvHandler(lvList, lvListAdapter, lvList_foot_more, lvList_foot_progress, PAGE_SIZE);
        //加载资讯数据
		if(lvListData.isEmpty()) {
			loadLvListData(0, lvListHandler, LISTVIEW_ACTION_INIT);
		}
    }

    private void initListView()
    {   
        lvListAdapter = new ListViewBillAdapter(this, lvListData, R.layout.item_bill_listitem);        
        lvList_footer = getLayoutInflater().inflate(R.layout.listview_footer, null);
        lvList_foot_more = (TextView)lvList_footer.findViewById(R.id.listview_foot_more);
        lvList_foot_progress = (ProgressBar)lvList_footer.findViewById(R.id.listview_foot_progress);
        lvList = (PullToRefreshListView)findViewById(R.id.frame_listview_bills);
        lvList.addFooterView(lvList_footer);//添加底部视图  必须在setAdapter前
        lvList.setAdapter(lvListAdapter); 

        lvList.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				lvList.onScrollStateChanged(view, scrollState);
				
				//数据为空--不用继续下面代码了
				if(lvListData.isEmpty()) return;
				
				//判断是否滚动到底部
				boolean scrollEnd = false;
				try {
					if(view.getPositionForView(lvList_footer) == view.getLastVisiblePosition())
						scrollEnd = true;
				} catch (Exception e) {
					scrollEnd = false;
				}
				
				int lvDataState = StringUtils.toInt(lvList.getTag());
				if(scrollEnd && lvDataState==LISTVIEW_DATA_MORE)
				{
					lvList_foot_more.setText(R.string.load_ing);
					lvList_foot_progress.setVisibility(View.VISIBLE);
					//当前pageIndex
					int pageIndex = lvListSumData/PAGE_SIZE;
					loadLvListData(pageIndex, lvListHandler, LISTVIEW_ACTION_SCROLL);
				}
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
				lvList.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
			}
		});
        lvList.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
			public void onRefresh() {
            	BillsActivity.this.preMonth = 0;
            	loadLvListData(0, lvListHandler, LISTVIEW_ACTION_REFRESH);
            }
        });			
    }
    
    /**
     * 获取listview的初始化Handler
     * @param lv
     * @param adapter
     * @return
     */
    private Handler getLvHandler(final PullToRefreshListView lv,final BaseAdapter adapter,final TextView more,final ProgressBar progress,final int pageSize){
    	return new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.what >= 0){
					//listview数据处理
					handleLvData(msg.what, msg.obj, msg.arg2, msg.arg1);
					
					if(msg.what < pageSize){
						lv.setTag(LISTVIEW_DATA_FULL);
						adapter.notifyDataSetChanged();
						more.setText(R.string.load_full);
					}else if(msg.what == pageSize){
						lv.setTag(LISTVIEW_DATA_MORE);
						adapter.notifyDataSetChanged();
						more.setText(R.string.load_more);
					}
				}
				else if(msg.what == -1){
					//有异常--显示加载出错 & 弹出错误消息
					lv.setTag(LISTVIEW_DATA_MORE);
					more.setText(R.string.load_error);
					Toast.makeText(BillsActivity.this, ((Exception)msg.obj).getMessage(),  Toast.LENGTH_LONG).show();
				}
				if(adapter.getCount()==0){
					lv.setTag(LISTVIEW_DATA_EMPTY);
					more.setText(R.string.load_empty);
				}
				progress.setVisibility(View.GONE);
				if(msg.arg1 == LISTVIEW_ACTION_REFRESH){
					lv.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
					lv.setSelection(0);
				}
			}
		};
    }
    /**
     * listview数据处理
     * @param what 数量
     * @param obj 数据
     * @param objtype 数据类型
     * @param actiontype 操作类型
     * @return notice 通知信息
     */
    private BillListModel handleLvData(int what,Object obj,int objtype,int actiontype){
    	BillListModel model = null;
		switch (actiontype) {
			case LISTVIEW_ACTION_INIT:
			case LISTVIEW_ACTION_REFRESH:
				switch (objtype) {
					case LISTVIEW_DATATYPE_BILLS:
						model = (BillListModel)obj;
						List<BillModel> blist = model.getData();
						lvListSumData = what;

						lvListData.clear();//先清除原有数据

						for(int i=0;i<blist.size();i++)
						{
							lvListData.add(blist.get(i));
						}
						break;
				}
				if(actiontype == LISTVIEW_ACTION_REFRESH){

				}
				break;
			case LISTVIEW_ACTION_SCROLL:
				switch (objtype) {
					case LISTVIEW_DATATYPE_BILLS:
						model = (BillListModel)obj;
						List<BillModel> blist = model.getData();
						lvListSumData += what;
						for(int i=0;i<blist.size();i++)
						{
							lvListData.add(blist.get(i));
						}
						break;
				}
				break;
		}
		return model;
    }
	
	private void loadLvListData(final int pageIndex,final Handler handler,final int action)
	{
        String urlString = UrlUtils.GetBillList;
        RequestParams params = new RequestParams(); // 绑定参数
        params.put("page_index", pageIndex);
        HttpUtils.post(urlString, params, new BaseJsonHttpResponseHandler<BillListModel>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, BillListModel response) {
            		if(response.isSuccess())
            		{
        				Message msg = new Message();
        				try {
        					//获取数据
        					BillsActivity.this.BillListParse(response);
        					msg.what = response.getRecordCount();
        					msg.obj = response;
        	            } catch (Exception e) {
        	            	e.printStackTrace();
        	            	msg.what = -1;
        	            	msg.obj = e;
        	            }
        				msg.arg1 = action;
        				msg.arg2 = LISTVIEW_DATATYPE_BILLS;
        				handler.sendMessage(msg);
            		}
            		else
            		{
            			Toast.makeText(BillsActivity.this,  response.getErrorMessage(), Toast.LENGTH_LONG).show();
            		}
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, BillListModel errorResponse) {

            		Toast.makeText(BillsActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            protected BillListModel parseResponse(String rawJsonData) throws Throwable {
            	ObjectMapper om = new ObjectMapper();
            	//om.setPropertyNamingStrategy(new IgnoreCaseNamingStrategy());
                return om.readValues(new JsonFactory().createParser(rawJsonData), BillListModel.class).next();
            }

        });
	}
	
	private void BillListParse(BillListModel response)
	{
		if(response.getData() != null && response.getData().size() >0)
		{
			int size = response.getData().size();

			BillModel bill;
			for(int i=0;i<size;i++)
			{
				bill = response.getData().get(i);
		        Calendar date = Calendar.getInstance();
		        date.setTimeInMillis(bill.getCreateTime()*1000);
		        int month = date.get(Calendar.YEAR) *100 + date.get(Calendar.MONTH) +1;
		        bill.setMonth(this.parseMonthToString(month));
		        if(preMonth ==0 || (preMonth >0 && preMonth != month))
		        	bill.setDisplayMonth(true);
		        else
		        	bill.setDisplayMonth(false);
		        bill.setDate(StringUtils.dateToNoYearString(date.getTime()));
		        preMonth = month;
			}
		}
	}
	
	private String parseMonthToString(int month)
	{
        Date d = new Date();  
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        if(month/100 == now.get(Calendar.YEAR))
        {
        	if(month%100 == (now.get(Calendar.MONTH)+1))
        	{
        		return "本月";
        	}
        	else
        	{
        		return month%100 + "月";
        	}
        }
        else
        {
        	return month/100 +"年" +  month%100 + "月";
        }
	}
}
