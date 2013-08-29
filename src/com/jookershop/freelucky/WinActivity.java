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

public class WinActivity  extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.win);
		
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(MainActivity.baseUrl + "winlist" , new AsyncHttpResponseHandler() {
			
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
		        		WinItem item = new WinItem();
		        		item.setId(obj.getString("id"));
		        		item.setTitle(obj.getString("title"));
		        		item.setOpendate(obj.getLong("opendate"));
		        		item.setWin_num(obj.getInt("win_num"));
		        		item.setPhone(obj.getString("win_phone"));
		        		items.add(item);
		        	}
				} catch (JSONException e) {
					e.printStackTrace();
				}
		        WinItem [] weather_data = (WinItem[]) items.toArray(new WinItem[items.size()]);
		        WinAdpaterList adapter = new WinAdpaterList(WinActivity.this,R.layout.winitem, weather_data);
				ListView listView1 = (ListView)findViewById(R.id.listView1);
				
				View header = (View)getLayoutInflater().inflate(R.layout.win_header, null);
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
