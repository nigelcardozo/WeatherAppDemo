README

The purpose of this code is to show you how to create a simple weather app for Android.

This app utilises the libraries Volley to create HTTP Requests and Gson to parse the JSON responses.

It currently supports two types of queries, location (latitude and longitude) or city. Feel free to expand it further.

In order to get the latitude and longitude we use the network and the location sensors.

Current the app doesn't fully handle all error cases, it's simply a proof of concept to show how to implement such functionality utilising the appropriate libraries!





To use within Android Studio:

1) Clone this project

2) Open Android Studio, then choose to open an existing Android Studio project and select the location where you've cloned this repo.

3) Android studio will attempt to build and will give you an error. This is due to the fact that I cannot redistribute Volley.

4) Clone Volley:

	git clone https://android.googlesource.com/platform/frameworks/volley

5) Within Android Studio now choose 'Import Module'	and select the location where you cloned Volley.

6) Once step 5 has completed, clean the build and rebuild.

7) Within MainActivity.java, replace the 'FFFFFFFFFF' string (as detailed below) with the API key provided to you when you register with OpenWeatherMap (see below)
   public static final String WEATHER_API_KEY = "FFFFFFFFFF";


   
Please Note:

This code utilises the Volley and GSON libraries.

You can find GSON at:
https://github.com/google/gson

You can find VOLLEY at:
https://android.googlesource.com/platform/frameworks/volley

The Weather API used is provided by OpenWeatherMap.org. You will need to register with them to obtain an API key.
http://www.openweathermap.org/


Icons are from the Oxygen Team icon set
(http://www.iconarchive.com/show/oxygen-icons-by-oxygen-icons.org.html)

Finally, I hope this code is of use, please feel free to download some of my free apps from the Google Play Store 
(https://play.google.com/store/apps/developer?id=elnimijo+games)



