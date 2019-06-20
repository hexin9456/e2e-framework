package com.example.hexin.testdemo.eventbus;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import com.example.hexin.testdemo.R;
import com.example.hexin.testdemo.eventbus.event.GetDatasEvent;
import com.example.hexin.testdemo.eventbus.event.ToastEvent;
import com.example.hexin.testdemo.eventbus.presenter.EventBusPresenterCompl;
import com.example.hexin.testdemo.eventbus.presenter.IEventBusPresenter;
import com.example.hexin.testdemo.eventbus.view.IEventBusView;

public class EventBusActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,IEventBusView {

	private IEventBusPresenter iEventBusPresenter;
	List<String> datas = new ArrayList<>();
	private BaseAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		//register
		EventBus.getDefault().register(this);

		//find view
		ListView listView = (ListView) this.findViewById(R.id.list_home);

		//set listener
		listView.setOnItemClickListener(this);

		//init
		View loadingView = LayoutInflater.from(this).inflate(R.layout.item_empty_view, null);
		ViewGroup viewGroup = (ViewGroup) this.findViewById(R.id.layout_home);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		viewGroup.addView(loadingView, layoutParams);
		listView.setEmptyView(loadingView);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datas);
		listView.setAdapter(adapter);
		iEventBusPresenter = new EventBusPresenterCompl(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		iEventBusPresenter.loadDatas();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		iEventBusPresenter.onItemClick(position);
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	// EventBus Subscribe
	public void onEvent(ToastEvent toastEvent){
		if (toastEvent!=null&&toastEvent.getMessage()!=null){
			Toast.makeText(this,toastEvent.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	// EventBus Subscribe
	public void onEvent(GetDatasEvent getDatasEvent){
		if (getDatasEvent!=null && getDatasEvent.getDatas()!=null && getDatasEvent.getDatas().size()>0){
			this.datas.clear();
			this.datas.addAll(getDatasEvent.getDatas());
			adapter.notifyDataSetChanged();
		}
	}

}
