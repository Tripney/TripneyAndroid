package com.msaranu.tripney.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.astuetz.PagerSlidingTabStrip;
import com.msaranu.tripney.R;
import com.msaranu.tripney.adapters.TripDetailPagerAdapter;
import com.msaranu.tripney.models.Trip;

public class DetailTripActivity extends AppCompatActivity {
    FragmentPagerAdapter adapterViewPager;
    Trip trip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_trip);
        Bundle extras = getIntent().getExtras();
        trip = extras.getParcelable("trip_obj");
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new TripDetailPagerAdapter(getSupportFragmentManager(), trip));

        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);


    }



}

