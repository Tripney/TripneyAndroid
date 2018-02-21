package com.msaranu.tripney.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * Created by msaranu on 2/3/18.
 */
@ParseClassName("Event")
public class Event extends ParseObject implements Parcelable{

    public String eventID;
    public String tripID;
    public String name;
    public String location;
    public String duration;

    public String type;
    public String date;
    public Double price;

    public Event(){

    }

    public Event(String tripID,String eventID,String name, String location, String duration, String type, Double price, String date){
        this.tripID=tripID;
        this.eventID=eventID;
        this.name=name;
        this.location=location;
        this.duration=duration;
        this.type=type;
        this.price=price;
        this.date=date;
    }

    static int eventno=0;

    protected Event(Parcel in) {
        eventID = in.readString();
        tripID = in.readString();
        name = in.readString();
        location = in.readString();
        duration = in.readString();
        type = in.readString();
        date = in.readString();
        price = in.readDouble();
    }

    public void loadInstanceVariables(){
        eventID = getObjectId();
        tripID = getString("tripID");
        name = getString("name");
        location = getString("location");
        duration = getString("duration");
        type = getString("type");
        date=getString("date");
        price=getDouble("price");


    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        loadInstanceVariables();
        dest.writeString(eventID);
        dest.writeString(tripID);
        dest.writeString(name);
        dest.writeString(location);
        dest.writeString(duration);
        dest.writeString(type);
        dest.writeString(date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public static ArrayList<Event> createTempEvents(int numTrips){
        ArrayList<Event> events = new ArrayList<Event>();
        for(int i=1; i<=numTrips ;i++){
         //   events.add(new Event(Integer.toString(1), Integer.toString(eventno++), "Event YStone", "Wyoming", "2", "Hiking", 200d));
        }
        return  events;
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        this.name = name;
        this.put("name",name);
    }

    public String getLocation() {
        return getString("location");
    }

    public void setLocation(String location) {
        this.location = location;
        this.put("location",location);
    }

    public String getDuration() {
        return getString("duration");
    }

    public void setDuration(String duration) {
        this.duration = duration;
        this.put("duration",duration);
    }

    public String getType() {
        return getString("type");
    }

    public void setType(String type) {
        this.type = type;
        this.put("type",type);
    }

    public Double getPrice() {
        return getDouble("price");
    }

    public void setPrice(Double price) {
        this.price = price;
        this.put("price",price);
    }


    public String getEventID() {
        return getString("eventID");
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
        this.put("eventID",eventID);
    }

    public String getDate() {
        return getString("date");
    }

    public void setDate(String date) {
        this.date = date;
        this.put("date",date);
    }


    public String getTripID() {
        return getString("tripID");
    }

    public void setTripID(String tripID) {
        this.tripID = tripID;
        this.put("tripID",tripID);
    }



}
