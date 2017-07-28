package com.lynxsolutions.intern.sappi.events;

/**
 * Created by szabohunor on 25.07.2017.
 */

public class Event {
    private String imageUrl;
    private String date;
    private String title;
    private String UID;
    private String description;

    public Event(String imageUrl, String date, String title, String UID, String description) {
        this.imageUrl = imageUrl;
        this.date = date;
        this.title = title;
        this.UID = UID;
        this.description = description;
    }
    public Event()
    {};

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
}
