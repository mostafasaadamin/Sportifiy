package com.work.unknown.sportifiy.Models;

/**
 * Created by unknown on 2/23/2018.
 */

public class AlluserModel {
    String username;
    String key;
    String imageurl;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public AlluserModel(String username, String key, String imageurl) {

        this.username = username;
        this.key = key;
        this.imageurl = imageurl;
    }
}
