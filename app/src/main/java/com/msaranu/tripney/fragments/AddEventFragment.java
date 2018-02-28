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

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.codetroopers.betterpickers.timepicker.TimePickerDialogFragment;
import com.msaranu.tripney.R;
import com.msaranu.tripney.models.Event;
import com.msaranu.tripney.models.Trip;

import java.util.Calendar;


public class AddEventFragment extends DialogFragment  implements CalendarDatePickerDialogFragment.OnDateSetListener,  RadialTimePickerDialogFragment.OnTimeSetListener {


    private EditText eventName;
    private EditText eventLocation;
    private EditText eventDuration;
    private EditText eventType;
    private EditText eventPrice;
    private EditText eventDate;

    private Button save;
    Event event;
    Trip trip;
    Boolean isWishList=false;
    public static final String WISH_LIST = "wish";


    public AddEventFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static AddEventFragment newInstance(Trip trip, boolean isWishList) {
        AddEventFragment frag = new AddEventFragment();
        Bundle args = new Bundle();
        args.putParcelable("trip", trip);
        args.putBoolean(WISH_LIST, isWishList);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        eventDate.setText(monthOfYear+"/"+dayOfMonth+ "/" +year);
        callTimePicker();
    }

    private void callTimePicker() {
        RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                .setOnTimeSetListener(AddEventFragment.this)
                .setStartTime(10, 10)
                .setDoneText("Done")
                .setCancelText("Cancel");
        rtpd.show(getFragmentManager(), "Pick Time");
    }


    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        eventDate.setText(eventDate.getText().toString() + " " + hourOfDay + ":" + minute );

    }

    public interface AddEventFragmentDialogListener {
        void onFinishEditDialog(Event event);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_event, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        event = new Event();
        super.onViewCreated(view, savedInstanceState);

        // Fetch arguments from bundle and set
         trip = getArguments().getParcelable("trip");
         isWishList = getArguments().getBoolean(WISH_LIST);


        // Get field from view
        eventName = (EditText) view.findViewById(R.id.etEventName);
        eventLocation = (EditText) view.findViewById(R.id.etEventLocation);
        eventDuration = (EditText) view.findViewById(R.id.etEventDuration);
        eventType = (EditText) view.findViewById(R.id.etEventType);
        eventPrice = (EditText) view.findViewById(R.id.etEventPrice);
        eventDate = (EditText) view.findViewById(R.id.etEventDate);


        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                            CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                                .setOnDateSetListener(AddEventFragment.this)
                                .setFirstDayOfWeek(Calendar.SUNDAY)
                                    .setDoneText("Done")
                                .setCancelText("Cancel");

                        cdp.show(getFragmentManager(), "Select Date");
                    }
        });

        save = (Button) view.findViewById(R.id.btnEventSave);


        getDialog().setTitle("Title");
        // Show soft keyboard automatically and request focus to field
        eventName.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                event.setName(eventName.getText().toString());
                event.setLocation(eventLocation.getText().toString());
                event.setDuration(eventDuration.getText().toString());
                event.setType(eventType.getText().toString());
                event.setDate(eventDate.getText().toString());
                event.setPrice(Double.parseDouble(eventPrice.getText().toString()));
                event.setTripID(trip._id);
                if(isWishList) event.setIsWish("Y");
                else event.setIsWish("");

                event.saveInBackground();
                AddEventFragmentDialogListener addEventFragmentDialogListener =
                        (AddEventFragmentDialogListener) getTargetFragment();
                addEventFragmentDialogListener.onFinishEditDialog(event);
                dismiss();
            }
        });

    }
}

