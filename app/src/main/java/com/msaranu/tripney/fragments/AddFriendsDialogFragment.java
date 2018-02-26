package com.msaranu.tripney.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.msaranu.tripney.R;
import com.msaranu.tripney.models.User;

import java.util.List;

public class AddFriendsDialogFragment extends DialogFragment {

    public AddFriendsDialogFragment() {
        // Required empty public constructor
    }


    public static AddFriendsDialogFragment newInstance(String param1, String param2) {
        AddFriendsDialogFragment fragment = new AddFriendsDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
         }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_friends_dialog, container, false);
    }


    public interface AddFriendsFragmentDialogListener {
        void onFinishAddFriendsDialog(List<User> users);
    }




}
