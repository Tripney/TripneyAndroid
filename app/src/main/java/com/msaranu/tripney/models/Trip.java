package com.msaranu.tripney.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * Created by msaranu on 1/14/18.
 */
@ParseClassName("Trip")
public class Trip extends ParseObject implements Parcelable {


    public String tripID;
    public  String mName;
    public   String mDate;
    public String mStatus;
    public String mbckgrndUrl;
    public String mDescription;
    public String mUserID;
    public String mlocation;


    public Trip(String id, String name, String desc, String date, String status, String url){
        tripID =id;
        mName=name;
        mDescription=desc;
        mDate=date;
        mStatus=status;
        mbckgrndUrl=url;
    }


    protected Trip(Parcel in) {
        tripID = in.readString();
        mName = in.readString();
        mDate = in.readString();
        mStatus = in.readString();
        mbckgrndUrl = in.readString();
        mDescription = in.readString();
        mUserID = in.readString();
        mlocation = in.readString();
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


    public String getmName() {
        return  getString("mName");
    }

    public void setmName(String mName) {
        this.mName = mName;
        this.put("mName",mName);
    }

    public String getTripID() {
        return  getObjectId();
    }

    public void setTripID(String tripID) {
        this.tripID=tripID;
        this.put("tripID",tripID);
    }


    public String getmDate() {
        return  getString("mDate");
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
        this.put("mDate", mDate);
    }

    public String getmStatus() {
        return  getString("mStatus");
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
        this.put("mStatus", mStatus);
    }

    public String getMbckgrndUrl() {
        return  getString("mbckgrndUrl");
    }

    public void setMbckgrndUrl(String mbckgrndUrl) {
        this.mbckgrndUrl = mbckgrndUrl;
        this.put("mbckgrndUrl", mbckgrndUrl);
    }

    public String getmDescription() {
        return  getString("mDescription");
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
        this.put("mDescription", mDescription);
    }


    public String getmLocation() {
        return  getString("mlocation");
    }

    public void setmLocation(String mlocation) {
        this.mlocation = mlocation;
        this.put("mlocation", mlocation);
    }

    public String getmUserID() {
        return getString("mUserID");
    }

    public void setmUserID(String mUserID) {
        this.mUserID = mUserID;
        this.put("mUserID", mUserID);
    }

    public Trip(){

    }

    public void loadInstanceVariables(){
        tripID = getObjectId();
        mName = getString("mName");
        mDescription = getString("mDescription");
        mDate = getString("mDate");
        mStatus = getString("mStatus");
        mUserID=getString("mUserID");
        mlocation=getString("mlocation");
        mbckgrndUrl = getString("mbckgrndUrl");

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        loadInstanceVariables();
        dest.writeString(tripID);
        dest.writeString(mName);
        dest.writeString(mDate);
        dest.writeString(mStatus);
        dest.writeString(mbckgrndUrl);
        dest.writeString(mDescription);
        dest.writeString(mUserID);
        dest.writeString(mlocation);
    }

    @Override
    public int describeContents() {
        return 0;
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