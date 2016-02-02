package com.elnimijo.games.weatherappdemo.model;

import com.google.gson.annotations.SerializedName;

public class MainInfo {

    private String temp;
    private String pressure;
    private String humidity;
    private String temp_min;
    private String temp_max;

    public String getTemp()
    {
        return temp;
    }

    public String getPressure()
    {
        return pressure;
    }

    public String getHumidity()
    {
        return humidity;
    }

    public String getTempMin()
    {
        return temp_min;
    }

    public String getTempMax()
    {
        return temp_max;
    }
}

