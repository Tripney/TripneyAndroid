package com.msaranu.tripney.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.msaranu.tripney.R;
import com.msaranu.tripney.databinding.ItemEventAlternateBinding;
import com.msaranu.tripney.databinding.ItemEventBinding;
import com.msaranu.tripney.databinding.ItemPersonBinding;
import com.msaranu.tripney.decorators.ItemClickSupport;
import com.msaranu.tripney.fragments.AddNewFriendsFragment;
import com.msaranu.tripney.fragments.EditEventDialogFragment;
import com.msaranu.tripney.fragments.EventDetailFragment;
import com.msaranu.tripney.fragments.TripThingsToDoFragment;
import com.msaranu.tripney.models.Event;
import com.msaranu.tripney.models.User;
import com.msaranu.tripney.models.UserFriend;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by msaranu on 1/14/18.
 */

public class SearchAddFriendsAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {


    RecyclerView mRecyclerView;
    AddNewFriendsFragment addNewFriendsFragment;
    // Store a member variable for the contacts
    private List<ParseUser> mPeople;
    // Store the context for easy access
    private Context mContext;

    // Pass in the contact array into the constructor
    public SearchAddFriendsAdapter(Context context, List<ParseUser> people, AddNewFriendsFragment addNewFriendsFragment) {
        mPeople = people;
        mContext = context;
        this.addNewFriendsFragment = addNewFriendsFragment;
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
        final ItemPersonBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = ItemPersonBinding.bind(itemView);
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
        personView = inflater.inflate(R.layout.item_person, parent, false);
        viewHolder = new ViewHolder(personView);


        ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                            AddFriendToUser(mPeople.get(position), v);
                    }
                }
        );

        return viewHolder;

    }

    private void AddFriendToUser(ParseUser friend, View v) {
        UserFriend uFriend = new UserFriend();
        uFriend.setUserID(ParseUser.getCurrentUser().getObjectId());
        uFriend.setFriendID(friend.getObjectId());
        uFriend.setStatus("P");
        uFriend.saveInBackground();
        Toast.makeText(getContext(), "Friend Added", Toast.LENGTH_SHORT).show();
    }


    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
                SearchAddFriendsAdapter.ViewHolder vh = (SearchAddFriendsAdapter.ViewHolder) viewHolder;
                configureViewHolder(vh,mPeople.get(position));

    }


    private void configureViewHolder(ViewHolder viewHolder, ParseUser person) {

        viewHolder.binding.tvFirstName.setText(person.get("firstName").toString());  // setVariable(BR.user, user) would also work
        viewHolder.binding.tvLastName.setText(person.get("lastName").toString());
        viewHolder.binding.tvUserId.setText("@" + person.get("username").toString());

        if(person.get("profilePicture") !=null) {
            Glide.with(mContext).load(person.get("profilePicture").toString())
                    .fitCenter()
                    .into(viewHolder.binding.ivUserProfileImage);
        }
    }
    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mPeople.size();
    }
}
