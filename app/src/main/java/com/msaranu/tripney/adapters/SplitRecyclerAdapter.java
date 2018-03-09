package com.msaranu.tripney.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.msaranu.tripney.R;
import com.msaranu.tripney.databinding.ItemSplitBinding;
import com.msaranu.tripney.databinding.ItemSplitEqualBinding;
import com.msaranu.tripney.decorators.ItemClickSupport;
import com.msaranu.tripney.models.Event;
import com.msaranu.tripney.models.Split;
import com.msaranu.tripney.models.User;
import com.msaranu.tripney.utilities.DateUtils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by msaranu on 1/14/18.
 */

public class SplitRecyclerAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String splitType;
    RecyclerView mRecyclerView;
    // Store a member variable for the contacts
    private List<Split> mSplits;
    Double eventPrice = 0.0;
    // Store the context for easy access
    private Context mContext;

    // Pass in the contact array into the constructor
    public SplitRecyclerAdapter(Context context, List<Split> splits, Double eventPrice, String splitType) {
        mSplits = splits;
        mContext = context;
        this.eventPrice = eventPrice;
        this.splitType = splitType;
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
    public static class ViewHolderPercentage extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        final ItemSplitBinding binding;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolderPercentage(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            binding = ItemSplitBinding.bind(itemView);
        }

    }


    public static class ViewHolderAmount extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        final ItemSplitBinding binding;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolderAmount(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            binding = ItemSplitBinding.bind(itemView);
        }


    }


    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolderEqual extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        final ItemSplitEqualBinding binding;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolderEqual(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            binding = ItemSplitEqualBinding.bind(itemView);
        }


    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Return a new holder instance
        RecyclerView.ViewHolder viewHolder = null;
        View splitView;

        // Inflate the custom layout


        if (splitType == "PERCENTAGE") {
            splitView = inflater.inflate(R.layout.item_split, parent, false);
            viewHolder = new ViewHolderPercentage(splitView);
            return viewHolder;

        } else if (splitType == "AMOUNT")
            {
            splitView = inflater.inflate(R.layout.item_split, parent, false);
            viewHolder = new ViewHolderAmount(splitView);
            return viewHolder;

        }else if (splitType == "EQUAL"){
                splitView = inflater.inflate(R.layout.item_split_equal, parent, false);
                viewHolder = new ViewHolderEqual(splitView);
                return viewHolder;
        }else{
            return viewHolder;

        }

    }


    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        String firstName = "";
        String lastName = "";
        // Get the data model based on position
        Split splitPos = mSplits.get(position);

        if(splitPos == null){
            splitPos = new Split();
        }

        switch (splitType) {
            case "AMOUNT":
                SplitRecyclerAdapter.ViewHolderAmount viewHolderAmount = (SplitRecyclerAdapter.ViewHolderAmount) viewHolder;
                configureVHAmount(viewHolderAmount, splitPos, position);
                break;
            case "PERCENTAGE":
                SplitRecyclerAdapter.ViewHolderPercentage viewHolderPercentage = (SplitRecyclerAdapter.ViewHolderPercentage) viewHolder;
                configureVHPercentage(viewHolderPercentage, splitPos, position);
                break;
            case "EQUAL":
                SplitRecyclerAdapter.ViewHolderEqual viewHolderEqual = (SplitRecyclerAdapter.ViewHolderEqual) viewHolder;
                configureVHEqual(viewHolderEqual, splitPos, position);
                break;
            default:
                    break;

        }
    }

    private void configureVHPercentage(ViewHolderPercentage viewHolder, Split splitPos, int position) {


        Double percentAmount = (splitPos.getAmount()/eventPrice)*100.0;
        viewHolder.binding.etSplit.setText(percentAmount.toString());

        setUserName(viewHolder,splitPos);

        viewHolder.binding.etSplit.addTextChangedListener(new TextWatcher() {

            int totalPercentage =100;

            public void afterTextChanged(Editable s) {

                Double restAmount=0.0;
                Double editSplit = Double.valueOf(s.toString());
                for(Split split : mSplits){
                    if(splitPos.getObjectId()!=split.getObjectId()){
                        restAmount += split.amount;
                    }
                }

                if(restAmount + editSplit != totalPercentage ){
                    if(restAmount + editSplit > totalPercentage ){
                        String message = "Percent exceeded by " + ((restAmount + editSplit)- totalPercentage);
                        Toast.makeText(getContext(), message
                                , Toast.LENGTH_SHORT).show();
                    }

                    if(restAmount + editSplit < totalPercentage ){
                        String message = "Percent remaining " + (totalPercentage- (restAmount + editSplit));
                        Toast.makeText(getContext(), message
                                , Toast.LENGTH_SHORT).show();
                    }
                }
                    mSplits.get(position).setAmount(editSplit);
                    mSplits.get(position).setType("P");

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
    }


    private void configureVHAmount(ViewHolderAmount viewHolder, Split splitPos, int position) {

        Double byAmount = splitPos.getAmount();
        viewHolder.binding.etSplit.setText(byAmount.toString());

        setUserName(viewHolder,splitPos);


        viewHolder.binding.etSplit.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                Double restAmount=0.0;
                Double editSplit = Double.valueOf(s.toString());
                for(Split split : mSplits){
                    if(splitPos.getObjectId()!=split.getObjectId()){
                        restAmount += split.amount;
                    }
                }

                if(restAmount + editSplit != eventPrice ){
                    if(restAmount + editSplit > eventPrice ){
                        String message = "Amount exceeded by $" + (restAmount + (editSplit- eventPrice));
                        Toast.makeText(getContext(), message
                                , Toast.LENGTH_SHORT).show();
                    }

                    if(restAmount + editSplit < eventPrice ){
                        String message = "Amount remaining " + (eventPrice- (restAmount + editSplit));
                        Toast.makeText(getContext(), message
                                , Toast.LENGTH_SHORT).show();
                    }
                }
                    mSplits.get(position).setAmount(editSplit);
                    mSplits.get(position).setType("A");

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
    }




    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mSplits.size();
    }

    private void configureVHEqual(ViewHolderEqual viewHolder, Split splitPos, int position) {

        Double equalAmount = splitPos.getAmount();
        setUserName(viewHolder,splitPos);

        if(equalAmount ==0) {
            mSplits.get(position).setAmount(eventPrice/2);
        }
        mSplits.get(position).setType("E");

    }


    void setUserName(final RecyclerView.ViewHolder viewHolder , Split splitPos){

        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        String userID = splitPos.get("userID").toString();
        query.whereEqualTo("objectId", userID);

        // Execute the find asynchronously
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> itemList, ParseException e) {
                if (e == null) {
                    for (ParseUser user : itemList) {


                        if(splitPos.getType() == "E"){
                            SplitRecyclerAdapter.ViewHolderEqual vH = (ViewHolderEqual) viewHolder;
                            vH.binding.tvName.setText(user.get("firstName").toString() + " " + user.get("lastName").toString());

                        }else  if(splitPos.getType() == "P"){
                            SplitRecyclerAdapter.ViewHolderPercentage vH = (ViewHolderPercentage) viewHolder;
                            vH.binding.tvName.setText(user.get("firstName").toString() + " " + user.get("lastName").toString());
                        }else  if(splitPos.getType() == "A"){
                            SplitRecyclerAdapter.ViewHolderAmount vH = (ViewHolderAmount) viewHolder;
                            vH.binding.tvName.setText(user.get("firstName").toString() + " " + user.get("lastName").toString());
                        }
                    }
                    Toast.makeText(getContext(), "Message", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
            }
        });
    }
}
