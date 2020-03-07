package com.work.unknown.sportifiy.RecyclesModels;

/**
 * Created by unknown on 2/6/2018.
 */

public class CreatedMatchModel  {
    String Sport;
    String Date;
    String Time;
    String Place;
    String location;

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

    public CreatedMatchModel(){}
    public CreatedMatchModel(String sport, String date, String time, String place, String location) {
        Sport = sport;
        Date = date;
        Time = time;
        Place = place;
        this.location = location;
    }

}
