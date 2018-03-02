package com.msaranu.tripney.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.msaranu.tripney.R;
import com.msaranu.tripney.activities.DetailTripActivity;
import com.msaranu.tripney.databinding.ItemTripBinding;
import com.msaranu.tripney.decorators.ItemClickSupport;
import com.msaranu.tripney.models.Trip;
import com.msaranu.tripney.utilities.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by msaranu on 1/14/18.
 */

public class TripRecyclerAdapter extends
        RecyclerView.Adapter<TripRecyclerAdapter.ViewHolder> {

    RecyclerView mRecyclerView;
    // Store a member variable for the contacts
    private List<Trip> mTrips;
    // Store the context for easy access
    private Context mContext;
    // Pass in the contact array into the constructor
    public TripRecyclerAdapter(Context context, List<Trip> trips) {
        mTrips = trips;
        mContext = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        mRecyclerView = recyclerView;
    }

    private Context getContext() {
        return mContext;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        final ItemTripBinding binding;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            binding = ItemTripBinding.bind(itemView);
        }


    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public TripRecyclerAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        final View tripView = inflater.inflate(R.layout.item_trip, parent, false);

        // Return a new holder instance
        final ViewHolder viewHolder = new ViewHolder(tripView);

        ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                      //  Toast.makeText(getContext(), "Detailed Trip not yet available",
                      //          Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getContext(), DetailTripActivity.class);
                        i.putExtra("trip_obj", mTrips.get(position));
                        getContext().startActivity(i);
                    }
                }
        );


        return viewHolder;

    }


    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(TripRecyclerAdapter.ViewHolder viewHolder, int position) {

        // Get the data model based on position
        Trip trip = mTrips.get(position);

        Calendar cal = DateUtils.convertUTCtoLocalTime(trip.get("mDate").toString());

        // Set item views based on your views and data model

        viewHolder.binding.tvTripName.setText(trip.get("mName").toString());  // setVariable(BR.user, user) would also work
        viewHolder.binding.tvTripMonth.setText((new SimpleDateFormat("MMM").format(cal.getTime())).toUpperCase());
        viewHolder.binding.tvTripDay.setText(Integer.toString(cal.get(Calendar.DAY_OF_MONTH)));

        viewHolder.binding.tvTripDescriptom.setText(trip.get("mDescription").toString());
        viewHolder.binding.tvTripStatus.setText(trip.get("mStatus").toString());


                       Glide.with(mContext).load(R.drawable.eventimage)
                                .fitCenter()
                                .into(viewHolder.binding.ivTripBckgrndImage);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mTrips.size();
    }
}
