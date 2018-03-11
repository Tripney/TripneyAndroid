package com.msaranu.tripney.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.msaranu.tripney.services.ImageService;
import com.msaranu.tripney.utilities.DateUtils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import static android.app.Activity.RESULT_OK;


public class AddEventFragment extends DialogFragment  implements CalendarDatePickerDialogFragment.OnDateSetListener, 
        RadialTimePickerDialogFragment.OnTimeSetListener, AddFriendsToEventDialogFragment.AddFriendsFragmentDialogListener ,
           PhotoAlertDialogFragment.PhotoAlertDialogFragmentListener {



private EditText eventName;
    private EditText eventLocation;
    private EditText eventDuration;
    private EditText eventType;
    private EditText eventPrice;
    private EditText eventDate;
    ImageButton eventAddFriends;
    LinearLayout llFriendsListHorizontal ;
    private ImageView ivCameraImage;
    private ImageView ivEventBckgrndImage;
    ImageService imgService;
    Uri file;
    public final static int SELECT_IMAGE_ACTIVITY_REQUEST_CODE = 200;



    private Button save;
    Event event;
    Trip trip;
    String tripID;
    Boolean isWishList=false;
    public static final String WISH_LIST = "wish";
    private List<EventUser> eventUserList;


    public AddEventFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    @Override
    public void onFinishAlertDialog(String photoType) {
        pickPicture();
    }


    public void pickPicture() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_IMAGE_ACTIVITY_REQUEST_CODE);

    }



    private void saveProfileImage(int requestCode) {
        String filePath = null;
        if (requestCode == SELECT_IMAGE_ACTIVITY_REQUEST_CODE) {
            filePath = imgService.getPath(file, getContext());
        }

        Bitmap bitmap = imgService.getBitMap(getContext(), filePath, file);
        // Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(bitmap, screenSize);

        ivEventBckgrndImage.setImageBitmap(bitmap);

        imgService.saveParseFile(bitmap, event);
    }



   /* @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable("TRIP_OBJ", trip);
        savedInstanceState.putBoolean("isWishList", isWishList);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            trip = savedInstanceState.getParcelable("TRIP_OBJ");
            isWishList = savedInstanceState.getBoolean("isWishList");
        }
    }*/


        @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        if (resultCode == RESULT_OK) {
             if (requestCode == SELECT_IMAGE_ACTIVITY_REQUEST_CODE) {
                file = imageReturnedIntent.getData();
            }
            saveProfileImage(requestCode);
        }
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
        imgService = ImageService.getInstance();
        return inflater.inflate(R.layout.fragment_add_event, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        event = new Event();
        super.onViewCreated(view, savedInstanceState);

        // Fetch arguments from bundle and set
         trip = getArguments().getParcelable("trip");
         tripID = trip.tripID;
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
        ivCameraImage = (ImageView) view.findViewById(R.id.ivCameraImage);
        ivEventBckgrndImage = (ImageView) view.findViewById(R.id.ivEventBckgrndImage);


        if(event.eventImage != null) {
            Glide.with(this).load(event.eventImage.toString())
                    .fitCenter()
                    .into(ivEventBckgrndImage);
        }

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
                AddFriendsToEventDialogFragment addFriendsToEventDialogFragment = AddFriendsToEventDialogFragment.newInstance(tripID);
                addFriendsToEventDialogFragment.setTargetFragment(AddEventFragment.this, 300);
                addFriendsToEventDialogFragment.show(fm, "fragment_add_users");
            }
        });
        getDialog().setTitle("Title");
        // Show soft keyboard automatically and request focus to field
        eventName.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);



        ivCameraImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = getFragmentManager();
                PhotoAlertDialogFragment fdf = PhotoAlertDialogFragment.newInstance();
                fdf.setTargetFragment(AddEventFragment.this, 300);
                fdf.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppDialogTheme);
                fdf.show(fm, "FRAGMENT_MODAL_ALERT");
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                event.setName(eventName.getText().toString());
                event.setLocation(eventLocation.getText().toString());
                event.setDuration(eventDuration.getText().toString());
                event.setType(eventType.getText().toString());
                event.setDate(Long.toString(DateUtils.shiftTimeZone(eventDate.getText().toString()).getTime()));
                event.setPrice(Double.parseDouble(eventPrice.getText().toString()));
                event.setTripID(tripID);
                if(isWishList) event.setIsWish("Y");
                else event.setIsWish("");

                event.saveInBackground(new SaveCallback() {
                                          @Override
                                          public void done(ParseException e) {
                                              if(eventUserList !=null && eventUserList.size() >0 ) {
                                                  for (EventUser pU : eventUserList) {
                                                      pU.setEventID(event.getObjectId());
                                                      pU.saveInBackground();
                                                  }
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

