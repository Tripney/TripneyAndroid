package com.msaranu.tripney;

import android.app.Application;

import com.msaranu.tripney.models.Event;
import com.msaranu.tripney.models.Expense;
import com.msaranu.tripney.models.Trip;
import com.msaranu.tripney.models.User;
import com.msaranu.tripney.models.UserFriend;
import com.parse.Parse;
import com.parse.ParseObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by msaranu on 2/4/18.
 */

public class TripneyApplication extends Application {

    public static final String APP_TAG = "Tripney";
    public static final int PERM_CAM_PROFILE_REQ_CODE = 1;


    @Override
    public void onCreate() {
        super.onCreate();

        // Register your parse models
        ParseObject.registerSubclass(Trip.class);
        ParseObject.registerSubclass(Expense.class);
        ParseObject.registerSubclass(Trip.class);
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Event.class);
        ParseObject.registerSubclass(UserFriend.class);


        // Use for troubleshooting -- remove this line for production
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        // Use for monitoring Parse OkHttp traffic
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        // See http://square.github.io/okhttp/3.x/logging-interceptor/ to see the options.
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);

        // set applicationId, and server server based on the values in the Heroku settings.
        // clientKey is not needed unless explicitly configured
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("tripney") // should correspond to APP_ID env variable
                .clientKey(null)  // set explicitly unless clientKey is explicitly configured on Parse server
                .clientBuilder(builder)
                .server("http://tripney.herokuapp.com/parse/").build());

    }
}