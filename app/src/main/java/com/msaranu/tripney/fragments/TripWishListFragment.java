package com.msaranu.tripney.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.msaranu.tripney.R;
import com.msaranu.tripney.adapters.EventRecyclerAdapter;
import com.msaranu.tripney.databinding.FragmentTripThingsToDoBinding;
import com.msaranu.tripney.models.Event;
import com.msaranu.tripney.models.Trip;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class TripWishListFragment extends android.support.v4.app.Fragment {

    public static final String TRIP_OBJECT = "trip_obj";
    Event event;
    Trip trip;
    ArrayList<Event> events;
    FragmentTripThingsToDoBinding fragmentTripThingsToDoBinding;

    public TripWishListFragment() {
        // Required empty public constructor
    }


    public static TripWishListFragment newInstance(Trip trip) {
        TripWishListFragment tripWishListFragment = new TripWishListFragment();
        Bundle args = new Bundle();
        args.putParcelable(TRIP_OBJECT, trip);
        tripWishListFragment.setArguments(args);
        return tripWishListFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trip = getArguments().getParcelable(TRIP_OBJECT);
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


    private void setRecyclerView(View view) {

        //recycler view
        RecyclerView rvEvents = (RecyclerView) view.findViewById(R.id.rvEvents);

        // Initialize contacts
        events = Event.createTempEvents(20);
        //TODO: pass tripID and get corresponding events
        // Create adapter passing in the sample user data
        EventRecyclerAdapter adapter = new EventRecyclerAdapter(this.getContext(), events);
        // Attach the adapter to the recyclerview to populate items
        rvEvents.setAdapter(adapter);
        // Set layout manager to position the items
        rvEvents.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

}