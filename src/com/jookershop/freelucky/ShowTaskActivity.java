package com.jookershop.freelucky;

import org.apache.http.client.HttpResponseException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdView;
import com.inmobi.androidsdk.IMAdInterstitial;
import com.inmobi.androidsdk.IMAdRequest;
import com.inmobi.androidsdk.IMAdView;
import com.inmobi.androidsdk.impl.net.Response;
import com.kuad.KuBanner;
import com.kuad.kuADListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.mdotm.android.view.MdotMAdView;
import com.vpon.ads.VponAd;
import com.vpon.ads.VponAdListener;
import com.vpon.ads.VponAdRequest.VponErrorCode;
import com.vpon.ads.VponBanner;

public class ShowTaskActivity extends Activity {
	private VponBanner vponBanner = null;
//	private String bannerId = "8a808182405cf45a014073c1607e08a0";
//	private String admobBannerId = "a1520a5e8341bb7";
	private String kuId = "000000Riu";
	
	private AdView adView;
	private MdotMAdView adMdotView;
	private IMAdView mIMAdView;
	private IMAdInterstitial mIMAdInterstitial;
	private IMAdRequest mAdRequest;
	private KuBanner banner;
	
	private boolean isClickAD1;
	private boolean isClickAD2;
	private boolean isClickAD3 = true;
	private  SharedPreferences pre;
	private int cid;
	private String id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task);
		
		Intent i = this.getIntent();
		id = i.getStringExtra("id");
		cid = i.getIntExtra("cid", 0);
		
		pre = ShowTaskActivity.this.getSharedPreferences("freelucky", ShowTaskActivity.this.MODE_PRIVATE);

		vponBanner = (VponBanner) findViewById(R.id.vponBannerXML);
		vponBanner.setAdListener(new VponAdListener() {

			@Override
			public void onVponDismissScreen(VponAd arg0) {
				isClickAD1 = true;
			}

			@Override
			public void onVponFailedToReceiveAd(VponAd arg0, VponErrorCode arg1) {
				isClickAD1 = true;
			}

			@Override
			public void onVponLeaveApplication(VponAd arg0) {
			}

			@Override
			public void onVponPresentScreen(VponAd arg0) {
			}

			@Override
			public void onVponReceiveAd(VponAd arg0) {
			}});
		
		
		adView = (AdView) findViewById(R.id.adView);
    	adView.setAdListener(new AdListener() {
			@Override
			public void onDismissScreen(Ad arg0) {
				isClickAD2 = true;
			}

			@Override
			public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {
				isClickAD2 = true;
			}

			@Override
			public void onLeaveApplication(Ad arg0) {
			}

			@Override
			public void onPresentScreen(Ad arg0) {
			}

			@Override
			public void onReceiveAd(Ad arg0) {
			}
    	});
		
		banner = (KuBanner) findViewById(R.id.kuadview);
		banner.setAPID(kuId);
		banner.appStart();
		banner.setkuADListener(new kuADListener(){
			@Override
			public void onFailedRecevie(String arg0) {
				isClickAD3 = true;
			}
	
			@Override
			public void onRecevie(String arg0) {
				isClickAD3 = true;
			}
		});
		
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		final EditText phone = (EditText) this.findViewById(R.id.editText1);
		if(pre.contains("phone")){
			phone.setText(pre.getString("phone", ""));
		}
		phone.clearFocus();
		
		Button ad = (Button) this.findViewById(R.id.button136);
		ad.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
		    	SharedPreferences sp = getSharedPreferences("freelucky", MODE_PRIVATE);
		    	sp.edit().putBoolean("rating", true).commit();
		    	
				Intent intent = new Intent(Intent.ACTION_VIEW); 
				intent.setData(Uri.parse("market://details?id=com.jookershop.freelucky")); 
				startActivity(intent);				}
		});
		
		Button ok = (Button) this.findViewById(R.id.button1);
		ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String errorMsg = "";
				String phoneString = phone.getText().toString();
				
				if(!isClickAD1 || !isClickAD2) {
					errorMsg = "請點選所有廣告";
				} else if(phoneString == null || phoneString.equals("")) {
					errorMsg = "請填寫電話";
				}

				if(errorMsg != "") {
					AlertDialog.Builder builder = new AlertDialog.Builder(ShowTaskActivity.this);
	    	        builder.setCancelable(false);
					builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	    	        	public void onClick(DialogInterface dialog, int id) {
	    	        	}
	    	        });
	    	        builder.setMessage( errorMsg );
	    	        AlertDialog alert = builder.create();
	    	        alert.show();				
				} else {
					submitPlay(cid, id, phoneString);
					
				}
			}
		});
		
		Button cancel = (Button) this.findViewById(R.id.button2);
		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ShowTaskActivity.this, MainActivity.class);
				ShowTaskActivity.this.startActivity(intent);
			}
		});

		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (vponBanner != null) {
			vponBanner.destroy();
			vponBanner = null;
		}
		if(banner != null ) banner.destory();

		if (adView != null) {
			adView.destroy();
		}

		if(adMdotView != null)
			adMdotView.endAdSession();
		
		if (mIMAdView != null)
			mIMAdView.destroy();

		if (mIMAdInterstitial != null)
			mIMAdInterstitial.destroy();
	}
	
	public void submitPlay(int category, String id, final String phone) {
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(MainActivity.baseUrl + "play?cid=" + category + "&id=" + id + "&p=" + phone , new AsyncHttpResponseHandler() {

			 
			@Override
			public void onFailure(Throwable arg0, String arg1) {
				super.onFailure(arg0, arg1);
				System.out.println("error:" + arg0);
				
				HttpResponseException hre = (HttpResponseException) arg0;
				int statusCode = hre.getStatusCode();
				
				String errorMsg = "發生錯誤, 請稍候在嘗試!";
				if(statusCode == 509) {
					long t = Long.parseLong(arg1) / 60000;
					if(t > 60) {
						int h = (int) t / 60;
						int m = (int) t % 60;
						errorMsg = "需要再隔" + h + "小時又" +  m  + "分, 才能參加抽獎！";
					} else  errorMsg = "需要再隔" +  t  + "分, 才能參加抽獎！";
				}				
				
				AlertDialog.Builder builder = new AlertDialog.Builder(ShowTaskActivity.this);
    	        builder.setCancelable(false);
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					
    	        	public void onClick(DialogInterface dialog, int id) {
    					Intent intent = new Intent(ShowTaskActivity.this, MainActivity.class);
    					ShowTaskActivity.this.startActivity(intent);
    	        	}
    	        });
    	        builder.setMessage( errorMsg );
    	        AlertDialog alert = builder.create();
    	        alert.show();	
			}


			@Override
		    public void onSuccess(String response) {
		        System.out.println("response:" + response);
				pre.edit().putString("phone", phone).commit();
				
				AlertDialog.Builder builder = new AlertDialog.Builder(ShowTaskActivity.this);
    	        builder.setCancelable(false);
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					
    	        	public void onClick(DialogInterface dialog, int id) {
    					Intent intent = new Intent(ShowTaskActivity.this, MainActivity.class);
    					ShowTaskActivity.this.startActivity(intent);
    	        	}
    	        });
				builder.setMessage( "您此商品的抽獎序號為:" +  response  + ",感謝您的參與！" );
    	        AlertDialog alert = builder.create();
    	        alert.show();				
		    }
		});		
	}
}
