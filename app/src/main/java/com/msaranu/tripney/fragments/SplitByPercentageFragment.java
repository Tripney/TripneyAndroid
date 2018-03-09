package com.msaranu.tripney.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.msaranu.tripney.R;
import com.msaranu.tripney.adapters.SplitRecyclerAdapter;
import com.msaranu.tripney.databinding.FragmentSplitByPercentageBinding;
import com.msaranu.tripney.models.EventUser;
import com.msaranu.tripney.models.Split;
import com.msaranu.tripney.models.Trip;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SplitByPercentageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SplitByPercentageFragment extends android.support.v4.app.Fragment  {

    public static final String EVENT_ID = "event_id";
    public static final String AMOUNT = "amount";


    String eventID;
    Trip trip;
    ArrayList<Split> splits;
    FragmentSplitByPercentageBinding fragmentBinding;
    SplitRecyclerAdapter adapter;
    Double amount;

    public SplitByPercentageFragment() {
        // Required empty public constructor
    }


    public static SplitByPercentageFragment newInstance(String eventID, Double amount, ArrayList<Split> splits) {
        SplitByPercentageFragment splitByPercentageFragment = new SplitByPercentageFragment();
        Bundle args = new Bundle();
        args.putString(EVENT_ID, eventID);
        args.putDouble(AMOUNT, amount);
        args.putParcelableArrayList("SPLIT_OBJ", splits);

        splitByPercentageFragment.setArguments(args);
        return splitByPercentageFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventID = getArguments().getString(EVENT_ID);
        amount = getArguments().getDouble(AMOUNT);
        splits = getArguments().getParcelableArrayList("SPLIT_OBJ");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_split_by_percentage, parent, false);
        View view = fragmentBinding.getRoot();
        ButterKnife.bind(this, view);
        setRecyclerView(view);
        fragmentBinding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double total=0;

                for(Split split: splits) {
                    total += split.amount;
                }
                if(total != amount){
                    Toast.makeText(getContext(), "Total does not match"
                            ,Toast.LENGTH_SHORT).show();
                    return;
                }

                for(Split split: splits){
                    ParseQuery<Split> splitQuery = ParseQuery.getQuery(Split.class);
                    splitQuery.whereEqualTo("objectId", split.splitID);
                    // Execute the find asynchronously
                    splitQuery.findInBackground(new FindCallback<Split>() {
                        public void done(List<Split> itemList, ParseException e) {
                            if (e == null) {
                                if(itemList.size() ==0 ){
                                    split.saveInBackground();
                                }else{
                                    ParseQuery<Split> splitQuery = ParseQuery.getQuery(Split.class);
                                    splitQuery.getInBackground(split.splitID, new GetCallback<Split>() {
                                        public void done(Split splitObj, ParseException e) {
                                            if (e == null) {
                                                splitObj.put("amount", split.amount);
                                                splitObj.put("type", split.type);
                                                splitObj.saveInBackground();
                                            }
                                        }
                                    });
                                }

                            } else {
                                Log.d("item", "Error: " + e.getMessage());
                            }
                        }
                    });
                    split.saveInBackground();
                }
            }
        });


        return view;
    }


    private void setRecyclerView(View view) {

        //recycler view
        RecyclerView rvSplits = (RecyclerView) view.findViewById(R.id.rvSplits);

        // Initialize contacts
        //events = Split.createTempSplits(20);
        //TODO: pass tripID and get corresponding events
        // Create adapter passing in the sample user data
        adapter = new SplitRecyclerAdapter(this.getContext(), splits, amount, "PERCENTAGE");
        // Attach the adapter to the recyclerview to populate items
        rvSplits.setAdapter(adapter);
        // Set layout manager to position the items
        rvSplits.setLayoutManager(new LinearLayoutManager(this.getContext()));

    }

}