package com.work.unknown.sportifiy.Models;

/**
 * Created by unknown on 6/4/2018.
 */

public class followingModel {
    String place;
    String Time;
    String Date;
    String sport;
    String creator_key;
    String Location;

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getCreator_key() {
        return creator_key;
    }

    public void setCreator_key(String creator_key) {
        this.creator_key = creator_key;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public followingModel(String place, String time, String date, String sport, String creator_key, String location) {

        this.place = place;
        Time = time;
        Date = date;
        this.sport = sport;
        this.creator_key = creator_key;
        Location = location;
    }
}
