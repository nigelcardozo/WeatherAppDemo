package com.elnimijo.games.weatherappdemo.model;

import com.google.gson.annotations.SerializedName;

public class CurrentWeatherData {

    public String message;
    public String cod;
    public String count;

    @SerializedName(value = "list")
    public WeatherListItem[] weatherListItem;

}
