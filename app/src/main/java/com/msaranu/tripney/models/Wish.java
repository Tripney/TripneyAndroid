package com.msaranu.tripney.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by msaranu on 2/3/18.
 */
@org.parceler.Parcel
public class Wish implements  Parcelable{

    String wishID;
    String tripID;
    String name;
    String location;
    String duration;
    String type;
    Double price;

    public Wish(){}

    public Wish(String tripID,String wishID,String name, String location, String duration, String type, Double price){
        this.tripID=tripID;
        this.wishID=wishID;
        this.name=name;
        this.location=location;
        this.duration=duration;
        this.type=type;
        this.price=price;
    }

    static int wishno=0;

    protected Wish(Parcel in) {
        wishID = in.readString();
        tripID = in.readString();
        name = in.readString();
        location = in.readString();
        duration = in.readString();
        type = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(wishID);
        dest.writeString(tripID);
        dest.writeString(name);
        dest.writeString(location);
        dest.writeString(duration);
        dest.writeString(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Wish> CREATOR = new Creator<Wish>() {
        @Override
        public Wish createFromParcel(Parcel in) {
            return new Wish(in);
        }

        @Override
        public Wish[] newArray(int size) {
            return new Wish[size];
        }
    };

    public static ArrayList<Wish> createTempTrips(int numTrips){
        ArrayList<Wish> wishes = new ArrayList<Wish>();
        for(int i=1; i<=numTrips ;i++){
            wishes.add(new Wish(Integer.toString(1), Integer.toString(wishno++), "Wish YStone", "Wyoming", "2", "Hiking", 200d));
        }
        return  wishes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }


    public String getWishID() {
        return wishID;
    }

    public void setWishID(String wishID) {
        this.wishID = wishID;
    }

}
