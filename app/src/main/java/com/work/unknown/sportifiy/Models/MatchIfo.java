package com.work.unknown.sportifiy.Models;

/**
 * Created by unknown on 2/5/2018.
 */

public class MatchIfo {
    String Sport,Date,Time,Place,location,Note;

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

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public MatchIfo(String sport, String date, String time, String place, String location, String note) {
        Sport = sport;
        Date = date;
        Time = time;
        Place = place;
        this.location = location;
        Note = note;
    }
}
