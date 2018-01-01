package com.example.news.utils;

import com.google.firebase.storage.StorageReference;

import javax.annotation.Nonnull;

/**
 * Created by Влад on 01.01.2018.
 *
 */

public class StorageHelper {
    public static final String TAG = StorageHelper.class.getSimpleName();
    public static final String PHOTOS_OBJ = "photos";

    private StorageReference mStorageRef;

    public StorageHelper(@Nonnull StorageReference mStorageRef) {
        this.mStorageRef = mStorageRef;
    }

    /**
     *  Get references of photo in Firebase Storage
     *
     * @param pLastPathSegment see {@code Uri.getLastPathSegment()}
     * @return reference to Storage of photo
     */
    public StorageReference getRefOfFile(String pLastPathSegment){
        return mStorageRef.child(PHOTOS_OBJ)
                .child(pLastPathSegment);
    }
}
