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

    String eventID;
    String tripID;
    String name;
    String location;
    String duration;
    String type;
    Double price;

    public Event(){

    }

    public Event(String tripID,String eventID,String name, String location, String duration, String type, Double price){
        this.tripID=tripID;
        this.eventID=eventID;
        this.name=name;
        this.location=location;
        this.duration=duration;
        this.type=type;
        this.price=price;
    }

    static int eventno=0;

    public Event(Parcel in) {
        eventID = in.readString();
        tripID = in.readString();
        name = in.readString();
        location = in.readString();
        duration = in.readString();
        type = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(eventID);
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
            events.add(new Event(Integer.toString(1), Integer.toString(eventno++), "Event YStone", "Wyoming", "2", "Hiking", 200d));
        }
        return  events;
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


    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

}
