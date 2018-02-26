package com.msaranu.tripney.activities;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.msaranu.tripney.R;
import com.msaranu.tripney.fragments.AddNewFriendsFragment;
import com.msaranu.tripney.fragments.UserProfileFragment;

public class AddNewFriendsActivity extends AppCompatActivity {

    AddNewFriendsFragment addNewFriendsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator
                (ContextCompat.getDrawable(AddNewFriendsActivity.this, R.drawable.ic_arrow_back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowTitleEnabled(false);


        addNewFriendsFragment =  AddNewFriendsFragment.newInstance(null);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentAddNewFriends, addNewFriendsFragment);
        ft.commit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchItem.expandActionView();
        searchView.requestFocus();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                addNewFriendsFragment =  AddNewFriendsFragment.newInstance(query);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragmentAddNewFriends, addNewFriendsFragment);
                ft.commit();
                searchView.clearFocus();
                return true;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                addNewFriendsFragment =  AddNewFriendsFragment.newInstance(newText);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragmentAddNewFriends, addNewFriendsFragment);
                ft.commit();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
