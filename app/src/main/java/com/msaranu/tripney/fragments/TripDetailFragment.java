package com.msaranu.tripney.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.msaranu.tripney.R;
import com.msaranu.tripney.databinding.FragmentDetailTripBinding;
import com.msaranu.tripney.models.Trip;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link TripDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TripDetailFragment extends android.support.v4.app.Fragment {

    public static final String TRIP_OBJECT = "trip_obj";
    Trip trip;
    FragmentDetailTripBinding fragmentDetailTripBinding;

    public TripDetailFragment() {
        // Required empty public constructor
    }


    public static TripDetailFragment newInstance(Trip trip) {
        TripDetailFragment tripDetailFragment = new TripDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(TRIP_OBJECT, trip);
        tripDetailFragment.setArguments(args);
        return tripDetailFragment;
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
        fragmentDetailTripBinding =  DataBindingUtil.inflate(inflater,R.layout.fragment_detail_trip, parent, false);
        View view = fragmentDetailTripBinding.getRoot();
        ButterKnife.bind(this, view);
        fragmentDetailTripBinding.tvTripDate.setText(trip.getmDate());
        fragmentDetailTripBinding.tvTripDescription.setText(trip.getmDescription());
        fragmentDetailTripBinding.tvTripName.setText(trip.getmName());
        fragmentDetailTripBinding.tvTripStatus.setText(trip.getmStatus());

        Glide.with(this).load(R.drawable.eventimage)
                .fitCenter()
                .into(fragmentDetailTripBinding.ivTripBckgrndImage);
        return view;
    }


}
