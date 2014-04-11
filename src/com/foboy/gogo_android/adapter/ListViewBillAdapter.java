package com.foboy.gogo_android.adapter;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.foboy.gogo_android.R;
import com.foboy.gogo_android.common.PayMethodType;
import com.foboy.gogo_android.models.BillModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewBillAdapter extends BaseAdapter {
	
	private Context 					context;//运行上下文
	private List<BillModel> 		listItems;//数据集合
	private LayoutInflater 				listContainer;//视图容器
	private int 						itemViewResource;//自定义项视图源
	private DecimalFormat decimalFormat=new DecimalFormat(".00");
	
	private Map<String,String> displayMonth = new HashMap<String,String>();
	
	static class ListItemView{				//自定义控件集合  
	        public TextView nameAndPhone;  
		    public TextView date;  
		    public TextView payMethod;
		    public TextView amount;
		    public TextView month;
	 }  

	/**
	 * 实例化Adapter
	 * @param context
	 * @param data
	 * @param resource
	 */
	public ListViewBillAdapter(Context context, List<BillModel> data,int resource) {
		this.context = context;			
		this.listContainer = LayoutInflater.from(context);	//创建视图容器并设置上下文
		this.itemViewResource = resource;
		this.listItems = data;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listItems.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//Log.d("method", "getView");
		
		//自定义视图
		ListItemView  listItemView = null;
		
		if (convertView == null) {
			//获取list_item布局文件的视图
			convertView = listContainer.inflate(this.itemViewResource, null);
			
			listItemView = new ListItemView();
			//获取控件对象
			listItemView.nameAndPhone = (TextView)convertView.findViewById(R.id.nameAndPhoneTextview);
			listItemView.date = (TextView)convertView.findViewById(R.id.dateTextview);
			listItemView.payMethod= (TextView)convertView.findViewById(R.id.payMethodTextview);
			listItemView.amount= (TextView)convertView.findViewById(R.id.amountTextview);
			listItemView.month = (TextView)convertView.findViewById(R.id.monthTextview);
			
			//设置控件集到convertView
			convertView.setTag(listItemView);
		}else {
			listItemView = (ListItemView)convertView.getTag();
		}
				
		BillModel bill = listItems.get(position);
		
		if(bill.isDisplayMonth())
		{
			listItemView.month.setText(bill.getMonth());
			listItemView.month.setVisibility(View.VISIBLE);
		}
		else
		{
			listItemView.month.setVisibility(View.GONE);
		}

		listItemView.nameAndPhone.setText(bill.getName() +"(" +bill.getPhone() +")");
		listItemView.date.setText(bill.getDate());
		if(bill.getPayMethod() == PayMethodType.GoGoPay)
			listItemView.payMethod.setText("GO币支付");
		else
			listItemView.payMethod.setText("拉卡拉支付");
		if(bill.getPayMethod() == PayMethodType.GoGoPay)
			listItemView.amount.setText(bill.getGoCoin() +"GO币");
		else
			listItemView.amount.setText(decimalFormat.format(bill.getAmount()) +"元");

		return convertView;
	}
	


}
