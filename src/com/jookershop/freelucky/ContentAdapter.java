package com.jookershop.freelucky;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpResponseException;
import org.apache.http.conn.HttpHostConnectException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jookershop.freelucky.image.GridImageLoader;
import com.jookershop.freelucky.image.ImageUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class ContentAdapter extends BaseAdapter {
	public Context mContext;
	private LayoutInflater inflater;
	public List<Item> item = new ArrayList<Item>();
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private int screenWidth;
	private int screenHeight;
	private  SharedPreferences pre;
	
	public ContentAdapter(Context c, List<Item> item, int screenWidth, int screenHeight){
		mContext = c;
		inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.item = item;
	    imageLoader = ImageLoader.getInstance();
		GridImageLoader.init(c);
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		
		pre = mContext.getSharedPreferences("freelucky", mContext.MODE_PRIVATE);		
		options = new DisplayImageOptions.Builder()
//			.showStubImage(R.drawable.stub_image)
//			.showImageForEmptyUri(R.drawable.image_for_empty_url)
			.cacheInMemory()
			.cacheOnDisc()
			.build();
	    
	}
	
	public List<Item> getItem() {
		return item;
	}

	public void setItem(List<Item> item) {
		this.item = item;
	}

	@Override
	public int getCount() {
		return item.size();
	}

	@Override
	public Object getItem(int position) {
		return item.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = null;
		if (convertView != null)
			v = convertView;
		else
			v = inflater.inflate(R.layout.row, null);
		TextView title = (TextView) v.findViewById(R.id.textView2);
		title.setText(item.get(position).getTitle());
		
		TextView desc = (TextView) v.findViewById(R.id.textView1);
		desc.setText(item.get(position).getDesc());
		
		ProgressBar progressBar1 = (ProgressBar) v.findViewById(R.id.progressBar1);
		progressBar1.setProgress(item.get(position).getFunded());

//		TextView found = (TextView) v.findViewById(R.id.textView3);
//		found.setText(item.get(position).getFunded() + "%");
		
		TextView people = (TextView) v.findViewById(R.id.textView3);
		people.setText(item.get(position).getParticipator() + "人");

		TextView target = (TextView) v.findViewById(R.id.textView7);
		target.setText(item.get(position).getTarget() + "人");
		
		

		final ImageView imageView = (ImageView)v.findViewById(R.id.imageView1);
		imageLoader.displayImage(item.get(position).getImgUrl(), imageView, options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				int w = loadedImage.getWidth();
				
				int width = screenWidth;
				int height =(int) (screenHeight * 0.6);
				
				
				if(loadedImage.getHeight() > loadedImage.getWidth()) {
					height = loadedImage.getHeight() / loadedImage.getWidth() * (int) (screenHeight * 0.6);
				} else {
					width = loadedImage.getWidth() / loadedImage.getHeight() * (int) (screenHeight * 0.6); 
				}				
				Bitmap thumbnail = ImageUtil.scaleCenterCrop(loadedImage, height, width);//ImageUtil.getResizedBitmap(thumbnail, 512);
				imageView.setImageBitmap(thumbnail);

			}			
		});
		
		imageView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				checkPlay(item.get(position).cid, item.get(position).id);
			}
		});
        return v;
	}
	
	public void checkPlay(final int cid, final String id) {
		String phone = pre.getString("phone", "");
		
		if(phone == "") {
			Intent i = new Intent(mContext.getApplicationContext(), ShowTaskActivity.class);
			i.putExtra("id", id);
			i.putExtra("cid", cid);
			mContext.startActivity(i);
		} else {
			AsyncHttpClient client = new AsyncHttpClient();
			client.get(MainActivity.baseUrl + "check?id=" + id + "&p=" + phone , new AsyncHttpResponseHandler() {
				 
				@Override
				public void onFailure(Throwable arg0, String arg1) {
					super.onFailure(arg0, arg1);
					String errorMsg = "發生錯誤, 請稍候在嘗試!";
					
//					if(arg0 instanceof  HttpHostConnectException) {
//					} else 
					if(arg0 instanceof  HttpResponseException) {
						HttpResponseException hre = (HttpResponseException) arg0;
						int statusCode = hre.getStatusCode();
						
						if(statusCode == 509) {
							long t = Long.parseLong(arg1) / 60000;
							if(t > 60) {
								int h = (int) t / 60;
								int m = (int) t % 60;
								errorMsg = "需要再隔" + h + "小時又" +  m  + "分, 才能參加抽獎！";
							} else  errorMsg = "需要再隔" +  t  + "分, 才能參加抽獎！";
						}				
					}
					
					AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
	    	        builder.setCancelable(false);
					builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	    	        	public void onClick(DialogInterface dialog, int id) {
	    	        	}
	    	        });
	    	        builder.setMessage( errorMsg );
	    	        AlertDialog alert = builder.create();
	    	        alert.show();	
				}
	
				@Override
			    public void onSuccess(String response) {
			        System.out.println("response:" + response);
			        
					Intent i = new Intent(mContext.getApplicationContext(), ShowTaskActivity.class);
					i.putExtra("id", id);
					i.putExtra("cid", cid);
					mContext.startActivity(i);
			    }
			});
		}
	}
	
}
