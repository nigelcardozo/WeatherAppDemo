package com.elnimijo.games.weatherappdemo.model;

import com.google.gson.annotations.SerializedName;

public class Weather{
    @SerializedName("id")
    private String weather_id;
    private String main;
    private String description;
    private String icon;

    public String getWeatherId()
    {
        return weather_id;
    }

    public String getMain()
    {
        return main;
    }

    public String getDescription()
    {
        return description;
    }

    public String getIcon()
    {
        return icon;
    }
}