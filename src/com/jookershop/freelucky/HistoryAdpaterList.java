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

public class HistoryAdpaterList extends ArrayAdapter<HistoryItem>{
    Context context; 
    int layoutResourceId;    
    HistoryItem data[] = null;
    private SimpleDateFormat formatter;
    
    public HistoryAdpaterList(Context context, int layoutResourceId, HistoryItem[] data) {
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
            holder.historyDate = (TextView)row.findViewById(R.id.textView1);
            holder.currentMoney = (TextView)row.findViewById(R.id.textView2);
            holder.totalMoney = (TextView)row.findViewById(R.id.textView3);
            
            row.setTag(holder);
        }
        else
        {
            holder = (WeatherHolder)row.getTag();
        }
        
        Calendar startCalendar = Calendar.getInstance();
        HistoryItem weather = data[position];
        startCalendar.setTimeInMillis(weather.getTs());
        holder.historyDate.setText(weather.getTitle());
        holder.currentMoney.setText(formatter.format(startCalendar.getTime()));
        holder.totalMoney.setText(weather.getNum() + "");
        
        return row;
    }
    
    static class WeatherHolder
    {
    	TextView historyDate;
        TextView currentMoney;
        TextView totalMoney;        
    }
}