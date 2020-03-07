package com.work.unknown.sportifiy.Models;

/**
 * Created by unknown on 2/5/2018.
 */

public class SearchModel {
    String Date,Area,Game;
    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getGame() {
        return Game;
    }

    public void setGame(String game) {
        Game = game;
    }

    public SearchModel(String date, String area, String game) {
        Date = date;
        Area = area;
        Game = game;
    }
}
