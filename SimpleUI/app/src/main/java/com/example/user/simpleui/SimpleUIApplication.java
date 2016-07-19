package com.example.user.simpleui;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by user on 2016/7/19.
 */
public class SimpleUIApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Order.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                        .applicationId("vg1HDPybFrlqHdUcrj7RTPkvedcXc1MKErks27Yd")
                        .server("https://parseapi.back4app.com/")
                        .clientKey("9oaZ2V0e4aA2BDb5pSZptcCmCvvSpb0n9QLDAtqe")
                        .build()
        );
    }
}
