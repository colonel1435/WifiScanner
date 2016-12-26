package com.wumin.wifiscaner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by samsung-pc on 2016/9/16.
 */
public class PageFragment extends Fragment {
    public static final String ARGS_PAGE = "PAGE";
    public static final String ARGS_WIFI = "WIFI_INFO";
    public static String TAGS = "wumin.y";
    private List<WifiInfo> mWifiList;
    private int mPage;

    public static PageFragment newInstace(int page, List<WifiInfo> wifiList) {
        Log.i(TAGS, "List num1 : " + wifiList.size());
        Bundle args = new Bundle();
        args.putInt(ARGS_PAGE, page);
        args.putSerializable(ARGS_WIFI, (Serializable)wifiList);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        Log.i(TAGS, "PAGES : " + page);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWifiList =(List<WifiInfo>) getArguments().getSerializable(ARGS_WIFI);
        mPage = getArguments() != null ? getArguments().getInt(ARGS_PAGE) : 1;
        Log.i(TAGS, "List num2 : " + mWifiList.size());
        Log.i(TAGS, "onCreate Pages : " + mPage);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_main, null);
        ListView listView = (ListView) view.findViewById(R.id.wifi_list);
        WifiAdapter wifiAdapter = new WifiAdapter(mWifiList, this.getContext());
        listView.setAdapter(wifiAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String memo = "Wifi : " + mWifiList.get(position).ssid + "\nPassword : " + mWifiList.get(position).psw;
                Snackbar.make(view, memo, Snackbar.LENGTH_LONG)
                        .setAction("Share", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.i(TAGS, "onItemClick memo = " + memo);
                                Intent shareIntent = new Intent();
                                shareIntent.setAction(Intent.ACTION_SEND);
                                shareIntent.putExtra(Intent.EXTRA_SUBJECT,"share");
                                shareIntent.putExtra(Intent.EXTRA_TEXT, memo);
                                shareIntent.setType("text/plain");
                                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(Intent.createChooser(shareIntent, "Share To"));
                            }
                        })
                        .show();
            }
        });
        Log.i(TAGS, "onCreateView : " + mPage);
        return view;
    }


}
