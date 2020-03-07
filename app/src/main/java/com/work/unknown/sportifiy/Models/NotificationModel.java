package com.work.unknown.sportifiy.Models;

/**
 * Created by unknown on 2/23/2018.
 */

public class NotificationModel {
    String status,Date,Location,Place,Sport,Time;
    String reject_Or_Accept_user_key;
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getDate() {
        return Date;
    }
    public void setDate(String date) {
        Date = date;
    }
    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public String getSport() {
        return Sport;
    }

    public void setSport(String sport) {
        Sport = sport;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getReject_Or_Accept_user_key() {
        return reject_Or_Accept_user_key;
    }
    public void setReject_Or_Accept_user_key(String reject_Or_Accept_user_key) {
        this.reject_Or_Accept_user_key = reject_Or_Accept_user_key;
    }
    public NotificationModel(String status, String date, String place, String sport, String time, String reject_Or_Accept_user_key) {
        this.status = status;
        Date = date;
        Place = place;
        Sport = sport;
        Time = time;
        this.reject_Or_Accept_user_key = reject_Or_Accept_user_key;
    }
}
