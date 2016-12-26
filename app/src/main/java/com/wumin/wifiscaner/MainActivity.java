package com.wumin.wifiscaner;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//import cn.sharesdk.onekeyshare.OnekeyShare;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private TabLayout mTabLayout = null;
    private ViewPager mViewPager = null;
    private MyFragmentPageAdapter myFragmentPageAdapter = null;
    public List<String> mTitleList = null;
    private List<Fragment> mFragmentList = null;
    private List<WifiInfo> mWifiList = null;
    public  final int MSG_REFRESH = 1;
    public final int MSG_EXPORT = 2;
    public final int TOP_NUM = 5;
    public  String TAG = "wumin.y";
    private String wifiFileName = "/sdcard/wifiText.txt";
    private Context mContext = null;
    private Toolbar toolbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();

        Bundle wifiArgs = getIntent().getBundleExtra(PageFragment.ARGS_WIFI);
        mWifiList = (List<WifiInfo>) wifiArgs.getSerializable(PageFragment.ARGS_WIFI);

        InitView();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                Snackbar.make(getWindow().getDecorView(), "Settings is enter", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.action_refresh:
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.i("wumin.y", "start refresh");
//                            mWifiList = WifiManage.Read();
                            if (mWifiList != null) {
                                Message msg = new Message();
                                msg.what = MSG_REFRESH;
                                mHandler.sendMessage(msg);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_export) {
            Export2File(wifiFileName);
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            String info = List2String(mWifiList);
            shareIntent.putExtra(Intent.EXTRA_TEXT, info);
            shareIntent.setType("text/plain");
            startActivity(Intent.createChooser(shareIntent, "Share to"));
        } else if (id == R.id.nav_send) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            File file = new File(wifiFileName);
            Uri uri = Uri.fromFile(file);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setType("text/plain");
            startActivity(Intent.createChooser(shareIntent, "Send to"));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void InitView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTitleList = new ArrayList<String>();
        mTitleList.add(this.getString(R.string.title_current));
        mTitleList.add(this.getString(R.string.title_history));
        mTitleList.add(this.getString(R.string.title_all));

        mFragmentList = new ArrayList<Fragment>();
        List<WifiInfo> mTopList = new ArrayList<WifiInfo>();
        for(int i = 0; i < TOP_NUM; i ++) {
            mTopList.add(mWifiList.get(i));
        }
        mFragmentList.add(PageFragment.newInstace(0, mTopList));
        mFragmentList.add(PageFragment.newInstace(1, mWifiList));
        mFragmentList.add(PageFragment.newInstace(2, mWifiList));
//        mFragmentList.add(PlusOneFragment.newInstance("2","AD"));

        mTabLayout = (TabLayout) findViewById(R.id.page_tabs);
        mViewPager = (ViewPager) findViewById(R.id.page_container);

//        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)));
//        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(1)));
//        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(2)));

//        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                mViewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//
//        final TabLayout.TabLayoutOnPageChangeListener listener = new
//                TabLayout.TabLayoutOnPageChangeListener(mTabLayout);
//        mViewPager.addOnPageChangeListener(listener);

        FragmentManager fm = getSupportFragmentManager();
        myFragmentPageAdapter = new MyFragmentPageAdapter(fm, mFragmentList, mTitleList, mWifiList);
        mViewPager.setAdapter(myFragmentPageAdapter);
        mViewPager.setCurrentItem(0);

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(myFragmentPageAdapter);

        Log.i("wumin.y", "start fcb init");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                String info = List2String(mWifiList);
                shareIntent.putExtra(Intent.EXTRA_TEXT, info);
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent, "Share to"));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        Export2File(wifiFileName);
    }
    private void Export2File(final String fileName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File file = new File(fileName);
                    FileOutputStream fos = new FileOutputStream(file);
                    for(int i = 0; i < mWifiList.size(); i++) {
                        fos.write(mWifiList.get(i).toString().getBytes());
                        Log.i(TAG, mWifiList.get(i).toString());
                    }
                    fos.close();
                    Message msg = new Message();
                    msg.what = MSG_EXPORT;
                    mHandler.sendMessage(msg);
                } catch (FileNotFoundException e) {
                    Log.i(TAG, "no file");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.i(TAG,"no string");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private String List2String(List<WifiInfo> list) {
        String info = "";
        for (int i = 0; i < list.size(); i++) {
            info += list.get(i).toString();
        }
        return info;
    }
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REFRESH:
                    Toast.makeText(mContext, "Refresh success!", Toast.LENGTH_SHORT).show();
                    break;
                case MSG_EXPORT:
                    String info = "Export to " + wifiFileName + " success!";
                    Toast.makeText(mContext, info, Toast.LENGTH_SHORT).show();
                    break;

            }
            super.handleMessage(msg);
        }
    };

//    public static void showShare(Context context) {
//        ShareSDK.initSDK(context);
//        OnekeyShare oks = new OnekeyShare();
//        oks.disableSSOWhenAuthorize();
//        oks.setTitle("Sina weixin qq");
////        oks.setUrl("http://sharesdk.cn");
//        oks.setText("i'm shared info");
////        oks.setImageUrl();
////        oks.setImagePath();
////        oks.setComment("i'm test info");
////        oks.setSite("http://sharesdk.cn");
////        oks.setSiteUrl("http://sharesdk.cn");
//
//        oks.show(context);
//    }
}
