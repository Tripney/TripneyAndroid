package com.msaranu.tripney.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.msaranu.tripney.R;
import com.msaranu.tripney.models.Split;
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
    String type;


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


        getSplits(null, false);

        btnEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Default Load
                getSplits("E",true);
            }
        });

        btnAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Default Load
                getSplits("A", true);
            }
        });

        btnPercentage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Default Load
                getSplits("P", true);
            }
        });

    }

    private void getSplits(String splitType, boolean forcePress) {

        ParseQuery<Split> Splitquery = ParseQuery.getQuery(Split.class);
        Splitquery.whereEqualTo("eventID", eventID);
        splitAmounts = new ArrayList<Split>();
         type =splitType;

        // Execute the find asynchronously
        Splitquery.findInBackground(new FindCallback<Split>() {
            public void done(List<Split> itemList, ParseException e) {
                if (e == null) {
                    for (Split split : itemList) {
                        split.loadInstanceVariables();
                        splitAmounts.add(split);
                    }
                    if(!forcePress){
                        if(splitAmounts != null && splitAmounts.size() > 0){
                            type = splitAmounts.get(0).type;
                        } else{
                            type ="E";
                        }
                    }
                        if (type.equals("E")) {
                            getChildFragmentManager().beginTransaction().replace(R.id.flContainer, SplitEqualFragment.newInstance(eventID, amount, splitAmounts)).commit();
                        } else if (type.equals("P")) {
                            getChildFragmentManager().beginTransaction().replace(R.id.flContainer, SplitByPercentageFragment.newInstance(eventID, amount, splitAmounts)).commit();
                        } else if (type.equals("A")) {
                            getChildFragmentManager().beginTransaction().replace(R.id.flContainer, SplitByAmountFragment.newInstance(eventID, amount, splitAmounts)).commit();
                        }

                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
            }
        });
    }

}
