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
        ParseObject.registerSubclass(Drink.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                        .applicationId("vg1HDPybFrlqHdUcrj7RTPkvedcXc1MKErks27Yd")
//                        .applicationId("76ee57f8e5f8bd628cc9586e93d428d5")
                        .server("https://parseapi.back4app.com/")
//                        .server("http://parseserver-ps662-env.us-east-1.elasticbeanstalk.com/parse/")
                        .clientKey("9oaZ2V0e4aA2BDb5pSZptcCmCvvSpb0n9QLDAtqe")
                        .enableLocalDataStore()
                        .build()
        );
    }
}
