package com.elnimijo.games.weatherappdemo;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.widget.Toast;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

public class GeocoderHelper {

    public static String getCityName(Context ctx, double longitude, double latitude)
    {
        //Retrieves the cityname. Seems somewhat unreliable however. Should ideally use
        //Google Play services for a better solution.

        String city = new String();

        try{
            Geocoder geocoder = new Geocoder(ctx);

            if (geocoder.isPresent())
            {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

                if (addresses!=null)
                {
                    String cityName = addresses.get(0).getLocality();

                    StringBuilder sb = new StringBuilder();
                    if (cityName != null)
                    {
                        sb.append(cityName);

                        city = URLEncoder.encode(sb.toString(), "utf-8");
                    }
                }
                else
                {
                    Toast.makeText(ctx,"Error : Can't retrieve city name", Toast.LENGTH_SHORT).show();
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(ctx,"Error : Can't retrieve city name", Toast.LENGTH_LONG).show();
        }

        return city;
    }
}
