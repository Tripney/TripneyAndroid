package com.msaranu.tripney.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.msaranu.tripney.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SplitByPercentageFragment extends Fragment {


    public SplitByPercentageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_split_by_percentage, container, false);
    }

}
