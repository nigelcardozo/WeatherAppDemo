package com.elnimijo.games.weatherappdemo.model;

import com.google.gson.annotations.SerializedName;

public class Coordinates {
    @SerializedName("lon")
    private String longitude;
    @SerializedName("lat")
    private String latitude;

    public String getLatitude()
    {
        return latitude;
    }
    public String getLongitude()
    {
        return longitude;
    }
}
