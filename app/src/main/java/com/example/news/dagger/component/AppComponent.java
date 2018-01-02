package com.example.news.dagger.component;

import com.example.news.activities.MainActivity;
import com.example.news.dagger.module.ApiModule;
import com.example.news.dagger.module.AppModule;
import com.example.news.fragments.BaseFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Влад on 24.12.2017.
 */

@Singleton
@Component(modules = {ApiModule.class, AppModule.class})
public interface AppComponent {
    void inject (MainActivity mainActivity);
    void inject (BaseFragment baseFragment);
}
