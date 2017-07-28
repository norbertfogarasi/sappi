package com.lynxsolutions.intern.sappi;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by internkalmi on 26.07.2017.
 */

class SectionPagerAdapter extends FragmentPagerAdapter {
    public SectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CarFeedFragment();
            case 1:
                return new NewsFeedFragment();
            case 2:
                return new EventFeedFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position){
        switch (position) {
            case 0:
                return "Car";
            case 1:
                return "News";
            case 2:
                return "Events";
            default:
                return null;
        }
    }
}
