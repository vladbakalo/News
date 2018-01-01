package com.example.news.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.ContextCompat;



/**
 * Created by Влад on 01.01.2018.
 *
 */

public class PermissionUtils {

    public final static int MY_PERMISSIONS_REQUEST_GALLERY = 201;
    public final static int MY_PERMISSIONS_REQUEST_CAMERA = 202;

    public static boolean isGrantSuccess(int[] grantResults) {
        boolean Result = true;
        // If request is cancelled, the result arrays are empty.
        for (int i = 0; i < grantResults.length; i++) {
            Result = Result && (grantResults[i] == PackageManager.PERMISSION_GRANTED);
        }
        return  Result;
    }

    public static boolean checkCameraPermision(Activity activity){
        return checkPermission(activity, Manifest.permission.CAMERA, MY_PERMISSIONS_REQUEST_CAMERA);
    }

    public static boolean checkGalleryPermision(Activity activity){
        return checkPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE, MY_PERMISSIONS_REQUEST_GALLERY);
    }

    private static boolean checkPermission(Activity activity, String permission, int requestCode){
        if (ContextCompat.checkSelfPermission(activity,
                permission)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity,
                    new String[]{permission},
                    requestCode);

            return false;
        }
        return true;
    }
}
