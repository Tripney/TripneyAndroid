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


    public String _id;
    public  String mName;
    public   String mDate;
    public String mStatus;
    public String mbckgrndUrl;
    public String mDescription;
    public String mUserID;


    public Trip(String id, String name, String desc, String date, String status, String url){
        _id =id;
        mName=name;
        mDescription=desc;
        mDate=date;
        mStatus=status;
        mbckgrndUrl=url;
    }


    protected Trip(Parcel in) {
        _id = in.readString();
        mName = in.readString();
        mDate = in.readString();
        mStatus = in.readString();
        mbckgrndUrl = in.readString();
        mDescription = in.readString();
        mUserID = in.readString();
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

    public String get_id() {
        return  getObjectId();
    }

    public void set_id(String _id) {
        this._id=_id;
        this.put("_id",_id);
    }

    public String getmName() {
        return  getString("mName");
    }

    public void setmName(String mName) {
        this.mName = mName;
        this.put("mName",mName);
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
        _id = getObjectId();
        mName = getString("mName");
        mDescription = getString("mDescription");
        mDate = getString("mDate");
        mStatus = getString("mStatus");
        mUserID=getString("mUserID");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        loadInstanceVariables();
        parcel.writeString(_id);
        parcel.writeString(mName);
        parcel.writeString(mDate);
        parcel.writeString(mStatus);
        parcel.writeString(mbckgrndUrl);
        parcel.writeString(mDescription);
        parcel.writeString(mUserID);
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