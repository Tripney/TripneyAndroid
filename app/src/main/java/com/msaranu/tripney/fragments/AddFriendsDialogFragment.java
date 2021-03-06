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
import android.view.WindowManager;
import android.widget.Button;

import com.msaranu.tripney.R;
import com.msaranu.tripney.adapters.AddFriendsToTripAdapter;
import com.msaranu.tripney.databinding.FragmentAddFriendsDialogBinding;
import com.msaranu.tripney.models.TripUser;
import com.msaranu.tripney.models.UserFriend;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFriendsDialogFragment extends DialogFragment {

    private static final String TRIPUSER_LIST = "tripuserlist_string";
    ArrayList<ParseUser> peopleList;
    ArrayList<TripUser> existsPeopleList;
    AddFriendsToTripAdapter adapter;
    String search_key;
    FragmentAddFriendsDialogBinding binding;
    ParseUser user;
    ArrayList<TripUser> tripUserList;
    String tripID;



    public AddFriendsDialogFragment() {
        // Required empty public constructor
    }



    public static AddFriendsDialogFragment newInstance(ArrayList<TripUser> tripUserList, String tripID) {
        AddFriendsDialogFragment addNewFriendsFragment = new AddFriendsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(TRIPUSER_LIST, tripUserList);
        args.putString("tripID", tripID);
        addNewFriendsFragment.setArguments(args);
        return addNewFriendsFragment;
    }

    public interface AddFriendsFragmentDialogListener{
        public void onFinishAddFriendsDialog(List<TripUser> tUserList);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_friends_dialog, container, false);
        View view = binding.getRoot();
        ButterKnife.bind(this, view);
        return view;
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            // Inflate the layout for this fragment
        existsPeopleList = getArguments().getParcelableArrayList(TRIPUSER_LIST);
        tripID = getArguments().getString("tripID");
        setRecyclerView(view);
        Button btnSave = (Button) view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (adapter.getTripUserList() != null && adapter.getTripUserList().size() > 0) {
                    for (TripUser pU : adapter.getTripUserList()) {
                        pU.setTripID(tripID);
                        pU.saveInBackground();
                    }
                }

                AddFriendsDialogFragment.AddFriendsFragmentDialogListener addFriendsFragmentDialogListener =
                        (AddFriendsDialogFragment.AddFriendsFragmentDialogListener) getTargetFragment();
                addFriendsFragmentDialogListener.onFinishAddFriendsDialog(adapter.getTripUserList());
                dismiss();

            }
        });

        //  fragmentAddNewFriends..setText(trip.getmDate());
    }

    private void setRecyclerView(View view) {

        //recycler view
        RecyclerView rvPeople = (RecyclerView) view.findViewById(R.id.rvPeople);
        peopleList = new ArrayList<ParseUser>();

        // Initialize contacts
        //events = Event.createTempEvents(20);
        //TODO: pass tripID and get corresponding events
       // Create adapter passing in the sample user data
        adapter = new AddFriendsToTripAdapter(this.getContext(), peopleList,existsPeopleList);
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
                        String friendID = userFriend.get("friendID").toString();

                        query.whereEqualTo("objectId", friendID);

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
