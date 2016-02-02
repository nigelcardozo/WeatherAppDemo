package com.elnimijo.games.weatherappdemo;


import android.content.Context;
import android.content.SharedPreferences;

public class MainUtility {

    public static final String WEATHER_APP_DEMO_PREFS = "WeatherAppDemo";
    public static final String REQUEST_TAG = "MainActivityRequest";

    public static boolean isNightTime(int hour)
    {
        //Tells us if it's day or night!
        //Used for updating the background colour.
        boolean isNight;

        isNight = hour < 6 || hour > 18;
        return isNight;
    }

    public static int getBackGroundIconID(boolean isNight, String weatherCode)
    {
        //Get the background Icon ID.

        int backgroundIcon = WeatherIcon.getWeatherIcon(isNight, Integer.parseInt(weatherCode));

        return backgroundIcon;
    }
}
