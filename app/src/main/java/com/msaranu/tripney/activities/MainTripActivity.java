package com.msaranu.tripney.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.msaranu.tripney.R;
import com.msaranu.tripney.adapters.TripRecyclerAdapter;
import com.msaranu.tripney.fragments.AddNewFriendsFragment;
import com.msaranu.tripney.fragments.AddTripFragment;
import com.msaranu.tripney.models.Trip;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MainTripActivity extends AppCompatActivity implements AddTripFragment.AddTripFragmentDialogListener {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private ImageView navHeaderProfileImage;
    FloatingActionButton myFab;
    TextView tvNavUserName;
    ArrayList<Trip> trips;
    TripRecyclerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_trip);

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.tbTripney);
        setSupportActionBar(toolbar);


        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        nvDrawer = (NavigationView) findViewById(R.id.nvTrips);

        View headerView = nvDrawer.inflateHeaderView(R.layout.navigation_drawer_header);
        navHeaderProfileImage = (ImageView) headerView.findViewById(R.id.ivProfileImage);
        tvNavUserName = (TextView) headerView.findViewById(R.id.tvUserName);

        // Setup drawer view
        setupDrawerContent(nvDrawer);


        // Find our drawer view
        drawerToggle = setupDrawerToggle();
        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);
        setRecyclerView();
        setFAB();

    }

    private void setFAB() {
        myFab = (FloatingActionButton) findViewById(R.id.fabTrip);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                AddTripFragment editNameDialogFragment = AddTripFragment.newInstance("Add New Trip");
                editNameDialogFragment.show(fm, "fragment_edit_name");
            }
        });
    }

    @Override
    public void onFinishEditDialog(Trip trip) {
        trips.add(trip);
        adapter.notifyDataSetChanged();
    }



    private void setRecyclerView() {

        trips = new ArrayList<Trip>();
        // Initialize contacts
      //  trips = Trip.createTempTrips(20);

       // trips.add(new Trip("300", "Yellow Stone"," It is awesome", "01-01-2010", "NEW", "blahhhhhh URL"));

        //recycler view
        RecyclerView rvTrips = (RecyclerView) findViewById(R.id.rvTrips);

        adapter = new TripRecyclerAdapter(this, trips);
        rvTrips.setAdapter(adapter);
        rvTrips.setLayoutManager(new LinearLayoutManager(this));



        ParseQuery<Trip> query = ParseQuery.getQuery(Trip.class);
        query.whereEqualTo("mUserID", ParseUser.getCurrentUser().getObjectId());
        // Execute the find asynchronously
        query.findInBackground(new FindCallback<Trip>() {
            public void done(List<Trip> itemList, ParseException e) {
                if (e == null) {
                    for(Trip trip : itemList){
                        trips.add(trip);
                    }
                    Toast.makeText(MainTripActivity.this, "Message", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
            }
        });
    }


    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        Intent i;
        switch(menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                i = new Intent(this, MainTripActivity.class);
                break;
            case R.id.nav_second_fragment:
                i = new Intent(this, UserProfileActivity.class);
                break;
            case R.id.nav_third_fragment:
                i = new Intent(this, AddNewFriendsActivity.class);
                break;
            default:
                i = new Intent(this, MainTripActivity.class);
        }

        try {
            this.startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}