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

public class PaidByDialogFragment extends DialogFragment {

    Double amount;
    TextView tvPaidBy;
    Button btnEqual;
    Button btnAmount;
    Button btnPercentage;


    public PaidByDialogFragment() {
        // Required empty public constructor
    }


    public static PaidByDialogFragment newInstance(Double price) {
        PaidByDialogFragment fragment = new PaidByDialogFragment();
        Bundle args = new Bundle();
        args.putDouble("price", price);
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
        }

        tvPaidBy = (TextView) view.findViewById(R.id.tvPaidBy);
        btnEqual = (Button) view.findViewById(R.id.btnEqual);
        btnAmount = (Button) view.findViewById(R.id.btnAmount);
        btnPercentage = (Button) view.findViewById(R.id.btnPercentage);



        btnEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Default Load
                getChildFragmentManager().beginTransaction().replace(R.id.flContainer, new SplitEqualFragment()).commit();
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
