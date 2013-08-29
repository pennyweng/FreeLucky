package com.jookershop.freelucky;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.text.Html;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.*;

public class MainActivity extends Activity {
	int selectdCategory = 0;
	private ListView listView;
	public static String baseUrl = "http://www.jookershop.com:8080/";
//	public static String baseUrl = "http://192.168.1.4:8080/";
	private String [] categoryString;// = new String[] { "3C", "餐廳", "住宿"};	
	private ContentAdapter adapter;
	private int screenWidth;
	private int screenHeight;
	private  SharedPreferences pre;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		refreshMenu();
		pre = getSharedPreferences("freelucky", MODE_PRIVATE);
		
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		screenWidth = size.x;
		screenHeight = size.y;
		
		listView = (ListView) this.findViewById(R.id.listView1);
		adapter = new ContentAdapter(this, new ArrayList(), screenWidth, screenHeight);
		listView.setAdapter(adapter);
		
		final TextView category = (TextView)this.findViewById(R.id.category);
		category.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(MainActivity.this).setTitle("類別").setIcon( 
						android.R.drawable.ic_dialog_info).setSingleChoiceItems(categoryString, selectdCategory, 
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										category.setText("類別:" + categoryString[which]);
										selectdCategory = which;
										refreshView(selectdCategory);
										dialog.dismiss();
										}
									}).setNegativeButton("取消", null).show();				
			}
		});
		refreshView(selectdCategory);
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if(item.getItemId() == R.id.action_settings) {
			AsyncHttpClient client = new AsyncHttpClient();
			client.get(baseUrl + "rule/rule.txt" , new AsyncHttpResponseHandler() {
				
			    @Override
				public void onFailure(Throwable arg0) {
					super.onFailure(arg0);
					System.out.println("error:" + arg0);
				}

				@Override
			    public void onSuccess(String response) {
					AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
	    	        builder.setCancelable(false);
					builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
						
	    	        	public void onClick(DialogInterface dialog, int id) {
	    	        	}
	    	        });
	    	        
	    	        
	    	        LayoutInflater inflater = getLayoutInflater();
	    	        View dialoglayout = inflater.inflate(R.layout.rule, (ViewGroup) findViewById(R.id.rr1));
	    	        TextView tv1 = (TextView) dialoglayout.findViewById(R.id.textView1);
	    	        tv1.setText(Html.fromHtml(response));
	    	        
	    	        builder.setView(dialoglayout);
	    	        builder.show();
			    }
			});		

		} else if(item.getItemId() == R.id.action_mywinlist) {
			String phone = pre.getString("phone", "");
			if(phone == "") {
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
    	        builder.setCancelable(false);
				builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
    	        	public void onClick(DialogInterface dialog, int id) {
    	        	}
    	        });
    	        builder.setMessage("尚未參與任何抽獎活動");
    	        AlertDialog alert = builder.create();
    	        alert.show();				
			} else {
				Intent i = new Intent(getApplicationContext(), HistoryActivity.class);
				i.putExtra("p", phone);
				MainActivity.this.startActivityForResult(i, 200);
			}
		} else if(item.getItemId() == R.id.action_winlist) {
				Intent i = new Intent(getApplicationContext(), WinActivity.class);
				MainActivity.this.startActivityForResult(i, 210);
		}
		
		return super.onMenuItemSelected(featureId, item);
	}

	public void refreshMenu() { 
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(baseUrl + "categorymenu", new AsyncHttpResponseHandler() {
			
		    @Override
			public void onFailure(Throwable arg0) {
				super.onFailure(arg0);
				System.out.println("error:" + arg0);
			}

			@Override
		    public void onSuccess(String response) {
		        System.out.println("response:" + response);
		        categoryString = response.split(",");
		    }
		});		
		
	}
	
	public void refreshView(final int category) {
		AsyncHttpClient client = new AsyncHttpClient();
		System.out.println("response:" + baseUrl + "category/" + category);
		client.get(baseUrl + "category/" + category , new AsyncHttpResponseHandler() {
			
		    @Override
			public void onFailure(Throwable arg0) {
				super.onFailure(arg0);
				System.out.println("error:" + arg0);
			}

			@Override
		    public void onSuccess(String response) {
		        System.out.println("response:" + response);

		        try {
		        	List items = new ArrayList();
		        	JSONArray jo = new JSONArray(response);
		        	for(int index = 0; index < jo.length(); index ++) {
		        		JSONObject obj = jo.getJSONObject(index);
		        		Item item = new Item();
		        		item.setId(obj.getString("id"));
		        		item.setTitle(obj.getString("title"));
		        		item.setDesc(obj.getString("desc"));
		        		item.setParticipator(obj.getInt("participator"));
		        		item.setTarget(obj.getInt("target"));
		        		item.setImgUrl(obj.getString("img"));
		        		item.setOpendate(obj.getLong("opendate"));
		        		item.setCid(category);
		        		items.add(item);
		        	}
					adapter.setItem(items);
					adapter.notifyDataSetChanged();
				} catch (JSONException e) {
					e.printStackTrace();
				}
		    }
		});		
		
	}
}
