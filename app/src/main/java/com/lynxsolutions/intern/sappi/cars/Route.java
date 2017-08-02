package com.lynxsolutions.intern.sappi.cars;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by szabohunor on 27.07.2017.
 */

public class Route implements Parcelable{
    private String from;
    private String to;
    private String from_to;
    private String description;
    private String phonenumber;
    private String uid;
    private String date;
    private String username;

    public Route(String from, String to, String description, String phonenumber, String uid, String date, String username) {
        this.from = from;
        this.to = to;
        this.from_to = from + "_" + to;
        this.description = description;
        this.phonenumber = phonenumber;
        this.uid = uid;
        this.date = date;
        this.username = username;
    }

    protected Route(Parcel in) {
        from = in.readString();
        to = in.readString();
        from_to = in.readString();
        description = in.readString();
        phonenumber = in.readString();
        uid = in.readString();
        date = in.readString();
        username = in.readString();
    }

    public static final Creator<Route> CREATOR = new Creator<Route>() {
        @Override
        public Route createFromParcel(Parcel in) {
            return new Route(in);
        }

        @Override
        public Route[] newArray(int size) {
            return new Route[size];
        }
    };

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Route() {}

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom_to() {
        return from_to;
    }

    public void setFrom_to(String from_to) {
        this.from_to = from_to;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(from);
        parcel.writeString(to);
        parcel.writeString(from_to);
        parcel.writeString(description);
        parcel.writeString(phonenumber);
        parcel.writeString(uid);
        parcel.writeString(date);
        parcel.writeString(username);
    }
}
