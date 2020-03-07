package com.work.unknown.sportifiy.Models;

/**
 * Created by unknown on 2/1/2018.
 */

public class userinfo {
    String username;
    String Firstname;
    String lastname;
    String gender;
    String address;
    String phone;
    String bio;
    String imageurl;
    String day;
    String month;
    String year;
    String favouritlist;
public userinfo(){}
    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return Firstname;
    }

    public void setFirstname(String firstname) {
        Firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getFavouritlist() {
        return favouritlist;
    }

    public void setFavouritlist(String favouritlist) {
        this.favouritlist = favouritlist;
    }

    public userinfo(String username, String firstname, String lastname, String gender, String address, String phone, String bio, String day, String month, String year, String favouritlist,String imageurl) {
        this.username = username;
        Firstname = firstname;
        this.lastname = lastname;
        this.gender = gender;
        this.address = address;
        this.phone = phone;
        this.bio = bio;
        this.day = day;
        this.month = month;
        this.year = year;
        this.favouritlist = favouritlist;
    this.imageurl=imageurl;
    }


}
