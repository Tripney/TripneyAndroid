package com.msaranu.tripney.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.msaranu.tripney.R;
import com.msaranu.tripney.activities.MainTripActivity;
import com.msaranu.tripney.adapters.SearchAddFriendsAdapter;
import com.msaranu.tripney.databinding.FragmentAddNewFriendsBinding;
import com.msaranu.tripney.models.Event;
import com.msaranu.tripney.models.Trip;
import com.msaranu.tripney.models.User;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewFriendsFragment extends Fragment {

    private static final String SEARCH_STRING = "search_string";
    FragmentAddNewFriendsBinding fragmentAddNewFriends;
    ArrayList<ParseUser> people;
    SearchAddFriendsAdapter adapter;
    String search_key;



    public AddNewFriendsFragment() {
        // Required empty public constructor
    }



    public static AddNewFriendsFragment newInstance(String searchStr) {
        AddNewFriendsFragment addNewFriendsFragment = new AddNewFriendsFragment();
        Bundle args = new Bundle();
        args.putString(SEARCH_STRING, searchStr);
        addNewFriendsFragment.setArguments(args);
        return addNewFriendsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SEARCH_STRING != null)
        search_key = getArguments().getString(SEARCH_STRING);
    }

   /* Inflate search action bar in fragments
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = new SearchView(((MainTripActivity) getContext()).getSupportActionBar().getThemedContext());
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setActionView(searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {

                                          }
                                      }
        );
    }*/



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentAddNewFriends = DataBindingUtil.inflate(inflater, R.layout.fragment_add_new_friends, parent, false);
        View view = fragmentAddNewFriends.getRoot();
        ButterKnife.bind(this, view);
        setRecyclerView(view);
        //  fragmentAddNewFriends..setText(trip.getmDate());
        return view;
    }

    private void setRecyclerView(View view) {

        //recycler view
        RecyclerView rvPeople = (RecyclerView) view.findViewById(R.id.rvPeople);
        people = new ArrayList<ParseUser>();
        List<ParseQuery<ParseUser>> parseQueries;

        // Initialize contacts
        //events = Event.createTempEvents(20);
        //TODO: pass tripID and get corresponding events
        // Create adapter passing in the sample user data
        adapter = new SearchAddFriendsAdapter(this.getContext(), people, AddNewFriendsFragment.this);
        // Attach the adapter to the recyclerview to populate items
        rvPeople.setAdapter(adapter);
        // Set layout manager to position the items
        rvPeople.setLayoutManager(new LinearLayoutManager(this.getContext()));

        ParseQuery<ParseUser> query = ParseUser.getQuery();

        if(search_key == null) {
            query.whereNotEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
        }else{
            parseQueries = new ArrayList<ParseQuery<ParseUser>>();

           /* ParseQuery<ParseUser>  pQuery1 = ParseUser.getQuery();
            parseQueries.add(pQuery1.whereContains("firstName", search_key));
            ParseQuery<ParseUser>  pQuery2 = ParseUser.getQuery();
            parseQueries.add(pQuery2.whereContains("lastName", search_key));
            ParseQuery<ParseUser>  pQuery3 = ParseUser.getQuery();
            parseQueries.add(pQuery3.whereContains("username", search_key));*/

           //  parseQueries.add( query.whereContains("firstName", search_key));
            //parseQueries.add( query.whereContains("lastName",  search_key));
            parseQueries.add( query.whereContains("username", search_key));
            query.whereNotEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
            //query.or(parseQueries);
        }



        // Execute the find asynchronously
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> itemList, ParseException e) {
                if (e == null) {
                    for(ParseUser user : itemList){
                        people.add(user);
                    }
//                    Toast.makeText(getContext(), "Message", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
            }
        });

    }


}
