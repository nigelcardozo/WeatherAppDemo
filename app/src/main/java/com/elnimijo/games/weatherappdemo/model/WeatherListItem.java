package com.elnimijo.games.weatherappdemo.model;

import com.google.gson.annotations.SerializedName;

public class WeatherListItem {

    private String id;
    private String name;
    private String dt;

    public Coordinates coord;
    public SystemInfo sys;
    public Weather[] weather;
    public MainInfo main;
    public Wind wind;

    public String getId() {
        return id;
    }

    public String getCityName() {
        return name;
    }

    public String getDt() {
        return dt;
    }
}
