package com.lynxsolutions.intern.sappi.cars;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.lynxsolutions.intern.sappi.R;

/**
 * Created by internkalmi on 31.07.2017.
 */

public class NavigationManager {


    private final FragmentManager manager;

    public NavigationManager(FragmentManager manager){
        this.manager = manager;
    }

    public void switchToFragment(Fragment fragment) {
        manager.beginTransaction().replace(R.id.content, fragment).commit();
    }
}