package me.captivelabs.goliveit.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import me.captivelabs.goliveit.ui.fragments.TabAllEventsFragment;
import me.captivelabs.goliveit.ui.fragments.TabArtistsFragment;
import me.captivelabs.goliveit.ui.fragments.TabFavoritesFragment;
import me.captivelabs.goliveit.ui.fragments.TabPopularFragment;


/**
 * Created by Aron on 7/9/2015.
 */
public class MainFragmentsAdapter extends FragmentPagerAdapter {
    public MainFragmentsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return new TabArtistsFragment();
        }else if (position == 1) {
            return new TabPopularFragment();
        }else if (position == 2) {
            return new TabAllEventsFragment();
        }else if (position == 3) {
            return new TabFavoritesFragment();
        }else{
            return new TabAllEventsFragment();
        }

    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "MY ARTISTS" ;
        } else if (position == 1) {
            return "POPULAR";
        } else if (position == 2) {
            return "ALL EVENTS";
        }else if (position == 3) {
            return "FAVORITES";
        } else {
            return null;
        }
    }
}
