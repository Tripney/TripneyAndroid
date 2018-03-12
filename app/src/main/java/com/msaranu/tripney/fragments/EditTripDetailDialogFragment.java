package com.msaranu.tripney.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.msaranu.tripney.R;
import com.msaranu.tripney.activities.DetailTripActivity;
import com.msaranu.tripney.models.Event;
import com.msaranu.tripney.models.Trip;
import com.msaranu.tripney.models.TripUser;
import com.msaranu.tripney.models.User;
import com.msaranu.tripney.services.ImageService;
import com.msaranu.tripney.utilities.DateUtils;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditTripDetailDialogFragment extends DialogFragment implements AddFriendsDialogFragment.AddFriendsFragmentDialogListener,
        CalendarDatePickerDialogFragment.OnDateSetListener, RadialTimePickerDialogFragment.OnTimeSetListener,
        PhotoAlertDialogFragment.PhotoAlertDialogFragmentListener {


    private EditText tripName;
    private EditText tripDate;
    private EditText tripDescripton;
    private EditText tripLocation;
    private ImageButton tripAddFriends;
    private Spinner spinnerTrip;
    private Button save;
    Trip trip;
    String tripID;
    ParseFile imageFile;
    Calendar cal;
    private ImageView ivCameraImage;
    private ImageView ivTripBckgrndImage;
    ImageService imgService;
    Uri file;
    public final static int SELECT_IMAGE_ACTIVITY_REQUEST_CODE = 200;
    private String imageURL;


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

    @Override
    public void onFinishAddFriendsDialog(List<TripUser> tUserList) {

    }

    public interface EditTripFragmentDialogListener {
        void onFinishEditDialog(Trip trip);
    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        tripDate.setText(monthOfYear + 1 + "/" + dayOfMonth + "/" + year);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        imgService = ImageService.getInstance();
        return inflater.inflate(R.layout.fragment_add_trip, container);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        trip = new Trip();
        super.onViewCreated(view, savedInstanceState);


        // Fetch arguments from bundle and set
        trip = getArguments().getParcelable("trip");
        tripID = trip.tripID;

        cal = DateUtils.convertUTCtoLocalTime(trip.mDate);


        tripName = (EditText) view.findViewById(R.id.etTripName);
        tripDescripton = (EditText) view.findViewById(R.id.etTripDescription);
        tripDate = (EditText) view.findViewById(R.id.etTripDate);
        tripLocation = (EditText) view.findViewById(R.id.etTripLocation);
        tripAddFriends = (ImageButton) view.findViewById(R.id.ibAddFriends);
        save = (Button) view.findViewById(R.id.btnTripSave);
        ivCameraImage = (ImageView) view.findViewById(R.id.ivCameraImage);
        ivTripBckgrndImage = (ImageView) view.findViewById(R.id.ivTripBckgrndImage);


        //TODO: Remove boiler plate code. Swith to databinding/butterknife

        tripName.setText(trip.mName);
        tripDescripton.setText(trip.mDescription);
        tripDate.setText( DateUtils.convertCalendarToDisplayDateEdit(cal));
        tripLocation.setText(trip.mlocation);

        if (trip.mbckgrndUrl != null) {
            Glide.with(this).load(trip.mbckgrndUrl.toString())
                    .fitCenter()
                    .into(ivTripBckgrndImage);
        }


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


        ivCameraImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = getFragmentManager();
                PhotoAlertDialogFragment fdf = PhotoAlertDialogFragment.newInstance();
                fdf.setTargetFragment(EditTripDetailDialogFragment.this, 300);
                fdf.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppDialogTheme);
                fdf.show(fm, "FRAGMENT_MODAL_ALERT");
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
                trip.setObjectId(tripID);
                trip.setmStatus("New");
                if(imageFile != null) {
                    imageFile.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                trip.setMbckgrndUrl(imageFile.getUrl());

                                trip.saveInBackground();

                            }
                        }
                    });
                }else{
                    trip.saveInBackground();
                }

                EditTripFragmentDialogListener editTripFragmentDialogListener =
                        (EditTripFragmentDialogListener) getTargetFragment();
                editTripFragmentDialogListener.onFinishEditDialog(trip);
                dismiss();
            }
        });

    }

}

