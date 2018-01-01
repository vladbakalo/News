package com.example.news.utils;



import android.app.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.Calendar;

/**
 * Utils
 *
 */

public class Utils {

    public static void hideKeyboardByView(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static Fragment getFragmentFromBundle(AppCompatActivity pActivity, String classname) {
        FragmentManager fm = pActivity.getSupportFragmentManager();

        String tag = classname;
        Fragment fragment = fm.findFragmentByTag(tag);


        if (fragment != null) {
            return fragment;
        }

        fragment = Fragment.instantiate(pActivity, classname);

        return fragment;
    }

    public static String getReadableDate(Calendar calendar){
        return String.format("%d/%d/%d", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
    }

}
