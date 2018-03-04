package com.msaranu.tripney.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.msaranu.tripney.R;
import com.msaranu.tripney.models.Trip;
import com.msaranu.tripney.models.User;
import com.msaranu.tripney.utilities.DateUtils;

import java.util.List;

public class SplitExpensesDialogFragment extends DialogFragment {

    Double amount;
    String eventID;
    TextView tvPaidBy;
    Button btnEqual;
    Button btnAmount;
    Button btnPercentage;


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

        btnEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Default Load
                getChildFragmentManager().beginTransaction().replace(R.id.flContainer,  SplitEqualFragment.newInstance(eventID, amount)).commit();
            }
        });

        btnAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    //Default Load
                    getChildFragmentManager().beginTransaction().replace(R.id.flContainer, new SplitByAmountFragment()).commit();
                }
        });

        btnPercentage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    //Default Load
                    getChildFragmentManager().beginTransaction().replace(R.id.flContainer, new SplitByPercentageFragment()).commit();
                }
        });

    }

}
