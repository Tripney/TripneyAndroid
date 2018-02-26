package com.msaranu.tripney.models;

import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by msaranu on 2/3/18.
 */
@ParseClassName("User")
public class User extends ParseObject implements Parcelable{

    String userID;
    String firstName;
    String lastName;
    String email;
    String phone;
    String address1;
    String address2;
    String city;
    String state;
    String zip;
    String profilePicture;

    public User(){}

    protected User(android.os.Parcel in) {
        userID = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        phone = in.readString();
        address1 = in.readString();
        address2 = in.readString();
        city = in.readString();
        state = in.readString();
        zip = in.readString();
        profilePicture = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(android.os.Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };


    public void loadInstanceVariables() {
        userID = getString("userID");
        firstName = getString("firstName");
        lastName = getString("lastName");
        email = getString("email");
        phone = getString("phone");
        address1 = getString("address1");
        address2 = getString("address2");
        city = getString("city");
        state = getString("state");
        zip = getString("zip");
        profilePicture = getString("profilePicture");
    }
    public String getUserID() {
        return getString("userID");
    }

    public void setUserID(String userID) {
        this.userID = userID;
        this.put("userID",userID);
    }

    public String getFirstName() {
        return getString("firstName");
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        this.put("firstName", firstName);
    }

    public String getLastName() {
        return getString("lastName");
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        this.put("lastName", lastName);
    }

    public String getEmail() {
        return getString("email");
    }

    public void setEmail(String email) {
        this.email = email;
        this.put("email",email);
    }

    public String getPhone() {
        return getString("phone");
    }

    public void setPhone(String phone) {
        this.phone = phone;
        this.put("phone",phone);
    }

    public String getAddress1() {
        return getString("address1");
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
        this.put("address1", address1);
    }

    public String getAddress2() {
        return getString("address2");
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
        this.put("address2", address2);
    }

    public String getCity() {
        return getString("city");
    }

    public void setCity(String city) {
        this.city = city;
        this.put("city",city);
    }

    public String getState() {
        return getString("state");
    }

    public void setState(String state) {
        this.state = state;
        this.put("state",state);
    }

    public String getZip() {
        return getString("zip");
    }

    public void setZip(String zip) {
        this.zip = zip;
        this.put("zip", zip);
    }


    public String getProfilePicture() {
        return getString("profilePicture");
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
        this.put("profilePicture", profilePicture);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel parcel, int i) {
        loadInstanceVariables();
        parcel.writeString(userID);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(email);
        parcel.writeString(phone);
        parcel.writeString(address1);
        parcel.writeString(address2);
        parcel.writeString(city);
        parcel.writeString(state);
        parcel.writeString(zip);
        parcel.writeString(profilePicture);
    }
}