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

    public String splitID;
    public String userID;
    public String eventID;
    public double amount;
    public String type;

    public Split(){}

    public Split(Parcel in) {
        userID = in.readString();
        eventID = in.readString();
        splitID = in.readString();
        amount = in.readDouble();
        type = in.readString();
    }


    public void loadInstanceVariables() {
        splitID = this.getObjectId();
        eventID =  getString("eventID");
        userID = getString("userID");
        type = getString("type");
        amount = getDouble("amount");
    }

        @Override
    public void writeToParcel(Parcel dest, int flags) {
        loadInstanceVariables();
        dest.writeString(userID);
        dest.writeString(eventID);
            dest.writeString(splitID);
            dest.writeDouble(amount);
        dest.writeString(type);

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

    public String getSplitID() {
        return getString("splitID");
    }

    public void setSplitID(String splitID) {
        this.splitID = splitID;
        this.put("splitID", splitID);
    }

    public double getAmount() {
        return getDouble("amount");
    }

    public void setAmount(double amount) {
        this.amount = amount;
        this.put("amount", amount);
    }

    public String getType() {
        return getString("type");
    }

    public void setType(String type) {
        this.type = type;
        this.put("type", type);
    }
}
