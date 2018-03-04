package com.msaranu.tripney.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by msaranu on 3/3/18.
 */

@ParseClassName("Split")
public class Split extends ParseObject implements Parcelable {

    public String userID;
    public String eventID;
    public double amount;

    public Split(){}

    public Split(Parcel in) {
        userID = in.readString();
        eventID = in.readString();
        amount = in.readDouble();
    }


    public void loadInstanceVariables() {
        eventID =  getString("eventID");
        userID = getString("userID");
        amount = getDouble("amount");
    }

        @Override
    public void writeToParcel(Parcel dest, int flags) {
        loadInstanceVariables();
        dest.writeString(userID);
        dest.writeString(eventID);
        dest.writeDouble(amount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Split> CREATOR = new Creator<Split>() {
        @Override
        public Split createFromParcel(Parcel in) {
            return new Split(in);
        }

        @Override
        public Split[] newArray(int size) {
            return new Split[size];
        }
    };

    public String getUserID() {
        return getString("userID");
    }

    public void setUserID(String userID) {
        this.userID = userID;
        this.put("userID", userID);

    }

    public String getEventID() {
        return getString("eventID");
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
        this.put("eventID", eventID);
    }

    public double getAmount() {
        return getDouble("amount");
    }

    public void setAmount(double amount) {
        this.amount = amount;
        this.put("amount", amount);
    }
}
