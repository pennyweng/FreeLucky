package com.jookershop.freelucky;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class WinAdpaterList extends ArrayAdapter<WinItem>{
    Context context; 
    int layoutResourceId;    
    WinItem data[] = null;
    private SimpleDateFormat formatter;
    
    public WinAdpaterList(Context context, int layoutResourceId, WinItem[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");        
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        WeatherHolder holder = null;
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new WeatherHolder();
            holder.title = (TextView)row.findViewById(R.id.textView1);
            holder.opendate = (TextView)row.findViewById(R.id.textView2);
            holder.win_num = (TextView)row.findViewById(R.id.textView3);
            holder.win_phone = (TextView)row.findViewById(R.id.textView4);
            
            row.setTag(holder);
        }
        else
        {
            holder = (WeatherHolder)row.getTag();
        }
        
        Calendar startCalendar = Calendar.getInstance();
        WinItem weather = data[position];
        startCalendar.setTimeInMillis(weather.getOpendate());
        holder.title.setText(weather.getTitle());
        holder.opendate.setText(formatter.format(startCalendar.getTime()));
        holder.win_num.setText(weather.getWin_num() + "");
        holder.win_phone.setText(weather.getPhone());
        
        return row;
    }
    
    static class WeatherHolder
    {
    	TextView id;
        TextView title;
        TextView opendate;
        TextView win_num;
        TextView win_phone;
    }
}