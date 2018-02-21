package com.msaranu.tripney.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.msaranu.tripney.R;
import com.msaranu.tripney.databinding.ItemEventAlternateBinding;
import com.msaranu.tripney.databinding.ItemEventBinding;
import com.msaranu.tripney.decorators.ItemClickSupport;
import com.msaranu.tripney.fragments.EventDetailFragment;
import com.msaranu.tripney.fragments.TripThingsToDoFragment;
import com.msaranu.tripney.models.Event;

import java.util.List;

/**
 * Created by msaranu on 1/14/18.
 */

public class EventRecyclerAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    public final int even=2;
    public final int odd =1;
    RecyclerView mRecyclerView;
    TripThingsToDoFragment tripThingsToDoFragment;
    // Store a member variable for the contacts
    private List<Event> mEvents;
    // Store the context for easy access
    private Context mContext;
    // Pass in the contact array into the constructor
    public EventRecyclerAdapter(Context context, List<Event> events, TripThingsToDoFragment tripThingsToDoFragment) {
        mEvents = events;
        mContext = context;
        this.tripThingsToDoFragment = tripThingsToDoFragment;
    }


    //Returns the view type of the item at position for the purposes of view recycling.
    @Override
    public int getItemViewType(int position) {
        if (position %2==0){
            return odd;
        } else
            return even;
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        mRecyclerView = recyclerView;
    }
    private Context getContext() {
        return mContext;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
         final ItemEventBinding binding;
         public ViewHolder(View itemView) {
            super(itemView);
            binding = ItemEventBinding.bind(itemView);
        }
        
    }


    public static class ViewHolderAlternate extends RecyclerView.ViewHolder {
        final ItemEventAlternateBinding binding;
        public ViewHolderAlternate(View itemView) {
            super(itemView);
            binding = ItemEventAlternateBinding.bind(itemView);
        }
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
         View eventView;
         RecyclerView.ViewHolder viewHolder;
        switch (viewType) {
            case odd:
                eventView = inflater.inflate(R.layout.item_event, parent, false);
                viewHolder = new ViewHolder(eventView);
                break;
            case even:
                 eventView = inflater.inflate(R.layout.item_event_alternate, parent, false);
                 viewHolder = new ViewHolderAlternate(eventView);
                break;
            default:
                return null;

        }


        ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        /*Intent i = new Intent(getContext(), DetailEventActivity.class);
                        i.putExtra("event_obj", mEvents.get(position));
                        getContext().startActivity(i);*/


                        final FragmentManager fragmentManager = tripThingsToDoFragment.getActivity().getSupportFragmentManager();
                        final FragmentTransaction fragmentTripDetail = fragmentManager.beginTransaction();
                        fragmentTripDetail.replace(R.id.flContainer, EventDetailFragment.newInstance(mEvents.get(position))).commit();


                        /*Fragment fragment =  EventDetailFragment.newInstance(mEvents.get(position));
                        Bundle args = new Bundle();
                       // args.putString("event_obj", "This data has sent to FragmentTwo");
                        fragment.setArguments(args);
                        FragmentTransaction transaction = tripThingsToDoFragment.getActivity().getSupportFragmentManager().
                                beginTransaction();
                        transaction.replace(R.id.flContainer, fragment);
                        //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        transaction.addToBackStack(null);
                        transaction.commit();*/

                    }
                }
        );


        return viewHolder;

    }


    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Event event = mEvents.get(position);


        switch (viewHolder.getItemViewType()) {
            case odd:
                EventRecyclerAdapter.ViewHolder vh = (EventRecyclerAdapter.ViewHolder) viewHolder;
                configureViewHolder(vh,event);
                break;
            case even:
                EventRecyclerAdapter.ViewHolderAlternate vhAlternate= (EventRecyclerAdapter.ViewHolderAlternate) viewHolder;
                configureViewHolderAlternate(vhAlternate,event);
                break;
            default:
                break;
        }

    }

    private void configureViewHolderAlternate(ViewHolderAlternate viewHolder, Event event) {

        viewHolder.binding.tvEventName.setText(event.get("name").toString());  // setVariable(BR.user, user) would also work
        viewHolder.binding.tvEventLocation.setText(event.get("location").toString());
        viewHolder.binding.tvEventDuration.setText(event.get("duration").toString());
        viewHolder.binding.tvEventType.setText(event.get("type").toString());
        viewHolder.binding.tvEventPrice.setText(event.get("price").toString());

        Glide.with(mContext).load(R.drawable.eventimage)
                .fitCenter()
                .into(viewHolder.binding.ivEventImage);
    }

    private void configureViewHolder(ViewHolder viewHolder, Event event) {

        viewHolder.binding.tvEventName.setText(event.get("name").toString());  // setVariable(BR.user, user) would also work
        viewHolder.binding.tvEventLocation.setText(event.get("location").toString());
        viewHolder.binding.tvEventDuration.setText(event.get("duration").toString());
        viewHolder.binding.tvEventType.setText(event.get("type").toString());
        viewHolder.binding.tvEventPrice.setText(event.get("price").toString());

        Glide.with(mContext).load(R.drawable.eventimage)
                .fitCenter()
                .into(viewHolder.binding.ivEventImage);
    }
    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mEvents.size();
    }
}
