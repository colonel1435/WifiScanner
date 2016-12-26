package com.wumin.wifiscaner;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by samsung-pc on 2016/9/16.
 */
public class MyFragmentPageAdapter extends FragmentPagerAdapter {
    private List<String> mTitleList;
    private List<Fragment> mFragmentList;
    private List<WifiInfo> mWifiList;
    private List<String> mTagList;

    public MyFragmentPageAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titleList, List<WifiInfo> wifiList) {
        super(fm);
        this.mFragmentList = fragmentList;
        this.mTitleList = titleList;
        this.mWifiList = wifiList;
    }

//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        mTagList.add(makeFragmentName(container.getId(), getItemId(position)));
//        return super.instantiateItem(container, position);
//    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }
//    public Fragment getItem(int position) {
//        return PageFragment.newInstace(position+1, mWifiList);
//    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);
    }
}
