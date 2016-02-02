package com.elnimijo.games.weatherappdemo;


import java.util.HashMap;

public class WeatherIcon {
    private static HashMap mapIconDay;
    private static HashMap mapIconNight;

    static {
        mapIconDay = new HashMap();

        mapIconDay.put( 2, R.drawable.weatherbackgroundthunder);
        mapIconDay.put( 3, R.drawable.weatherbackgrounddrizzle);
        mapIconDay.put( 5, R.drawable.weatherbackgroundrain);
        mapIconDay.put( 6, R.drawable.weatherbackgroundsnow);
        mapIconDay.put( 8, R.drawable.weatherbackgroundclear);

        mapIconNight = new HashMap();

        mapIconNight.put( 2, R.drawable.nweatherbackgroundthunder);
        mapIconNight.put( 3, R.drawable.nweatherbackgrounddrizzle);
        mapIconNight.put( 5, R.drawable.nweatherbackgroundrain);
        mapIconNight.put( 6, R.drawable.nweatherbackgroundsnow);
        mapIconNight.put( 8, R.drawable.nweatherbackgroundclear);
    }

    public static int getWeatherIcon(boolean isNight, int iconNumber) {

        if (isNight == false) {
            return (int) mapIconDay.get(iconNumber);
        } else {
            return (int) mapIconNight.get(iconNumber);
        }
    }
}