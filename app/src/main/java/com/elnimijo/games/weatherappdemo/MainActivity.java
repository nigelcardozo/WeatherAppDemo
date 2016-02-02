package com.elnimijo.games.weatherappdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import com.elnimijo.games.weatherappdemo.model.CurrentWeatherData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends Activity {

    //WARNING!!!! - Add your weather API Key here, without it this will NOT work.
    //Get your API Key by registering with OpenWeatherMap.org
    public static final String WEATHER_API_KEY = "FFFFFFFFFF";

    public static final String WEATHER_APP_DEMO_PREFS = "WeatherAppDemo";
    public static final String REQUEST_TAG = "MainActivityRequest";

    public static final int MAX_URL_SIZE = 200;

    public static final String WEATHER_CODE_OKAY = "200";

    public static final String WEATHER_API_REQ_GET_CURRENT_WEATHER = "http://api.openweathermap.org/data/2.5/find";
    public static final String WEATHER_API_REQ_GET_CURRENT_WEATHER_PARAM1A_CITY = "q=";
    public static final String WEATHER_API_REQ_GET_CURRENT_WEATHER_PARAM1B_LAT = "lat=";
    public static final String WEATHER_API_REQ_GET_CURRENT_WEATHER_PARAM1B_LON = "lon=";
    public static final String WEATHER_API_REQ_GET_CURRENT_WEATHER_PARAM2 = "cnt=";
    public static final String WEATHER_API_REQ_GET_CURRENT_WEATHER_PARAM3 = "units=imperial";
    public static final String WEATHER_API_REQ_GET_CURRENT_WEATHER_PARAM4 = "appid=";
    private RequestQueue mVolleyQueue;

    private TextView textViewCityName;
    private TextView textViewDescription;
    private TextView textViewTemperature;
    private TextView textViewLastUpdated;

    private ImageView imageViewBackgroundWeather;

    private View root;

    private MainUtility mainUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (initialise())
        {
            loadCachedWeatherData();

            getLocation();
        }
        else
        {
            showErrorDialog();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        performVolleyCleanUp();
    }


    private boolean initialise()
    {
        //Perform initialisation procedures.

        //This is here to ensure that the key is added. See the warning at the top of this file.
        if (WEATHER_API_KEY == "FFFFFFFFFF")
            return false;

        mVolleyQueue = VolleyRequestQueueHelper.getInstance(getApplicationContext())
                .getRequestQueueInstance();

        setupUIControls();

        return true;
    }

    private void setupUIControls()
    {
        //Setup the UI Controls

        textViewCityName = (TextView) findViewById(R.id.textViewCityName);
        textViewDescription = (TextView) findViewById(R.id.textViewDescription);
        textViewTemperature = (TextView) findViewById(R.id.textViewTemperature);
        textViewLastUpdated = (TextView) findViewById(R.id.textViewLastUpdated);
        imageViewBackgroundWeather = (ImageView) findViewById(R.id.imageViewBackgroundWeather);

        //Find the root view - required to update the background image later.
        root = textViewCityName.getRootView();
    }

    private void showErrorDialog()
    {
        //This is here purely to ensure that the API key is setup. If it's not, you'll see this!

        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Error");
        alertDialog.setMessage("API Key not detected. Please go to OpenWeatherMap.org, " +
                               "create a key and then update the value of " +
                               "WEATHER_API_KEY");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        alertDialog.show();
    }

    private void updateBackground(boolean isNight, int backGroundIcon)
    {
        //Update the background colour and weather icon.

        if (isNight)
        {
            Activity activity = this;
            activity.findViewById(android.R.id.content).setBackgroundColor(getResources().getColor(R.color.skynight));
        }
        else
        {
            Activity activity = this;
            activity.findViewById(android.R.id.content).setBackgroundColor(getResources().getColor(R.color.skymorning));
        }

        imageViewBackgroundWeather.setImageResource(backGroundIcon);
    }

    private void updateWeatherData(String weatherCode, String cityName, String weatherMain, String currentTemperature, String epochDate)
    {
        //Update the weather data on the screen.

        SimpleDateFormat sdfHours = new SimpleDateFormat("HH");
        String hour = sdfHours.format(new Date(Long.parseLong(epochDate) * 1000));

        boolean isNight = mainUtility.isNightTime(Integer.parseInt(hour));

        int backGroundIcon = mainUtility.getBackGroundIconID(isNight, weatherCode);

        updateBackground(isNight, backGroundIcon);

        textViewCityName.setText(cityName);
        textViewDescription.setText(weatherMain);
        textViewTemperature.setText(currentTemperature+ (char) 0x00B0);

        if (!epochDate.isEmpty())
        {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String lastUpdated = sdf.format(new Date(Long.parseLong(epochDate) * 1000));

            textViewLastUpdated.setText("Last Updated:" + lastUpdated);
        }
    }

    private void cacheWeatherData(String weatherCode, String cityName, String weatherMain, String currentTemperature, String epochDate)
    {
        //Cache the weather data so that we'll be able to display something in the event of
        //having no network or location information the next time we startup.

        SharedPreferences.Editor editor = getSharedPreferences(WEATHER_APP_DEMO_PREFS, MODE_PRIVATE).edit();

        editor.putInt("FirstRun", 0);
        editor.putString("CityName", cityName);
        editor.putString("WeatherMain", weatherMain);
        editor.putString("CurrentTemperature", currentTemperature);
        editor.putString("LastUpdated", epochDate);
        editor.putString("WeatherCode", weatherCode);
        editor.commit();
    }

    private void loadCachedWeatherData()
    {
        //Load in our cached data. Better than having a blank screen.

        SharedPreferences prefs = getSharedPreferences(WEATHER_APP_DEMO_PREFS, MODE_PRIVATE);

        int firstRun = prefs.getInt("FirstRun", 1);

        if (firstRun == 0) {
            String cityName = prefs.getString("CityName", "Loading...");
            String weatherMain = prefs.getString("WeatherMain", "Loading...");
            String currentTemperature = prefs.getString("CurrentTemperature", "0");
            String epochDate = prefs.getString("LastUpdated", "");
            String weatherCode = prefs.getString("WeatherCode", "5");

            updateWeatherData(weatherCode, cityName, weatherMain, currentTemperature, epochDate);
        }
    }


    /* Location Functions */

    private void getLocation()
    {
        //Usage - Calls helper function to retrieve location data.

        LocationHelper locationHelper = new LocationHelper();
        locationHelper.getLocation(this, locationResultCallback);
    }


    LocationHelper.LocationResult locationResultCallback = new LocationHelper.LocationResult(){
        @Override
        public void locationResponseHandler(Location location){

            if (location != null) {
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();

                String cityName = GeocoderHelper.getCityName(getApplicationContext(),longitude, latitude);

                if (! cityName.isEmpty())
                {
                    getWeatherData(cityName);
                }
                else
                {
                    //Use latitude and longitude to get data instead
                    getWeatherData(Double.toString(latitude), Double.toString(longitude));
                }
            }
        }
    };


    /* Weather Data Functions */

    private static String createUrl(boolean cityQuery, String[] params){

        //Define a decent limit to avoid reallocating.
        //String should be around 140 characters in length.
        StringBuilder sb = new StringBuilder(MAX_URL_SIZE);

        sb.append(WEATHER_API_REQ_GET_CURRENT_WEATHER);

        if (cityQuery)
        {
            sb.append("?" + WEATHER_API_REQ_GET_CURRENT_WEATHER_PARAM1A_CITY + params[0]);
        }
        else
        {
            sb.append("?" + WEATHER_API_REQ_GET_CURRENT_WEATHER_PARAM1B_LAT + params[0] +
                    "&" + WEATHER_API_REQ_GET_CURRENT_WEATHER_PARAM1B_LON + params[1]);
        }

        sb.append("&" + WEATHER_API_REQ_GET_CURRENT_WEATHER_PARAM2 + "10" +
                "&" + WEATHER_API_REQ_GET_CURRENT_WEATHER_PARAM3 +
                "&" + WEATHER_API_REQ_GET_CURRENT_WEATHER_PARAM4 +
                WEATHER_API_KEY);

        return sb.toString();

    }

    private void getWeatherData(String cityName)
    {
        //Our request should look like this...
        //"http://api.openweathermap.org/data/2.5/find?q=Wokingham,United%20Kingdom&cnt=10&appid=49b1efd434d630f05ca9607a976770c0";
        String[] params = new String[1];
        params[0] = cityName;
        String url = createUrl(true, params);

        makeAndSendJsonRequest(url);
    }

    private void getWeatherData(String latitude, String longitude)
    {
        //Our request should look like this...
        //"http://api.openweathermap.org/data/2.5/find?q=Wokingham,United%20Kingdom&cnt=10&appid=49b1efd434d630f05ca9607a976770c0";
        String[] params = new String[2];
        params[0] = latitude;
        params[1] = longitude;
        String url = createUrl(false, params);

        makeAndSendJsonRequest(url);
    }

    private void makeAndSendJsonRequest(String url)
    {
        //Populate JSON Request
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                jsonObjectReqSuccessCallback(),
                jsonReqErrorCallback());

        //This tag is only useful for cancelling request, we can't seem to check it when getting
        //a response
        req.setTag(REQUEST_TAG);

        //Add our request to the Volley queue
        addRequestToVolleyQueue(req);
    }

    private void parseWeatherDataResponseGson(JSONObject response)
    {
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject)jsonParser.parse(response.toString());

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        //Put the data into the weather object using GSON to parse
        CurrentWeatherData weatherData;

        weatherData = gson.fromJson(gsonObject, CurrentWeatherData.class);

        if (weatherData.cod.equals(WEATHER_CODE_OKAY))
        {
            String epochDate = weatherData.weatherListItem[0].getDt();
            String cityName = weatherData.weatherListItem[0].getCityName();
            String weatherMain = weatherData.weatherListItem[0].weather[0].getMain();
            String currentTemperature = weatherData.weatherListItem[0].main.getTemp();
            String weatherIdCode = weatherData.weatherListItem[0].weather[0].getWeatherId();

            String iconCode = weatherIdCode.substring(0,1);

            updateWeatherData(iconCode, cityName, weatherMain, currentTemperature, epochDate);
            cacheWeatherData(iconCode, cityName, weatherMain, currentTemperature, epochDate);
        }
        else {
            Toast.makeText(getApplicationContext(), "Unable to get data from weather service", Toast.LENGTH_SHORT).show();
        }
    }

    /* Volley Related Functions */

    private void addRequestToVolleyQueue(JsonObjectRequest req)
    {
        //Add the request to the volley queue
        mVolleyQueue.add(req);
    }

    private void performVolleyCleanUp()
    {
        // Clear down if required
        if (mVolleyQueue != null) {
            mVolleyQueue.cancelAll(REQUEST_TAG);
        }
    }

    private Response.Listener<JSONObject> jsonObjectReqSuccessCallback() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //We've got a response. For now it's only going to be weather data.
                //There doesn't seem to be a way to check the tag that we sent in
                //the request thus ideally we should handle such situations by ensuring
                //we never send more than one request at a time and managing this
                //situation locally.

                parseWeatherDataResponseGson(response);
            }
        };
    }

    private Response.ErrorListener jsonReqErrorCallback() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //We should handle errors here, for now as this is a demo, simply indicate an error!
                Toast.makeText(getApplicationContext(),"Error - Currently Unhandled in demo!",Toast.LENGTH_LONG).show();
            }
        };
    }
}
