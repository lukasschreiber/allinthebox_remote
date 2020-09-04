package com.example.allinthebox_remote.ui.main;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.allinthebox_remote.MainActivity;
import com.example.allinthebox_remote.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_3, R.string.tab_text_2};
    private final Context mContext;
    private MainActivity mainActivity;

    public SectionsPagerAdapter(Context context, FragmentManager fm, MainActivity m) {
        super(fm);
        mContext = context;
        mainActivity = m;
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch(position){
            case 0: fragment = mainActivity.barcodeFragment;break;
            case 1: fragment = mainActivity.contentFragment;break;
            case 2: fragment = mainActivity.cameraFragment;break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }
}