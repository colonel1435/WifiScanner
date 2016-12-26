package com.wumin.wifiscaner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Zero on 2016/09/21.
 */
public class WifiAdapter extends BaseAdapter {
    public List<WifiInfo> mList = null;
    public Context mContext = null;
    public WifiAdapter(List<WifiInfo> list, Context context) {
        this.mList = list;
        this.mContext = context;
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.wifi_info_item, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.wifi_item);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.textView.setText("Wifi id : " + mList.get(position).ssid + "\nPassword : " + mList.get(position).psw);
        return convertView;
    }

    static class ViewHolder {
        TextView textView;
    }
}
