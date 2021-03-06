package com.msaranu.tripney.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.msaranu.tripney.R;
import com.msaranu.tripney.databinding.ItemEventAlternateBinding;
import com.msaranu.tripney.databinding.ItemPersonBinding;
import com.msaranu.tripney.databinding.ItemTripFriendBinding;
import com.msaranu.tripney.models.EventUser;
import com.msaranu.tripney.models.UserFriend;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by msaranu on 1/14/18.
 */

public class AddFriendsToEventAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {


    RecyclerView mRecyclerView;
    // Store a member variable for the contacts
    private List<ParseUser> mPeople;
    // Store the context for easy access
    private Context mContext;
    private List<EventUser> eventUserList;
    private String tripID;

    public List<EventUser> getEventUserList() {
        return eventUserList;
    }

    public void setEventUser(List<EventUser> eventUserList) {
        this.eventUserList = eventUserList;
    }

    // Pass in the contact array into the constructor
    public AddFriendsToEventAdapter(Context context, List<ParseUser> people) {
        mPeople = people;
        mContext = context;
    }


    //Returns the view type of the item at position for the purposes of view recycling.
    @Override
    public int getItemViewType(int position) {
        return 0;
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
        final ItemTripFriendBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = ItemTripFriendBinding.bind(itemView);
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
        View personView;
        RecyclerView.ViewHolder viewHolder;
        personView = inflater.inflate(R.layout.item_trip_friend, parent, false);
        viewHolder = new ViewHolder(personView);
        eventUserList = new ArrayList<EventUser>();

        return viewHolder;

    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        AddFriendsToEventAdapter.ViewHolder vh = (AddFriendsToEventAdapter.ViewHolder) viewHolder;
        configureViewHolder(vh,mPeople.get(position), position);

    }



    private void configureViewHolder(ViewHolder viewHolder, ParseUser person, int position) {

        viewHolder.binding.tvFirstName.setText(person.get("firstName").toString());  // setVariable(BR.user, user) would also work
        viewHolder.binding.tvLastName.setText(person.get("lastName").toString());
        viewHolder.binding.tvUserId.setText("@" + person.get("username").toString());

        if(person.get("profilePicture") !=null) {
            Glide.with(mContext).load(person.get("profilePicture").toString())
                    .fitCenter()
                    .into(viewHolder.binding.ivUserProfileImage);
        }

        viewHolder.binding.ckAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ParseQuery<EventUser> queryUserTrip = ParseQuery.getQuery("EventUser");
                EventUser tUser = new EventUser();
                if(viewHolder.binding.ckAdd.isChecked()){
                    tUser.setUserID(person.getObjectId().toString());
                    tUser.setStatus("Y");
                }else{
                    tUser.setUserID(person.getObjectId().toString());
                    tUser.setStatus("N");
                }
                eventUserList.add(tUser);

            }
        });
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mPeople.size();
    }
}
