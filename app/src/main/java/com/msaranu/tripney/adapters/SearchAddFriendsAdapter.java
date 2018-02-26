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
import com.msaranu.tripney.decorators.ItemClickSupport;
import com.msaranu.tripney.fragments.AddNewFriendsFragment;
import com.msaranu.tripney.models.UserFriend;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
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
                           // AddFriendToUser(mPeople.get(position), v);
                    }
                }
        );

        return viewHolder;

    }

    private void AddFriendToUser(ParseUser friend, View v) {
        UserFriend uFriend = new UserFriend();
        UserFriend uUser = new UserFriend();

        String status =  ((Button)v).getText().toString();

        if(status == "Add"){

            uFriend.setUserID(ParseUser.getCurrentUser().getObjectId());
            uFriend.setFriendID(friend.getObjectId());
            uFriend.setStatus("Pending");//Complete
            uFriend.saveInBackground();

            uUser.setUserID(friend.getObjectId());
            uUser.setFriendID(ParseUser.getCurrentUser().getObjectId());
            uUser.setStatus("Accept");//Complete
            uUser.saveInBackground();

            ((Button)v).setText("Pending");

            Toast.makeText(getContext(), "Friend Requent Sent", Toast.LENGTH_SHORT).show();
        }


        if(status != null && status.equalsIgnoreCase("Accept")){
            ParseQuery<UserFriend> queryUFriend = ParseQuery.getQuery("UserFriend");
            ParseQuery<UserFriend> queryUFriendReverse = ParseQuery.getQuery("UserFriend");

            ParseQuery<UserFriend> queryUFriendUpdate = ParseQuery.getQuery("UserFriend");
            ParseQuery<UserFriend> queryUFriendUpdateReverse = ParseQuery.getQuery("UserFriend");

            final String uFriendOID=null;
            queryUFriend.whereEqualTo("userID", ParseUser.getCurrentUser().getObjectId());
            queryUFriend.whereEqualTo("friendID", friend.getObjectId());

            queryUFriendReverse.whereEqualTo("friendID", ParseUser.getCurrentUser().getObjectId());
            queryUFriendReverse.whereEqualTo("userID", friend.getObjectId());


            queryUFriend.findInBackground(new FindCallback<UserFriend>() {
                                              public void done(List<UserFriend> itemList, ParseException e) {
                                                  if (e == null) {
                                                      for (UserFriend userF : itemList) {

                                                          queryUFriendUpdate.getInBackground(userF.getObjectId().toString(), new GetCallback<UserFriend>() {
                                                              public void done(UserFriend uFriend, ParseException e) {
                                                                  if (e == null) {
                                                                      uFriend.put("status", "Friend");
                                                                      uFriend.saveInBackground();
                                                                  } else {
                                                                      Log.d("item", "Error: " + e.getMessage());
                                                                  }
                                                              }
                                                          });
                                                      }
                                                  }else {
                                                      Log.d("item", "Error: " + e.getMessage());
                                                  }
                                              }
            });



            queryUFriendReverse.findInBackground(new FindCallback<UserFriend>() {
                public void done(List<UserFriend> itemList, ParseException e) {
                    if (e == null) {
                        for (UserFriend userF : itemList) {

                            queryUFriendUpdateReverse.getInBackground(userF.getObjectId().toString(), new GetCallback<UserFriend>() {
                                public void done(UserFriend uFriend, ParseException e) {
                                    if (e == null) {
                                        uFriend.put("status", "Friend");
                                        uFriend.saveInBackground();
                                    } else {
                                        Log.d("item", "Error: " + e.getMessage());
                                    }
                                }
                            });
                        }
                    }else {
                        Log.d("item", "Error: " + e.getMessage());
                    }
                }
            });

            ((Button)v).setText("Friends");
            Toast.makeText(getContext(), "You are now Friends", Toast.LENGTH_SHORT).show();
        }

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

        if(person.get("status") != null)
        {
            viewHolder.binding.btnStatus.setText(person.get("status").toString());
        }else{
            viewHolder.binding.btnStatus.setText("Add");
        }
        viewHolder.binding.btnStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFriendToUser(person, viewHolder.binding.btnStatus );
            }
        });

    }
    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mPeople.size();
    }
}
