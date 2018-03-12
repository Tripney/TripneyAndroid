package com.msaranu.tripney.fragments;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.msaranu.tripney.R;
import com.msaranu.tripney.models.Event;
import com.msaranu.tripney.models.Trip;
import com.msaranu.tripney.services.ImageService;
import com.msaranu.tripney.utilities.BitmapScaler;
import com.msaranu.tripney.utilities.DateUtils;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditEventDialogFragment extends DialogFragment
        implements CalendarDatePickerDialogFragment.OnDateSetListener, RadialTimePickerDialogFragment.OnTimeSetListener,
        PhotoAlertDialogFragment.PhotoAlertDialogFragmentListener {

    public final static int SELECT_IMAGE_ACTIVITY_REQUEST_CODE = 200;

    private EditText eventName;
    private EditText eventLocation;
    private EditText eventDuration;
    private EditText eventType;
    private EditText eventPrice;
    private EditText eventDate;
    private ImageView ivCameraImage;
    private ImageView ivEventBckgrndImage;
    ImageService imgService;
    Uri file;
    int screenSize;


    private Button save;
    Event event;
    Trip trip;
    Calendar cal;
    private ParseFile imageFile;

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

    @Override
    public void onFinishAlertDialog(String photoType) {
        pickPicture();
    }

    public interface EditEventFragmentDialogListener {
        void onFinishEditDialog(Event event);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        imgService = ImageService.getInstance();
        return inflater.inflate(R.layout.fragment_edit_event_dialog, container, false);
    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        eventDate.setText(monthOfYear + 1 + "/" + dayOfMonth + "/" + year);
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
        eventDate.setText(eventDate.getText().toString() + " " + hourOfDay + ":" + minute);
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        event = new Event();
        super.onViewCreated(view, savedInstanceState);

        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        int pxWidth = displayMetrics.widthPixels;
        screenSize = (int) (pxWidth / displayMetrics.density);


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
        ivCameraImage = (ImageView) view.findViewById(R.id.ivCameraImage);
        ivEventBckgrndImage = (ImageView) view.findViewById(R.id.ivEventBckgrndImage);


        eventName.setText(event.name);
        eventLocation.setText(event.location);
        eventDuration.setText(event.duration);
        eventType.setText(event.type);
        eventPrice.setText(event.price.toString());
        eventDate.setText( DateUtils.convertCalendarToDisplayDateEdit(cal));

        if (event.eventImage != null) {
            Glide.with(this).load(event.eventImage.toString())
                    .fitCenter()
                    .into(ivEventBckgrndImage);
        }

        ivCameraImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = getFragmentManager();
                PhotoAlertDialogFragment fdf = PhotoAlertDialogFragment.newInstance();
                fdf.setTargetFragment(EditEventDialogFragment.this, 300);
                fdf.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppDialogTheme);
                fdf.show(fm, "FRAGMENT_MODAL_ALERT");
            }
        });


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
                if(imageFile != null) {

                    imageFile.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                event.setEventImage(imageFile.getUrl());
                                event.saveInBackground();
                            }
                        }
                    });
                }else{
                    event.saveInBackground();

                }

                EditEventFragmentDialogListener editEventFragmentDialogListener =
                        (EditEventFragmentDialogListener) getTargetFragment();
                editEventFragmentDialogListener.onFinishEditDialog(event);
                dismiss();
            }
        });

    }
}


