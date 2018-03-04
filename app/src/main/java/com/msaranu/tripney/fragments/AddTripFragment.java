package com.msaranu.tripney.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.msaranu.tripney.R;
import com.msaranu.tripney.models.Trip;
import com.msaranu.tripney.models.User;
import com.msaranu.tripney.utilities.DateUtils;
import com.parse.ParseUser;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;


public class AddTripFragment extends DialogFragment
 implements AddFriendsDialogFragment.AddFriendsFragmentDialogListener,
        CalendarDatePickerDialogFragment.OnDateSetListener,  RadialTimePickerDialogFragment.OnTimeSetListener {



private EditText tripName;
    private EditText tripDate;
    private EditText tripDescripton;
    private EditText tripLocation;
    private ImageButton tripAddFriends;
    private Spinner spinnerTrip;
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

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        tripDate.setText(monthOfYear+1 +"/"+dayOfMonth+ "/" +year);
        callTimePicker();
    }

    private void callTimePicker() {
        RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                .setOnTimeSetListener(AddTripFragment.this)
                .setTitleText(TimeZone.getDefault().getDisplayName())
                .setStartTime(10, 10)
                .setDoneText("Done")
                .setCancelText("Cancel");
        rtpd.show(getFragmentManager(), "Pick Time");
    }


    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        tripDate.setText(tripDate.getText().toString() + " " + hourOfDay + ":" + minute );
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();

    }

    @Override
    public void onFinishAddFriendsDialog() {
        //TODO Add horizontal scroll view here
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
        tripDescripton = (EditText) view.findViewById(R.id.etTripDescription);
        tripDate = (EditText) view.findViewById(R.id.etTripDate);
        tripLocation = (EditText) view.findViewById(R.id.etTripLocation);
        tripAddFriends = (ImageButton)  view.findViewById(R.id.ibAddFriends);
        save = (Button) view.findViewById(R.id.btnTripSave);


        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        tripName.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        tripDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(AddTripFragment.this)
                        .setFirstDayOfWeek(Calendar.SUNDAY)
                        .setDoneText("Done")
                        .setCancelText("Cancel");

                cdp.show(getFragmentManager(), "Select Date");
            }
        });


        tripAddFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                AddFriendsDialogFragment addFriendsDialogFragment = new AddFriendsDialogFragment();
                addFriendsDialogFragment.setTargetFragment(AddTripFragment.this, 300);
                addFriendsDialogFragment.show(fm, "fragment_add_users");
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trip.setmName(tripName.getText().toString());
                trip.setmDescription(tripDescripton.getText().toString());
                trip.setmDate(Long.toString(DateUtils.shiftTimeZone(tripDate.getText().toString()).getTime()));
                trip.setmLocation(tripLocation.getText().toString());
                trip.setmUserID(ParseUser.getCurrentUser().getObjectId());
                trip.setmStatus("New");
                trip.saveInBackground();
                AddTripFragmentDialogListener addTripFragmentDialogListener =
                        (AddTripFragmentDialogListener) getActivity();
                addTripFragmentDialogListener.onFinishEditDialog(trip);
                dismiss();
            }
        });

    }


}

