package com.msaranu.tripney.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.msaranu.tripney.fragments.TripDetailFragment;
import com.msaranu.tripney.fragments.TripExpensesFragment;
import com.msaranu.tripney.fragments.TripThingsToDoFragment;
import com.msaranu.tripney.fragments.TripWishListFragment;
import com.msaranu.tripney.models.Trip;

/**
 * Created by msaranu on 1/14/18.
 */


public class TripDetailPagerAdapter extends FragmentPagerAdapter {
    private String tabTitles[] = {"DETAILS", "THINGS TO DO","WISH LIST","EXPENSES"};
    Trip trip;

    public TripDetailPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public TripDetailPagerAdapter(FragmentManager fragmentManager, Trip trip) {
        super(fragmentManager);
        this.trip=trip;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return tabTitles.length;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return TripDetailFragment.newInstance(trip);
            case 1: // Fragment # 1 - This will show SecondFragment
                return TripThingsToDoFragment.newInstance(trip, true);
            case 2: // Fragment # 0 - This will show FirstFragment different title
                return TripWishListFragment.newInstance(trip);
            case 3: // Fragment # 0 - This will show FirstFragment different title
                return TripExpensesFragment.newInstance(trip);
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

}
