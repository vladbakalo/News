package com.example.news.application;

import android.app.Application;

import com.example.news.dagger.component.AppComponent;
import com.example.news.dagger.component.DaggerAppComponent;
import com.example.news.dagger.module.ApiModule;
import com.example.news.dagger.module.AppModule;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

/**
 * Application class
 *
 */

public class NewsApplication extends Application {
    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .apiModule(new ApiModule(Constants.NEWS_API_URL))
                .build();
    }

    public AppComponent getNetComponent() {
        return mAppComponent;
    }
}
