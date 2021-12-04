package com.example.dark.prod_family_project.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abd on 18-Jan-18.
 */

public class TabAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> mfragmenttitlelist = new ArrayList<>();

    public void addfragment(Fragment fragment,String title){

        fragmentList.add(fragment);
        mfragmenttitlelist.add(title);

    }

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mfragmenttitlelist.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
