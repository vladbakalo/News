package com.example.news.utils;

import android.util.Log;

import com.example.news.entity.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import javax.annotation.Nonnull;

/**
 * Data-base helper
 *
 */

public class DBHelper {
    public static final String TAG = DBHelper.class.getSimpleName();
    public static final String USERS_OBJ = "users";

    private FirebaseUser mUser;
    private DatabaseReference mDatabase;
    private String mUId;

    public DBHelper(@Nonnull FirebaseUser mUser, @Nonnull DatabaseReference mDatabase) {
        this.mUser = mUser;
        this.mDatabase = mDatabase;
        this.mUId = mUser.getUid();
    }

    public void createUser(){
        User userEntity = new User();
        userEntity.setUId(mUId);
        mDatabase.child(USERS_OBJ).child(mUId).setValue(userEntity);

        Log.wtf(TAG, "User created in DataBase with uId : " + mUId);
    }

    public void updateUser(User pUser){
        mDatabase.child(USERS_OBJ).child(mUId).setValue(pUser);
    }

    public DatabaseReference getUserReference(){
        return mDatabase.child(USERS_OBJ).child(mUId);
    }

    public DatabaseReference getAllUsers(){
        return mDatabase.child(USERS_OBJ);
    }
}
