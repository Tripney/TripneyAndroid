package com.msaranu.tripney.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by msaranu on 2/25/18.
 */
@ParseClassName("UserFriend")
public class UserFriend extends ParseObject implements Parcelable {

    String userID;
    String friendID;
    String status;

    public UserFriend(){

    }

    public UserFriend(Parcel in) {
        userID = in.readString();
        friendID = in.readString();
        status = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        loadInstanceVariables();
        dest.writeString(userID);
        dest.writeString(friendID);
        dest.writeString(status);
    }

    public void loadInstanceVariables() {
        userID = getString("userID");
        friendID = getString("friendID");
        status = getString("status");
    }

        @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserFriend> CREATOR = new Creator<UserFriend>() {
        @Override
        public UserFriend createFromParcel(Parcel in) {
            return new UserFriend(in);
        }

        @Override
        public UserFriend[] newArray(int size) {
            return new UserFriend[size];
        }
    };

    public String getUserID() {
        return getString("userID");    }

    public void setUserID(String userID) {
        this.userID = userID;
        this.put("userID",userID);
    }

    public String getFriendID() {
        return getString("friendID");    }

    public void setFriendID(String friendID) {
        this.friendID = friendID;
        this.put("friendID",friendID);

    }

    public String getStatus() {
        return getString("status");    }

    public void setStatus(String status) {
        this.status = status;
        this.put("status",status);
    }
}
