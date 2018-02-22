package com.msaranu.tripney.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.msaranu.tripney.R;
import com.msaranu.tripney.databinding.FragmentEventDetailBinding;
import com.msaranu.tripney.models.Event;
import com.parse.ParseUser;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link TripDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventDetailFragment extends android.support.v4.app.Fragment implements EditEventDialogFragment.EditEventFragmentDialogListener {

    MapView mMapView;
    ImageButton ivEditIcon;
    private GoogleMap googleMap;



    public static final String EVENT_OBJECT = "event_obj";
    Event event;
    FragmentEventDetailBinding fragmentBinding;

    public EventDetailFragment() {
        // Required empty public constructor
    }


    public static EventDetailFragment newInstance(Event event) {
        EventDetailFragment eventDetailFragment = new EventDetailFragment();
        Bundle args = new Bundle();
        event.loadInstanceVariables();
        args.putParcelable(EVENT_OBJECT, event);
        eventDetailFragment.setArguments(args);
        return eventDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        event = getArguments().getParcelable(EVENT_OBJECT);
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        fragmentBinding =  DataBindingUtil.inflate(inflater,R.layout.fragment_event_detail, parent, false);
        View view = fragmentBinding.getRoot();
        ButterKnife.bind(this, view);
        fragmentBinding.tvEventDate.setText(event.date);
        fragmentBinding.tvEventName.setText(event.name);
        fragmentBinding.tvEventLocation.setText(event.location);
        fragmentBinding.tvEventDuration.setText(event.duration);
        fragmentBinding.tvEventType.setText(event.type);
        fragmentBinding.tvEventPrice.setText(Double.toString(event.price));


        Glide.with(this).load(R.drawable.eventimage)
                .fitCenter()
                .into(fragmentBinding.ivEventBckgrndImage);

       //setOnBackButtonListener(view);
        //SetUp Listeners
        setSourceSiteLinkIntentListener();
        setAddToCalendarListener();
        setEditIconListener();
        //   setRedirectIconListener();
        initializeMap(view,savedInstanceState);

        return view;
    }

    //TODO: Not working when backbutton is pressed
    /*private void setOnBackButtonListener(View view) {
        Trip trip = new Trip();
        trip.set_id(event.tripID);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Intent i = new Intent(getContext(), DetailTripActivity.class);
                    i.putExtra("trip_obj", trip);
                    i.putExtra("item_id", R.id.action_trip_events);
                    getContext().startActivity(i);
                }
                return false;
            }
        });

    }*/


    private void setEditIconListener() {

        ivEditIcon = fragmentBinding.ivEditIcon;
        ivEditIcon.setOnClickListener(view -> {
            FragmentManager fm = getFragmentManager();
            EditEventDialogFragment editEventDialogFragment =EditEventDialogFragment.newInstance(event);
            editEventDialogFragment.setTargetFragment(EventDetailFragment.this, 300);
            editEventDialogFragment.show(fm, "fragment_edit_event");
        });
    }

    private void setAddToCalendarListener() {
        fragmentBinding.tvCalendar.setOnClickListener(v -> addToCalendar());
        fragmentBinding.ivCalendar.setOnClickListener(v -> addToCalendar());

    }

    private void initializeMap(View view, Bundle savedInstanceState) {
        mMapView = (MapView) view.findViewById(R.id.mapEBView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setSourceSiteLinkIntentListener() {

    }



    protected void populateAddressMap(Event t) {


        mMapView.getMapAsync(mMap -> {
            googleMap = mMap;

            LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

            Criteria locationCritera = new Criteria();
            locationCritera.setAccuracy(Criteria.ACCURACY_COARSE);
            locationCritera.setAltitudeRequired(false);
            locationCritera.setBearingRequired(false);
            locationCritera.setCostAllowed(true);
            locationCritera.setPowerRequirement(Criteria.NO_REQUIREMENT);

            // For showing a move to my location button
            int fineLocationPerm = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
            int coarseLocationPerm = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
            if (fineLocationPerm != PackageManager.PERMISSION_GRANTED && coarseLocationPerm != PackageManager.PERMISSION_GRANTED) {
                ParseUser user = ParseUser.getCurrentUser();
              /*  if (!user.getBoolean(User.LOC_PERM_SEEN)) {
                    Toast.makeText(getContext(), getString(R.string.pref_turn_on_location), Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, FomonoApplication.PERM_LOC_EVENT_REQ_CODE);
                } */
            } else {
                googleMap.setMyLocationEnabled(true);
            }
            LatLng latlng = new LatLng(37.7749, 122.4194);
            // For dropping a marker at a point on the Map
            googleMap.addMarker(new MarkerOptions().position(latlng).title("New York").
                    snippet("Some address"));


            // For zooming automatically to the location of the marker
            CameraPosition cameraPosition = new CameraPosition.Builder().target(latlng).zoom(11).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        });
    }

    public void addToCalendar() {
        long calID = 3;
        long startMillis = 0;
        long endMillis = 0;

      /*  startMillis = DateUtils.convertEventDatetoMilliSeconds(startDate);
        endMillis = DateUtils.convertEventDatetoMilliSeconds(endDate);

        ContentResolver cr = getActivity().getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());

        if (event.getName() != null) {
            values.put(CalendarContract.Events.TITLE, event.getName().getText());
        }
        if (event.getDescription() != null) {
            values.put(CalendarContract.Events.DESCRIPTION, event.getDescription().getText().toString());
        }
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
            long eventID = Long.parseLong(uri.getLastPathSegment());
            Toast.makeText(getActivity(), "Added to Calendar",
                    Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_CALENDAR}, FomonoApplication.PERM_CAL_EVENT_REQ_CODE);
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public void enableMapLocation() {
        int fineLocationPerm = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLocationPerm = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        if (fineLocationPerm == PackageManager.PERMISSION_GRANTED || coarseLocationPerm == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        }
    }


    @Override
    public void onFinishEditDialog(Event event) {
        this.event = event;
       getFragmentManager().beginTransaction().
                replace(R.id.flContainer, EventDetailFragment.newInstance(event)).commit();
    }
}
