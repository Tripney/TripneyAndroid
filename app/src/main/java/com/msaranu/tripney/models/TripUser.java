package com.msaranu.tripney.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by msaranu on 2/25/18.
 */
@ParseClassName("TripUser")
public class TripUser extends ParseObject implements Parcelable {

    String userID;
    String tripID;
    String status;

    public TripUser(){

    }

    public TripUser(Parcel in) {
        userID = in.readString();
        tripID = in.readString();
        status = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        loadInstanceVariables();
        dest.writeString(userID);
        dest.writeString(tripID);
        dest.writeString(status);
    }

    public void loadInstanceVariables() {
        userID = getString("userID");
        tripID = getString("tripID");
        status = getString("status");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TripUser> CREATOR = new Creator<TripUser>() {
        @Override
        public TripUser createFromParcel(Parcel in) {
            return new TripUser(in);
        }

        @Override
        public TripUser[] newArray(int size) {
            return new TripUser[size];
        }
    };

    public String getUserID() {
        return getString("userID");    }

    public void setUserID(String userID) {
        this.userID = userID;
        this.put("userID",userID);
    }

    public String getFriendID() {
        return getString("tripID");    }

    public void setFriendID(String tripID) {
        this.tripID = tripID;
        this.put("tripID",tripID);

    }

    public String getStatus() {
        return getString("status");    }

    public void setStatus(String status) {
        this.status = status;
        this.put("status",status);
    }
}
