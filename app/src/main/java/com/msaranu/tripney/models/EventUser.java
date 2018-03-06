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
    String eventID;
    String status;

    public EventUser(){

    }

    public EventUser(Parcel in) {
        userID = in.readString();
        eventID = in.readString();
        status = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        loadInstanceVariables();
        dest.writeString(userID);
        dest.writeString(eventID);
        dest.writeString(status);
    }

    public void loadInstanceVariables() {
        userID = getString("userID");
        eventID = getString("eventID");
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

    public String getEventID() {
        return getString("eventID");    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
        this.put("eventID",eventID);

    }

    public String getStatus() {
        return getString("status");    }

    public void setStatus(String status) {
        this.status = status;
        this.put("status",status);
    }
}
