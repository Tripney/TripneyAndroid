package com.msaranu.tripney.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.msaranu.tripney.R;
import com.msaranu.tripney.models.Event;
import com.msaranu.tripney.models.Trip;
import com.msaranu.tripney.models.TripUser;
import com.msaranu.tripney.models.User;
import com.msaranu.tripney.utilities.DateUtils;
import com.parse.ParseUser;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditTripDetailDialogFragment extends DialogFragment implements AddFriendsDialogFragment.AddFriendsFragmentDialogListener,
        CalendarDatePickerDialogFragment.OnDateSetListener,  RadialTimePickerDialogFragment.OnTimeSetListener {


    private EditText tripName;
    private EditText tripDate;
    private EditText tripDescripton;
    private EditText tripLocation;
    private ImageButton tripAddFriends;
    private Spinner spinnerTrip;
    private Button save;
    Trip trip;
    Calendar cal;

    public EditTripDetailDialogFragment() {
        // Required empty public constructor
    }



        public static EditTripDetailDialogFragment newInstance(Trip trip) {
            EditTripDetailDialogFragment frag = new EditTripDetailDialogFragment();
            Bundle args = new Bundle();
            args.putParcelable("trip", trip);
            frag.setArguments(args);
            return frag;
        }

    @Override
    public void onFinishAddFriendsDialog(List<TripUser> tUserList) {

    }

    public interface EditTripFragmentDialogListener {
            void onFinishEditDialog(Event event);
        }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        tripDate.setText(monthOfYear+1 +"/"+dayOfMonth+ "/" +year);
        callTimePicker();
    }

    private void callTimePicker() {
        RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                .setOnTimeSetListener(EditTripDetailDialogFragment.this)
                .setTitleText(TimeZone.getDefault().getDisplayName())
                .setStartTime(cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE))
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


        // Fetch arguments from bundle and set
        trip = getArguments().getParcelable("trip");

        cal = DateUtils.convertUTCtoLocalTime(trip.mDate);


        tripName = (EditText) view.findViewById(R.id.etTripName);
        tripDescripton = (EditText) view.findViewById(R.id.etTripDescription);
        tripDate = (EditText) view.findViewById(R.id.etTripDate);
        tripLocation = (EditText) view.findViewById(R.id.etTripLocation);
        tripAddFriends = (ImageButton)  view.findViewById(R.id.ibAddFriends);
        save = (Button) view.findViewById(R.id.btnTripSave);


        //TODO: Remove boiler plate code. Swith to databinding/butterknife

        tripName.setText(trip.mName);
        tripDescripton.setText(trip.mDescription);
        tripDate.setText(cal.get(Calendar.YEAR) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.DAY_OF_MONTH)
                +" " +cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE));
        tripLocation.setText(trip.mlocation);



        tripDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(EditTripDetailDialogFragment.this)
                        .setPreselectedDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
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
                addFriendsDialogFragment.setTargetFragment(EditTripDetailDialogFragment.this, 300);
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
                AddTripFragment.AddTripFragmentDialogListener addTripFragmentDialogListener =
                        (AddTripFragment.AddTripFragmentDialogListener) getActivity();
                addTripFragmentDialogListener.onFinishEditDialog(trip);
                dismiss();
            }
        });

    }
    }

