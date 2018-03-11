package com.msaranu.tripney.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.msaranu.tripney.R;
import com.msaranu.tripney.adapters.EventRecyclerAdapter;
import com.msaranu.tripney.databinding.FragmentTripThingsToDoBinding;
import com.msaranu.tripney.models.Event;
import com.msaranu.tripney.models.Trip;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link TripThingsToDoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TripThingsToDoFragment extends android.support.v4.app.Fragment implements
        AddEventFragment.AddEventFragmentDialogListener {

    public static final String TRIP_OBJECT = "trip_obj";
    public static final String WISH_LIST = "wish";

    boolean isWishList = false;

    Event event;
    Trip trip;
    ArrayList<Event> events;
    FragmentTripThingsToDoBinding fragmentTripThingsToDoBinding;
    EventRecyclerAdapter adapter;
    FloatingActionButton myFab;


    public TripThingsToDoFragment() {
        // Required empty public constructor
    }


    public static TripThingsToDoFragment newInstance(Trip trip, Boolean isWishList) {
        TripThingsToDoFragment tripDetailFragment = new TripThingsToDoFragment();
        Bundle args = new Bundle();
        args.putParcelable(TRIP_OBJECT, trip);
        args.putBoolean(WISH_LIST, isWishList);
        tripDetailFragment.setArguments(args);
        return tripDetailFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trip = getArguments().getParcelable(TRIP_OBJECT);
        isWishList =  getArguments().getBoolean(WISH_LIST);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentTripThingsToDoBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_trip_things_to_do, parent, false);
        View view = fragmentTripThingsToDoBinding.getRoot();
        ButterKnife.bind(this, view);
        setRecyclerView(view);
        //  fragmentTripThingsToDoBinding..setText(trip.getmDate());
        return view;
    }
    @Override
    public void onFinishEditDialog(Event event) {
        events.add(0,event);//insert at the beginning
        adapter.notifyDataSetChanged();
    }

    private void setFAB(View view) {
        myFab = (FloatingActionButton) view.findViewById(R.id.fabEvent);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                FragmentManager fm = getFragmentManager();
                AddEventFragment editNameDialogFragment = AddEventFragment.newInstance(trip, isWishList);
                editNameDialogFragment.setTargetFragment(TripThingsToDoFragment.this, 300);
                editNameDialogFragment.show(fm, "fragment_edit_name");

            }
        });
    }


    private void setRecyclerView(View view) {

        //recycler view
        RecyclerView rvEvents = (RecyclerView) view.findViewById(R.id.rvEvents);
        events = new ArrayList<Event>();

        // Initialize contacts
        //events = Event.createTempEvents(20);
        //TODO: pass tripID and get corresponding events
        // Create adapter passing in the sample user data
         adapter = new EventRecyclerAdapter(this.getContext(), events, TripThingsToDoFragment.this);
        // Attach the adapter to the recyclerview to populate items
        rvEvents.setAdapter(adapter);
        // Set layout manager to position the items
        rvEvents.setLayoutManager(new LinearLayoutManager(this.getContext()));


        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        String tripID = trip.tripID;
        query.whereEqualTo("tripID", tripID);
        if(isWishList)
        {
            query.whereEqualTo("isWish", "Y");
        }
        else {
            query.whereEqualTo("isWish", "");

        }
       query.orderByDescending("_created_at");


        // Execute the find asynchronously
        query.findInBackground(new FindCallback<Event>() {
            public void done(List<Event> itemList, ParseException e) {
                if (e == null) {
                    for(Event event : itemList){
                        events.add(event);
                    }
                    Toast.makeText(getContext(), "Message", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
            }
        });

        setFAB(view);
    }

}