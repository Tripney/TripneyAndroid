package com.msaranu.tripney.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by msaranu on 2/25/18.
 */
@ParseClassName("EventUser")
public class EventUser extends ParseObject implements Parcelable {

    String userID;
    String EventID;
    String status;

    public EventUser(){

    }

    public EventUser(Parcel in) {
        userID = in.readString();
        EventID = in.readString();
        status = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        loadInstanceVariables();
        dest.writeString(userID);
        dest.writeString(EventID);
        dest.writeString(status);
    }

    public void loadInstanceVariables() {
        userID = getString("userID");
        EventID = getString("EventID");
        status = getString("status");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EventUser> CREATOR = new Creator<EventUser>() {
        @Override
        public EventUser createFromParcel(Parcel in) {
            return new EventUser(in);
        }

        @Override
        public EventUser[] newArray(int size) {
            return new EventUser[size];
        }
    };

    public String getUserID() {
        return getString("userID");    }

    public void setUserID(String userID) {
        this.userID = userID;
        this.put("userID",userID);
    }

    public String getFriendID() {
        return getString("EventID");    }

    public void setFriendID(String EventID) {
        this.EventID = EventID;
        this.put("EventID",EventID);

    }

    public String getStatus() {
        return getString("status");    }

    public void setStatus(String status) {
        this.status = status;
        this.put("status",status);
    }
}
