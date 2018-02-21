package com.msaranu.tripney.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.msaranu.tripney.R;
import com.msaranu.tripney.fragments.TripDetailFragment;
import com.msaranu.tripney.fragments.TripThingsToDoFragment;
import com.msaranu.tripney.fragments.TripWishListFragment;
import com.msaranu.tripney.models.Trip;

public class DetailTripActivity extends AppCompatActivity {
    FragmentPagerAdapter adapterViewPager;
    Trip trip;
    int itemId;


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // itemId=R.id.action_trip_detail;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_trip);
        Bundle extras = getIntent().getExtras();
        final FragmentManager fragmentManager = getSupportFragmentManager();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        trip = extras.getParcelable("trip_obj");
       // if(extras.getParcelable("item_id") !=null){
       //     itemId  = extras.getInt("item_id");
       // }
       // bottomNavigationView.setSelectedItemId(itemId);


        //Default Load
        final FragmentTransaction fragmentTripDetail = fragmentManager.beginTransaction();
        fragmentTripDetail.replace(R.id.flContainer, TripDetailFragment.newInstance(trip)).commit();

        // handle navigation selection
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_trip_detail:
                                final FragmentTransaction fragmentTripDetail = fragmentManager.beginTransaction();
                                fragmentTripDetail.replace(R.id.flContainer, TripDetailFragment.newInstance(trip)).commit();
                                return true;
                            case R.id.action_trip_events:
                                final FragmentTransaction fragmentTripEvents = fragmentManager.beginTransaction();
                                fragmentTripEvents.replace(R.id.flContainer, TripThingsToDoFragment.newInstance(trip)).commit();
                                return true;
                            case R.id.action_wish_list:
                                final FragmentTransaction fragmentWishList = fragmentManager.beginTransaction();
                                fragmentWishList.replace(R.id.flContainer, TripWishListFragment.newInstance(trip)).commit();
                                return true;

                            case R.id.action_expenses:
                                final FragmentTransaction fragmentExpenses= fragmentManager.beginTransaction();
                                fragmentExpenses.replace(R.id.flContainer, TripWishListFragment.newInstance(trip)).commit();
                                return true;

                            default:
                                return true;

                        }
                    }
                }
        );

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        //ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        //viewPager.setAdapter(new TripDetailPagerAdapter(getSupportFragmentManager(), trip));

        // Give the PagerSlidingTabStrip the ViewPager
        //PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        //tabsStrip.setViewPager(viewPager);


    }

}

