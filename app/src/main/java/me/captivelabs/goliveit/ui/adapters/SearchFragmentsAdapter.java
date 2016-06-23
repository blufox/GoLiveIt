package me.captivelabs.goliveit.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import me.captivelabs.goliveit.ui.fragments.SearchArtistFragment;
import me.captivelabs.goliveit.ui.fragments.SearchEventFragment;
import me.captivelabs.goliveit.ui.fragments.SearchVenueFragment;

/**
 * Created by Aron on 11/5/2015.
 */
public class SearchFragmentsAdapter extends FragmentPagerAdapter {
    public SearchFragmentsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return new SearchArtistFragment();
        } else if (position == 1) {
            return new SearchEventFragment();
        } else if (position == 2) {
            return new SearchVenueFragment();
        } else {
            return new SearchArtistFragment();
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "ARTISTS";
        } else if (position == 1) {
            return "EVENTS";
        } else if (position == 2) {
            return "VENUES";
        } else {
            return null;
        }
    }
}
