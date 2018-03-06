package com.msaranu.tripney.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.codetroopers.betterpickers.timepicker.TimePickerDialogFragment;
import com.msaranu.tripney.R;
import com.msaranu.tripney.models.Event;
import com.msaranu.tripney.models.EventUser;
import com.msaranu.tripney.models.Trip;
import com.msaranu.tripney.models.EventUser;
import com.msaranu.tripney.utilities.DateUtils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;


public class AddEventFragment extends DialogFragment  implements CalendarDatePickerDialogFragment.OnDateSetListener, 
        RadialTimePickerDialogFragment.OnTimeSetListener, AddFriendsToEventDialogFragment.AddFriendsFragmentDialogListener {


    private EditText eventName;
    private EditText eventLocation;
    private EditText eventDuration;
    private EditText eventType;
    private EditText eventPrice;
    private EditText eventDate;
    ImageButton eventAddFriends;
    LinearLayout llFriendsListHorizontal ;


    private Button save;
    Event event;
    Trip trip;
    Boolean isWishList=false;
    public static final String WISH_LIST = "wish";
    private List<EventUser> eventUserList;


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
        eventDate.setText(monthOfYear+1 +"/"+dayOfMonth+ "/" +year);
        callTimePicker();
    }

    private void callTimePicker() {
        RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                .setOnTimeSetListener(AddEventFragment.this)
                .setTitleText(TimeZone.getDefault().getDisplayName())
                .setStartTime(10, 10)
                .setDoneText("Done")
                .setCancelText("Cancel");
        rtpd.show(getFragmentManager(), "Pick Time");
    }


    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        eventDate.setText(eventDate.getText().toString() + " " + hourOfDay + ":" + minute );
    }

    @Override
    public void onFinishAddFriendsDialog(List<EventUser> eventUserList) {

        //TODO Add horizontal scroll view here
        this.eventUserList = eventUserList;
        addFriendsLayout();

    }

    private void addFriendsLayout() {

        for(EventUser eventUser: eventUserList ){

            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("objectId", eventUser.getUserID());

            query.findInBackground(new FindCallback<ParseUser>() {
                public void done(List<ParseUser> itemList, ParseException e) {
                    if (e == null) {
                        for(ParseUser userF : itemList){


                            LayoutInflater inflater = LayoutInflater.from(getContext());
                            View llFriendsInnerList= inflater.inflate(R.layout.layout_friends, null, false);

                            ImageView userImage = (ImageView) llFriendsInnerList.findViewById(R.id.ivUserProfileImage);
                            TextView userName = (TextView) llFriendsInnerList.findViewById(R.id.tvName);

                            userName.setText(userF.get("firstName").toString() + "\n" + userF.get("lastName").toString());

                            String profileURL;
                            if(userF.get("profilePicture")!=null) {
                                Glide.with(getContext()).load(userF.get("profilePicture").toString())
                                        .fitCenter()
                                        .into(userImage);

                            }else{
                                Glide.with(getContext()).load(R.drawable.ic_person)
                                        .fitCenter()
                                        .into(userImage);
                            }

                            llFriendsListHorizontal.addView(llFriendsInnerList);


                        }
                    }
                    else {
                        Log.d("item", "Error: " + e.getMessage());
                    }
                }

            });

        }

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
         eventAddFriends = (ImageButton) view.findViewById(R.id.ibAddFriends);
        llFriendsListHorizontal = (LinearLayout) view.findViewById(R.id.llFriendsHorizontal);
        save = (Button) view.findViewById(R.id.btnEventSave);



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


        eventAddFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                AddFriendsToEventDialogFragment addFriendsToEventDialogFragment = AddFriendsToEventDialogFragment.newInstance();
                addFriendsToEventDialogFragment.setTargetFragment(AddEventFragment.this, 300);
                addFriendsToEventDialogFragment.show(fm, "fragment_add_users");
            }
        });
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
                event.setTripID(trip._id);
                if(isWishList) event.setIsWish("Y");
                else event.setIsWish("");

                event.saveInBackground(new SaveCallback() {
                                          @Override
                                          public void done(ParseException e) {
                                              for (EventUser pU: eventUserList) {
                                                  pU.setEventID(event.getObjectId());
                                                  pU.saveInBackground();
                                              }
                                          }
                                      }
                );                AddEventFragmentDialogListener addEventFragmentDialogListener =
                        (AddEventFragmentDialogListener) getTargetFragment();
                addEventFragmentDialogListener.onFinishEditDialog(event);
                dismiss();
            }
        });

    }
}

