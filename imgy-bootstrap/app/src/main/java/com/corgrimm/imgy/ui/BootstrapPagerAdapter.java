

package com.corgrimm.imgy.ui;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.corgrimm.imgy.R;

/**
 * Pager adapter
 */
public class BootstrapPagerAdapter extends FragmentPagerAdapter {

    private final Resources resources;

    /**
     * Create pager adapter
     *
     * @param resources
     * @param fragmentManager
     */
    public BootstrapPagerAdapter(Resources resources, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.resources = resources;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        switch (position) {
        case 1:
            ImageListFragment newsFragment = new ImageListFragment();
            newsFragment.setArguments(bundle);
            return newsFragment;
        case 0:
            UserListFragment userListFragment = new UserListFragment();
            userListFragment.setArguments(bundle);
            return userListFragment;
        case 2:
            CheckInsListFragment checkInsFragment = new CheckInsListFragment();
            checkInsFragment.setArguments(bundle);
            return checkInsFragment;
        default:
            return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
        case 0:
            return resources.getString(R.string.page_news);
        case 1:
            return resources.getString(R.string.page_users);
        case 2:
            return resources.getString(R.string.page_checkins);
        default:
            return null;
        }
    }
}
