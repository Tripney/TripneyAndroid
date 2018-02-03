package com.msaranu.tripney.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by msaranu on 1/14/18.
 */
@org.parceler.Parcel
public class Trip implements Parcelable {
          String mId;
          String mName;
          String mDate;
          String mStatus;
          String mbckgrndUrl;
          String mDescription;


    public Trip(String id, String name, String desc, String date, String status, String url){
        mId =id;
        mName=name;
        mDescription=desc;
        mDate=date;
        mStatus=status;
        mbckgrndUrl=url;
    }


    protected Trip(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mDate = in.readString();
        mStatus = in.readString();
        mbckgrndUrl = in.readString();
        mDescription = in.readString();
    }

    public static final Creator<Trip> CREATOR = new Creator<Trip>() {
        @Override
        public Trip createFromParcel(Parcel in) {
            return new Trip(in);
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getMbckgrndUrl() {
        return mbckgrndUrl;
    }

    public void setMbckgrndUrl(String mbckgrndUrl) {
        this.mbckgrndUrl = mbckgrndUrl;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public Trip(){

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mName);
        parcel.writeString(mDate);
        parcel.writeString(mStatus);
        parcel.writeString(mbckgrndUrl);
        parcel.writeString(mDescription);
    }

    static int triplast=0;
    public static ArrayList<Trip> createTempTrips(int numTrips){
        ArrayList<Trip> trips = new ArrayList<Trip>();
        for(int i=1; i<=numTrips ;i++){
            trips.add(new Trip(Integer.toString(triplast++), "Yellow Stone"," It is awesome", "01-01-2010", "NEW", "blahhhhhh URL"));
        }
        return  trips;
    }

}