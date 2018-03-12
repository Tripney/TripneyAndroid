package com.msaranu.tripney.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.msaranu.tripney.R;
import com.msaranu.tripney.models.Trip;
import com.msaranu.tripney.models.TripUser;
import com.msaranu.tripney.services.ImageService;
import com.msaranu.tripney.utilities.DateUtils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import static android.app.Activity.RESULT_OK;


public class AddTripFragment extends DialogFragment
        implements AddFriendsDialogFragment.AddFriendsFragmentDialogListener,
        CalendarDatePickerDialogFragment.OnDateSetListener, RadialTimePickerDialogFragment.OnTimeSetListener,
        PhotoAlertDialogFragment.PhotoAlertDialogFragmentListener {


    private EditText tripName;
    private EditText tripDate;
    private EditText tripDescripton;
    private EditText tripLocation;
    private ImageButton tripAddFriends;
    private Spinner spinnerTrip;
    private LinearLayout llFriendsListHorizontal;
    private Button save;
    Trip trip;
    private List<TripUser> tripUserList;
    private List<ParseUser> usersAdded;
    private ImageView ivCameraImage;
    private ImageView ivTripBckgrndImage;
    ImageService imgService;
    String imageURL;
    ParseFile imageFile;
    String tripID;
    Uri file;
    public final static int SELECT_IMAGE_ACTIVITY_REQUEST_CODE = 200;


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
    public void onFinishAlertDialog(String photoType) {
        pickPicture();
    }


    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        tripDate.setText(monthOfYear + 1 + "/" + dayOfMonth + "/" + year);
        callTimePicker();
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

        ivTripBckgrndImage.setImageBitmap(bitmap);

        imageFile = imgService.getParseFile(bitmap);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_IMAGE_ACTIVITY_REQUEST_CODE) {
                file = imageReturnedIntent.getData();
            }
            saveProfileImage(requestCode);
        }
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
        tripDate.setText(tripDate.getText().toString() + " " + hourOfDay + ":" + minute);
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
    public void onFinishAddFriendsDialog(List<TripUser> tripUserList) {
        //TODO Add horizontal scroll view here
        this.tripUserList = tripUserList;
        addFriendsLayout();

    }

    private void addFriendsLayout() {

        for (TripUser tripUser : tripUserList) {

            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("objectId", tripUser.getUserID());

            query.findInBackground(new FindCallback<ParseUser>() {
                public void done(List<ParseUser> itemList, ParseException e) {
                    if (e == null) {
                        for (ParseUser userF : itemList) {

                            usersAdded.add(userF);

                            LayoutInflater inflater = LayoutInflater.from(getContext());
                            View llFriendsInnerList = inflater.inflate(R.layout.layout_friends, null, false);

                            ImageView userImage = (ImageView) llFriendsInnerList.findViewById(R.id.ivUserProfileImage);
                            TextView userName = (TextView) llFriendsInnerList.findViewById(R.id.tvName);

                            userName.setText(userF.get("firstName").toString() + "\n" + userF.get("lastName").toString());

                            String profileURL;
                            if (userF.get("profilePicture") != null) {
                                Glide.with(getContext()).load(userF.get("profilePicture").toString())
                                        .fitCenter()
                                        .into(userImage);

                            } else {
                                Glide.with(getContext()).load(R.drawable.ic_person)
                                        .fitCenter()
                                        .into(userImage);
                            }

                            llFriendsListHorizontal.addView(llFriendsInnerList);


                        }
                    } else {
                        Log.d("item", "Error: " + e.getMessage());
                    }
                }

            });

        }

    }


    public interface AddTripFragmentDialogListener {
        void onFinishEditDialog(Trip trip);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        imgService = ImageService.getInstance();
        return inflater.inflate(R.layout.fragment_add_trip, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        usersAdded = new ArrayList<ParseUser>();
        trip = new Trip();
        tripID = trip.getObjectId();
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        tripName = (EditText) view.findViewById(R.id.etTripName);
        tripDescripton = (EditText) view.findViewById(R.id.etTripDescription);
        tripDate = (EditText) view.findViewById(R.id.etTripDate);
        tripLocation = (EditText) view.findViewById(R.id.etTripLocation);
        tripAddFriends = (ImageButton) view.findViewById(R.id.ibAddFriends);
        llFriendsListHorizontal = (LinearLayout) view.findViewById(R.id.llFriendsHorizontal);
        save = (Button) view.findViewById(R.id.btnTripSave);
        ivCameraImage = (ImageView) view.findViewById(R.id.ivCameraImage);
        ivTripBckgrndImage = (ImageView) view.findViewById(R.id.ivTripBckgrndImage);


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


        ivCameraImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = getFragmentManager();
                PhotoAlertDialogFragment fdf = PhotoAlertDialogFragment.newInstance();
                fdf.setTargetFragment(AddTripFragment.this, 300);
                fdf.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppDialogTheme);
                fdf.show(fm, "FRAGMENT_MODAL_ALERT");
            }
        });

        tripAddFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                AddFriendsDialogFragment addFriendsDialogFragment = AddFriendsDialogFragment.newInstance((ArrayList)tripUserList, trip.tripID);
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

                imageFile.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            trip.setMbckgrndUrl(imageFile.getUrl());
                            trip.saveInBackground(new SaveCallback() {
                                                      @Override
                                                      public void done(ParseException e) {
                                                          if (tripUserList != null && tripUserList.size() > 0) {
                                                              for (TripUser pU : tripUserList) {
                                                                  pU.setTripID(trip.getObjectId());
                                                                  pU.saveInBackground();
                                                              }
                                                          }
                                                      }
                                                  }
                            );


                        }
                    }
                });


                AddTripFragmentDialogListener addTripFragmentDialogListener =
                        (AddTripFragmentDialogListener) getActivity();
                addTripFragmentDialogListener.onFinishEditDialog(trip);
                dismiss();
            }
        });

    }


}

