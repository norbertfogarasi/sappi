package com.lynxsolutions.intern.sappi.events;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by szabohunor on 25.07.2017.
 */

public class Event  implements Parcelable{
    private String imageUrl;
    private String date;
    private String title;
    private String UID;
    private String description;
    private String timestamp;
    private String location;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }



    public Event(String imageUrl, String date, String title, String UID, String description, String timestamp, String location) {
        this.imageUrl = imageUrl;
        this.date = date;
        this.title = title;
        this.UID = UID;
        this.description = description;
        this.timestamp = timestamp;
        this.location = location;
    }
    public Event()
    {};

    protected Event(Parcel in) {
        imageUrl = in.readString();
        date = in.readString();
        title = in.readString();
        UID = in.readString();
        description = in.readString();
        timestamp = in.readString();
        location = in.readString();
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imageUrl);
        parcel.writeString(date);
        parcel.writeString(title);
        parcel.writeString(UID);
        parcel.writeString(description);
        parcel.writeString(timestamp);
        parcel.writeString(location);
    }
}
