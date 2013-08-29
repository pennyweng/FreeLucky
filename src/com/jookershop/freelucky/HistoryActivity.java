package com.jookershop.freelucky;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class HistoryActivity  extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history);
		Intent i = getIntent();
		String phone = i.getStringExtra("p");
//		final ArrayList result = new ArrayList();
		
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(MainActivity.baseUrl + "history?p=" + phone , new AsyncHttpResponseHandler() {
			
		    @Override
			public void onFailure(Throwable arg0) {
				super.onFailure(arg0);
				System.out.println("error:" + arg0);
			}

			@Override
		    public void onSuccess(String response) {
		        System.out.println("response:" + response);
		        List items = new ArrayList();
		        
		        try {
		        	
		        	JSONArray jo = new JSONArray(response);
		        	for(int index = 0; index < jo.length(); index ++) {
		        		JSONObject obj = jo.getJSONObject(index);
		        		HistoryItem item = new HistoryItem();
		        		item.setId(obj.getString("id"));
		        		item.setTitle(obj.getString("title"));
		        		item.setNum(obj.getInt("num"));
		        		item.setTs(obj.getLong("ts"));
		        		items.add(item);
		        	}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				HistoryItem [] weather_data = (HistoryItem[]) items.toArray(new HistoryItem[items.size()]);
				HistoryAdpaterList adapter = new HistoryAdpaterList(HistoryActivity.this,R.layout.historyitem, weather_data);
				ListView listView1 = (ListView)findViewById(R.id.listView1);
				
				View header = (View)getLayoutInflater().inflate(R.layout.history_header, null);
		        listView1.addHeaderView(header);		
				listView1.setAdapter(adapter);
				
				Button button = (Button)findViewById(R.id.button1);
				button.setOnClickListener( new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Bundle bundle = new Bundle();
						Intent intent = new Intent();
						intent.putExtras(bundle);
						setResult(200, intent);
						finish();				
					}
				});
		    }
		});		
	}
}
