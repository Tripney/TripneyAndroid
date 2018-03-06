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
import com.msaranu.tripney.adapters.SplitRecyclerAdapter;
import com.msaranu.tripney.adapters.SplitRecyclerAdapter;
import com.msaranu.tripney.databinding.FragmentSplitEqualBinding;
import com.msaranu.tripney.databinding.FragmentTripThingsToDoBinding;
import com.msaranu.tripney.models.Event;
import com.msaranu.tripney.models.Split;
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
 * Use the {@link SplitEqualFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SplitEqualFragment extends android.support.v4.app.Fragment  {

    public static final String EVENT_ID = "event_id";
    public static final String AMOUNT = "amount";


    String eventID;
    Trip trip;
    ArrayList<Split> splits;
    FragmentSplitEqualBinding fragmentSplitEqualBinding;
    SplitRecyclerAdapter adapter;
    Double amount;

    public SplitEqualFragment() {
        // Required empty public constructor
    }


    public static SplitEqualFragment newInstance(String eventID, Double amount) {
        SplitEqualFragment tripDetailFragment = new SplitEqualFragment();
        Bundle args = new Bundle();
        args.putString(EVENT_ID, eventID);
        args.putDouble(AMOUNT, amount);
        tripDetailFragment.setArguments(args);
        return tripDetailFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventID = getArguments().getString(EVENT_ID);
        amount = getArguments().getDouble(AMOUNT);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentSplitEqualBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_split_equal, parent, false);
        View view = fragmentSplitEqualBinding.getRoot();
        ButterKnife.bind(this, view);
        setRecyclerView(view);
        return view;
    }


    private void setRecyclerView(View view) {

        //recycler view
        RecyclerView rvSplits = (RecyclerView) view.findViewById(R.id.rvSplits);
        splits = new ArrayList<Split>();

        // Initialize contacts
        //events = Split.createTempSplits(20);
        //TODO: pass tripID and get corresponding events
        // Create adapter passing in the sample user data
        adapter = new SplitRecyclerAdapter(this.getContext(), splits, amount);
        // Attach the adapter to the recyclerview to populate items
        rvSplits.setAdapter(adapter);
        // Set layout manager to position the items
        rvSplits.setLayoutManager(new LinearLayoutManager(this.getContext()));


        ParseQuery<Split> query = ParseQuery.getQuery(Split.class);
        query.whereEqualTo("eventID", eventID);


        // Execute the find asynchronously
        query.findInBackground(new FindCallback<Split>() {
            public void done(List<Split> itemList, ParseException e) {
                if (e == null) {
                    for(Split split : itemList){
                        splits.add(split);
                    }
                    Toast.makeText(getContext(), "Message", Toast.LENGTH_SHORT).show();
                    if(splits == null) {
                        //TODO Add New splits
                        Split split = new Split();
                    }
                    adapter.notifyDataSetChanged();

                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
            }
        });

    }

}