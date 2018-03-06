package com.msaranu.tripney.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.msaranu.tripney.R;
import com.msaranu.tripney.databinding.ItemSplitBinding;
import com.msaranu.tripney.decorators.ItemClickSupport;
import com.msaranu.tripney.models.Event;
import com.msaranu.tripney.models.Split;
import com.msaranu.tripney.models.User;
import com.msaranu.tripney.utilities.DateUtils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by msaranu on 1/14/18.
 */

public class SplitRecyclerAdapter extends
        RecyclerView.Adapter<SplitRecyclerAdapter.ViewHolder> {

    RecyclerView mRecyclerView;
    // Store a member variable for the contacts
    private List<Split> mSplits;
    Double totalBefore=0.0;
    // Store the context for easy access
    private Context mContext;
    // Pass in the contact array into the constructor
    public SplitRecyclerAdapter(Context context, List<Split> splits, Double totalBefore) {
        mSplits = splits;
        mContext = context;
        this.totalBefore=totalBefore;
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
        final ItemSplitBinding binding;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            binding = ItemSplitBinding.bind(itemView);
        }


    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public SplitRecyclerAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        final View splitView = inflater.inflate(R.layout.item_split, parent, false);

        // Return a new holder instance
        final ViewHolder viewHolder = new ViewHolder(splitView);

        return viewHolder;

    }


    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(SplitRecyclerAdapter.ViewHolder viewHolder, int position) {

        String firstName="";
        String lastName="";
        // Get the data model based on position
        Split splitPos = mSplits.get(position);

        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        String userID = splitPos.get("userID").toString();
        query.whereEqualTo("objectId", userID);

        // Execute the find asynchronously
        query.findInBackground(new FindCallback<User>() {
            public void done(List<User> itemList, ParseException e) {
                if (e == null) {
                    for(User user : itemList){
                        viewHolder.binding.tvName.setText( user.getFirstName() + " " + user.getLastName());
                    }
                    Toast.makeText(getContext(), "Message", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
            }
        });


        viewHolder.binding.etSplit.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Double restAmount=0.0;
                Double editSplit = Double.valueOf(viewHolder.binding.etSplit.getText().toString());
                for(Split split : mSplits){
                    if(splitPos.getObjectId()!=split.getObjectId()){
                        restAmount += split.amount;
                    }
                }

                if(restAmount + editSplit != totalBefore ){
                    if(restAmount + editSplit > totalBefore ){
                        String message = "Amount exceeded by $" + (restAmount + editSplit- totalBefore);
                        Toast.makeText(getContext(), message
                                 , Toast.LENGTH_SHORT).show();
                    }

                    if(restAmount + editSplit < totalBefore ){
                        String message = "Amount remaining " + (totalBefore-restAmount + editSplit);
                        Toast.makeText(getContext(), message
                                , Toast.LENGTH_SHORT).show();
                    }
                }else{
                    mSplits.get(position).setAmount(editSplit);
                    //ToDO Add to the DB directly
                }
            }
        });


    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mSplits.size();
    }
}
