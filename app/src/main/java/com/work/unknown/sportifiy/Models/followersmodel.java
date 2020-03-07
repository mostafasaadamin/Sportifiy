package com.work.unknown.sportifiy.Models;

/**
 * Created by unknown on 2/10/2018.
 */

public class followersmodel  {
    String place;
    String Time;
    String Date;
    String sport;
    String follower_key;
    String Location;



    // public followersmodel(){}
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

    public String getFollower_key() {
        return follower_key;
    }

    public void setFollower_key(String follower_key) {
        this.follower_key = follower_key;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public followersmodel(String place, String time, String date, String sport, String follower_key, String location) {

        this.place = place;
        Time = time;
        Date = date;
        this.sport = sport;
        this.follower_key = follower_key;
        Location = location;
    }
}
