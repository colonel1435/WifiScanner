package com.wumin.wifiscaner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity {
    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 3000;
    private ImageView splashImg = null;
    private List<WifiInfo> mWifiList = null;
    private String TAG = "wumin.y.SplashActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initView();
    }

    private void initView() {
        splashImg = (ImageView) this.findViewById(R.id.splash_image);
        AlphaAnimation animation = new AlphaAnimation(0.1f,1.0f);
        animation.setDuration(UI_ANIMATION_DELAY);
        splashImg.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.i(TAG, "Start Animation");
                splashImg.setBackgroundResource(R.mipmap.icon_splash);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                skipWelcome();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void skipWelcome() {
        Log.i(TAG, "Start ManiActivity");
        loadWifiInfo();
        Intent startIntent = new Intent(this, MainActivity.class);
        Bundle wifiArgs = new Bundle();
        List<WifiInfo> list = new ArrayList<WifiInfo>();
        Collections.reverse(mWifiList);
        for(int i = 0; i < mWifiList.size(); i++) {
            list.add(mWifiList.get(i));
        }
        wifiArgs.putSerializable(PageFragment.ARGS_WIFI,(Serializable)list);
        startIntent.putExtra(PageFragment.ARGS_WIFI, wifiArgs);
        startActivity(startIntent);
        finish();
    }

    private int loadWifiInfo() {
        Log.i(TAG, "Start loadWifiInfo");
        try {
            mWifiList = WifiManage.Read();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mWifiList != null)
            return 0;
        else
            return -1;
    }

    private class MyTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            Log.i(TAG, "Start loadWifiInfo");
            int result;
            result = loadWifiInfo();
            return result;
        }
    }

}
