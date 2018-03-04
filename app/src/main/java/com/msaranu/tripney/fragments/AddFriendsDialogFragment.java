package com.msaranu.tripney.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.msaranu.tripney.R;
import com.msaranu.tripney.adapters.AddFriendsToTripAdapter;
import com.msaranu.tripney.adapters.SearchAddFriendsAdapter;
import com.msaranu.tripney.databinding.FragmentAddFriendsDialogBinding;
import com.msaranu.tripney.databinding.FragmentAddNewFriendsBinding;
import com.msaranu.tripney.models.User;
import com.msaranu.tripney.models.UserFriend;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFriendsDialogFragment extends DialogFragment {

    private static final String SEARCH_STRING = "search_string";
    ArrayList<ParseUser> peopleList;
    AddFriendsToTripAdapter adapter;
    String search_key;
    FragmentAddFriendsDialogBinding binding;
    ParseUser user;



    public AddFriendsDialogFragment() {
        // Required empty public constructor
    }

    public interface AddFriendsFragmentDialogListener{
        public void onFinishAddFriendsDialog();
    }


    public static AddFriendsDialogFragment newInstance(String searchStr) {
        AddFriendsDialogFragment addNewFriendsFragment = new AddFriendsDialogFragment();
        Bundle args = new Bundle();
        args.putString(SEARCH_STRING, searchStr);
        addNewFriendsFragment.setArguments(args);
        return addNewFriendsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
  //      if (SEARCH_STRING != null)
//            search_key = getArguments().getString(SEARCH_STRING);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_friends_dialog, parent, false);
        View view = binding.getRoot();
        ButterKnife.bind(this, view);
        setRecyclerView(view);
        Button btnSave = (Button) view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFriendsDialogFragment.AddFriendsFragmentDialogListener addFriendsFragmentDialogListener =
                        (AddFriendsDialogFragment.AddFriendsFragmentDialogListener) getTargetFragment();
                addFriendsFragmentDialogListener.onFinishAddFriendsDialog();
                dismiss();

            }
        });

        //  fragmentAddNewFriends..setText(trip.getmDate());
        return view;
    }

    private void setRecyclerView(View view) {

        //recycler view
        RecyclerView rvPeople = (RecyclerView) view.findViewById(R.id.rvPeople);
        peopleList = new ArrayList<ParseUser>();

        // Initialize contacts
        //events = Event.createTempEvents(20);
        //TODO: pass tripID and get corresponding events
        // Create adapter passing in the sample user data
        adapter = new AddFriendsToTripAdapter(this.getContext(), peopleList);
        // Attach the adapter to the recyclerview to populate items
        rvPeople.setAdapter(adapter);
        // Set layout manager to position the items
        rvPeople.setLayoutManager(new LinearLayoutManager(this.getContext()));

        ParseQuery<ParseUser> query = ParseUser.getQuery();

        ParseQuery<UserFriend> UFquery = ParseQuery.getQuery(UserFriend.class);
        UFquery.whereEqualTo("userID", ParseUser.getCurrentUser().getObjectId());


        // Execute the find asynchronously
        UFquery.findInBackground(new FindCallback<UserFriend>() {
            public void done(List<UserFriend> itemList, ParseException e) {
                if (e == null) {
                    for(UserFriend userFriend : itemList){
                        String friendID = userFriend.getFriendID();

                        query.whereEqualTo("userID", friendID);

                        query.findInBackground(new FindCallback<ParseUser>() {
                            public void done(List<ParseUser> itemList, ParseException e) {
                                if (e == null) {
                                    for(ParseUser userF : itemList){
                                        peopleList.add(userF);
                                    }
                                }
                                else {
                                    Log.d("item", "Error: " + e.getMessage());
                                }
                                adapter.notifyDataSetChanged();
                            }

                        });
                    }
//                    Toast.makeText(getContext(), "Message", Toast.LENGTH_SHORT).show();
                    // adapter.notifyDataSetChanged();
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
            }
        });

    }


}
