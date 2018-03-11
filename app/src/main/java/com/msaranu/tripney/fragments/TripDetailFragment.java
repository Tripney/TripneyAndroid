package com.msaranu.tripney.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import com.msaranu.tripney.databinding.FragmentDetailTripBinding;
import com.msaranu.tripney.models.Event;
import com.msaranu.tripney.models.Trip;
import com.msaranu.tripney.utilities.DateUtils;
import com.parse.ParseUser;

import java.util.Calendar;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link TripDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TripDetailFragment extends android.support.v4.app.Fragment implements EditTripDetailDialogFragment.EditTripFragmentDialogListener {

    MapView mMapView;
    ImageButton ivEditIcon;
    private GoogleMap googleMap;


    public static final String TRIP_OBJECT = "trip_obj";
    Trip trip;
    FragmentDetailTripBinding fragmentBinding;

    public TripDetailFragment() {
        // Required empty public constructor
    }


    public static TripDetailFragment newInstance(Trip trip) {
        TripDetailFragment tripDetailFragment = new TripDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(TRIP_OBJECT, trip);
        tripDetailFragment.setArguments(args);
        return tripDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trip = getArguments().getParcelable(TRIP_OBJECT);
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Calendar cal = DateUtils.convertUTCtoLocalTime(trip.mDate);

            // Inflate the layout for this fragment
        fragmentBinding =  DataBindingUtil.inflate(inflater,R.layout.fragment_detail_trip, parent, false);
            View view = fragmentBinding.getRoot();
            ButterKnife.bind(this, view);
        fragmentBinding.tvTripDate.setText(cal.getTime().toString());
        fragmentBinding.tvTripDescription.setText(trip.mDescription);
        fragmentBinding.tvTripName.setText(trip.mName);
        fragmentBinding.tvTripStatus.setText(trip.mStatus);


        if(trip.mbckgrndUrl != null) {
            Glide.with(this).load(trip.mbckgrndUrl)
                    .fitCenter()
                    .into(fragmentBinding.ivTripBckgrndImage);
        }

        //SetUp Listeners
        setSourceSiteLinkIntentListener();
        setAddToCalendarListener();
        setEditIconListener();
     //   setRedirectIconListener();
      initializeMap(view,savedInstanceState);

        return view;
    }

    private void setEditIconListener() {

        ivEditIcon = fragmentBinding.ivEditIcon;
        ivEditIcon.setOnClickListener(view -> {
            FragmentManager fm = getFragmentManager();
            EditTripDetailDialogFragment editTripDetailDialogFragment =EditTripDetailDialogFragment.newInstance(trip);
            editTripDetailDialogFragment.setTargetFragment(TripDetailFragment.this, 300);
            editTripDetailDialogFragment.show(fm, "fragment_edit_event");
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



    protected void populateAddressMap(Trip t) {


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
    public void onFinishEditDialog(Trip trip) {
        this.trip = trip;
        getFragmentManager().beginTransaction().
                replace(R.id.flContainer, TripDetailFragment.newInstance(trip)).commit();
    }

}
