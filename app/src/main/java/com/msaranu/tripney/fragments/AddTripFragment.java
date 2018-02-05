package com.msaranu.tripney.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.msaranu.tripney.R;
import com.msaranu.tripney.models.Trip;


public class AddTripFragment extends DialogFragment {


    private EditText tripName;
    private EditText tripDate;
    private EditText tripDescripton;
    private EditText tripStatus;
    private Button save;
    Trip trip;

    public AddTripFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static AddTripFragment newInstance(String title) {
        AddTripFragment frag = new AddTripFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    public interface AddTripFragmentDialogListener {
        void onFinishEditDialog(Trip trip);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_trip, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        trip = new Trip();
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        tripName = (EditText) view.findViewById(R.id.etTripName);
        tripDescripton = (EditText) view.findViewById(R.id.etTripDescriptom);
        tripDate = (EditText) view.findViewById(R.id.etTripDate);
        tripStatus = (EditText) view.findViewById(R.id.etTripStatus);
        save = (Button) view.findViewById(R.id.btnTripSave);


        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        tripName.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trip.setmName(tripName.getText().toString());
                trip.setmDescription(tripDescripton.getText().toString());
                trip.setmDate(tripDate.getText().toString());
                trip.setmStatus(tripStatus.getText().toString());
                trip.saveInBackground();
                AddTripFragmentDialogListener addTripFragmentDialogListener =
                        (AddTripFragmentDialogListener) getActivity();
                addTripFragmentDialogListener.onFinishEditDialog(trip);
                dismiss();
            }
        });

    }
}

