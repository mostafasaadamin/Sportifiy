package com.work.unknown.sportifiy.RecyclesModels;

import java.io.Serializable;

/**
 * Created by unknown on 2/6/2018.
 */

public class searchmodel implements Serializable {
    String Sport;
    String Date;
    String Time;
    String Place;
    String location;
    String Key;

    public String getSport() {
        return Sport;
    }

    public void setSport(String sport) {
        Sport = sport;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public searchmodel(String sport, String date, String time, String place, String location, String key) {

        Sport = sport;
        Date = date;
        Time = time;
        Place = place;
        this.location = location;
        Key = key;
    }
}
