package com.foboy.gogo_android.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.foboy.gogo_android.R;
import com.foboy.gogo_android.adapter.ListViewBillAdapter.ListItemView;
import com.foboy.gogo_android.common.PayMethodType;
import com.foboy.gogo_android.models.BillModel;

import android.R.integer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PayMethodGridAdapter extends BaseAdapter {

	private ArrayList<HashMap<String, Object>> data;
	private int itemViewResource;
	private LayoutInflater listContainer;
	private Context context;
	
	static class ListItemView{				//�Զ���ؼ�����   
	    public TextView content;  
 }  
	
	public PayMethodGridAdapter(Context context, ArrayList<HashMap<String, Object>> data,int resource)
	{
		this.context = context;		
		this.listContainer = LayoutInflater.from(context);
		this.data = data;
		this.itemViewResource = resource;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
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
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ListItemView  listItemView = null;
		
		if (convertView == null) {
			//��ȡlist_item�����ļ�����ͼ
			convertView = listContainer.inflate(this.itemViewResource, null);
			
			listItemView = new ListItemView();
			//��ȡ�ؼ�����
			listItemView.content = (TextView)convertView.findViewById(R.id.paygridcontent);
			
			//���ÿؼ�����convertView
			convertView.setTag(listItemView);
		}else {
			listItemView = (ListItemView)convertView.getTag();
		}
				
		HashMap<String, Object> item = data.get(arg0);
		
		listItemView.content.setText(item.get("content").toString());
		
		listItemView.content.setTextColor(Integer.parseInt(item.get("color").toString()));

		return convertView;
	}

}
