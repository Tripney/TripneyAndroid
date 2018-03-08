package com.msaranu.tripney.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.msaranu.tripney.R;
import com.msaranu.tripney.models.Split;
import com.msaranu.tripney.models.Trip;
import com.msaranu.tripney.models.User;
import com.msaranu.tripney.utilities.DateUtils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class SplitExpensesDialogFragment extends DialogFragment {

    Double amount;
    String eventID;
    TextView tvPaidBy;
    Button btnEqual;
    Button btnAmount;
    Button btnPercentage;
    ArrayList<Split> splitAmounts;


    public SplitExpensesDialogFragment() {
        // Required empty public constructor
    }


    public static SplitExpensesDialogFragment newInstance(String eventID, Double price) {
        SplitExpensesDialogFragment fragment = new SplitExpensesDialogFragment();
        Bundle args = new Bundle();
        args.putDouble("price", price);
        args.putString("eventID", eventID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_split_expenses_dialog, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        splitAmounts = new ArrayList<Split>();
        if (getArguments() != null) {
            amount = getArguments().getDouble("price");
            eventID = getArguments().getString("eventID");

        }

        tvPaidBy = (TextView) view.findViewById(R.id.tvPaidBy);
        btnEqual = (Button) view.findViewById(R.id.btnEqual);
        btnAmount = (Button) view.findViewById(R.id.btnAmount);
        btnPercentage = (Button) view.findViewById(R.id.btnPercentage);

        tvPaidBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Default Load
                FragmentManager fm = getFragmentManager();
                PaidByDialogFragment paidByDialogFragment = PaidByDialogFragment.newInstance(amount);
                paidByDialogFragment.setTargetFragment(SplitExpensesDialogFragment.this, 300);
                paidByDialogFragment.show(fm, "fragment_split");
            }
        });

        if(splitAmounts == null && splitAmounts.size() >0 ){
            if(splitAmounts.get(0).getType() == "P"){
                getChildFragmentManager().beginTransaction().replace(R.id.flContainer, SplitByPercentageFragment.newInstance(eventID, amount, getSplits())).commit();

            }else  if(splitAmounts.get(0).getType() == "A"){
                getChildFragmentManager().beginTransaction().replace(R.id.flContainer, SplitByAmountFragment.newInstance(eventID, amount, getSplits())).commit();

            }else  if(splitAmounts.get(0).getType() == "E"){
                getChildFragmentManager().beginTransaction().replace(R.id.flContainer, SplitEqualFragment.newInstance(eventID, amount, getSplits())).commit();
            }
        }else{
            getChildFragmentManager().beginTransaction().replace(R.id.flContainer, SplitEqualFragment.newInstance(eventID, amount, getSplits())).commit();
        }

        btnEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Default Load
                getChildFragmentManager().beginTransaction().replace(R.id.flContainer, SplitEqualFragment.newInstance(eventID, amount, getSplits())).commit();
            }
        });

        btnAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Default Load
                getChildFragmentManager().beginTransaction().replace(R.id.flContainer, SplitByAmountFragment.newInstance(eventID, amount, getSplits())).commit();
            }
        });

        btnPercentage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Default Load
                getChildFragmentManager().beginTransaction().replace(R.id.flContainer, SplitByPercentageFragment.newInstance(eventID, amount, getSplits())).commit();
            }
        });

    }

    private ArrayList<Split> getSplits() {

        ParseQuery<Split> Splitquery = ParseQuery.getQuery(Split.class);
        Splitquery.whereEqualTo("eventID", eventID);


        // Execute the find asynchronously
        Splitquery.findInBackground(new FindCallback<Split>() {
            public void done(List<Split> itemList, ParseException e) {
                if (e == null) {
                    for (Split split : itemList) {
                        splitAmounts.add(split);
                    }
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
            }
        });
        return splitAmounts;
    }

}
