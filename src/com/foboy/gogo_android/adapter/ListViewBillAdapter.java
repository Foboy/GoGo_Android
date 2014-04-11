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
	
	private Context 					context;//����������
	private List<BillModel> 		listItems;//���ݼ���
	private LayoutInflater 				listContainer;//��ͼ����
	private int 						itemViewResource;//�Զ�������ͼԴ
	private DecimalFormat decimalFormat=new DecimalFormat(".00");
	
	private Map<String,String> displayMonth = new HashMap<String,String>();
	
	static class ListItemView{				//�Զ���ؼ�����  
	        public TextView nameAndPhone;  
		    public TextView date;  
		    public TextView payMethod;
		    public TextView amount;
		    public TextView month;
	 }  

	/**
	 * ʵ����Adapter
	 * @param context
	 * @param data
	 * @param resource
	 */
	public ListViewBillAdapter(Context context, List<BillModel> data,int resource) {
		this.context = context;			
		this.listContainer = LayoutInflater.from(context);	//������ͼ����������������
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
		
		//�Զ�����ͼ
		ListItemView  listItemView = null;
		
		if (convertView == null) {
			//��ȡlist_item�����ļ�����ͼ
			convertView = listContainer.inflate(this.itemViewResource, null);
			
			listItemView = new ListItemView();
			//��ȡ�ؼ�����
			listItemView.nameAndPhone = (TextView)convertView.findViewById(R.id.nameAndPhoneTextview);
			listItemView.date = (TextView)convertView.findViewById(R.id.dateTextview);
			listItemView.payMethod= (TextView)convertView.findViewById(R.id.payMethodTextview);
			listItemView.amount= (TextView)convertView.findViewById(R.id.amountTextview);
			listItemView.month = (TextView)convertView.findViewById(R.id.monthTextview);
			
			//���ÿؼ�����convertView
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
			listItemView.payMethod.setText("GO��֧��");
		else
			listItemView.payMethod.setText("������֧��");
		if(bill.getPayMethod() == PayMethodType.GoGoPay)
			listItemView.amount.setText(bill.getGoCoin() +"GO��");
		else
			listItemView.amount.setText(decimalFormat.format(bill.getAmount()) +"Ԫ");

		return convertView;
	}
	


}
