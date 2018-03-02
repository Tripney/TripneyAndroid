package com.msaranu.tripney.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.msaranu.tripney.R;
import com.msaranu.tripney.models.Event;
import com.msaranu.tripney.models.Trip;
import com.msaranu.tripney.utilities.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditEventDialogFragment extends DialogFragment implements CalendarDatePickerDialogFragment.OnDateSetListener,  RadialTimePickerDialogFragment.OnTimeSetListener {

    private EditText eventName;
    private EditText eventLocation;
    private EditText eventDuration;
    private EditText eventType;
    private EditText eventPrice;
    private EditText eventDate;
    private Button save;
    Event event;
    Trip trip;
    Calendar cal;

    public EditEventDialogFragment() {
        // Required empty public constructor
    }

    public static EditEventDialogFragment newInstance(Event event) {
        EditEventDialogFragment frag = new EditEventDialogFragment();
        Bundle args = new Bundle();
        event.loadInstanceVariables();
        args.putParcelable("event", event);
        frag.setArguments(args);
        return frag;
    }

    public interface EditEventFragmentDialogListener {
        void onFinishEditDialog(Event event);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_event_dialog, container, false);
    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        eventDate.setText(monthOfYear+1 +"/"+dayOfMonth+ "/" +year);
        callTimePicker();
    }

    private void callTimePicker() {
        RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                .setOnTimeSetListener(EditEventDialogFragment.this)
                .setTitleText(TimeZone.getDefault().getDisplayName())
                .setStartTime(cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE))
                .setDoneText("Done")
                .setCancelText("Cancel");
        rtpd.show(getFragmentManager(), "Pick Time");
    }


    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        eventDate.setText(eventDate.getText().toString() + " " + hourOfDay + ":" + minute );
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        event = new Event();
        super.onViewCreated(view, savedInstanceState);

        // Fetch arguments from bundle and set
        event = getArguments().getParcelable("event");

        cal = DateUtils.convertUTCtoLocalTime(event.date);


        //TODO: Remove boiler plate code. Swith to databinding/butterknife
        // Get field from view
        eventName = (EditText) view.findViewById(R.id.etEventName);
        eventLocation = (EditText) view.findViewById(R.id.etEventLocation);
        eventDuration = (EditText) view.findViewById(R.id.etEventDuration);
        eventType = (EditText) view.findViewById(R.id.etEventType);
        eventPrice = (EditText) view.findViewById(R.id.etEventPrice);
        eventDate = (EditText) view.findViewById(R.id.etEventDate);

        eventName.setText(event.name);
        eventLocation.setText(event.location);
        eventDuration.setText(event.duration);
        eventType.setText(event.type);
        eventPrice.setText(event.price.toString());
        eventDate.setText(cal.get(Calendar.YEAR) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.DAY_OF_MONTH)
                +" " +cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE));


        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(EditEventDialogFragment.this)
                        .setPreselectedDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
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
                event.setDate(Long.toString(DateUtils.shiftTimeZone(eventDate.getText().toString()).getTime()));
                event.setPrice(Double.parseDouble(eventPrice.getText().toString()));
                event.setObjectId(event.eventID);

                event.saveInBackground();
                EditEventFragmentDialogListener editEventFragmentDialogListener =
                        (EditEventFragmentDialogListener) getTargetFragment();
                editEventFragmentDialogListener.onFinishEditDialog(event);
                dismiss();
            }
        });

    }
}


