package com.example.news.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.example.news.R;
import com.example.news.fragments.BaseFragment;

import java.io.File;

import javax.annotation.Nonnull;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Влад on 01.01.2018.
 *
 */

public class ImageHelper {
    public static final String TAG = ImageHelper.class.getSimpleName();

    private static final int RC_TAKE_PICTURE = 101;
    private static final int RC_TAKE_PICTURE_GALLERY = 102;
    public static final String AVATAR_SEGMENT = "_avatar.jpg";

    private Activity mActivity;
    private File mCameraFile;

    public ImageHelper(@Nonnull Activity pActivity) {
        this.mActivity = pActivity;
    }

    public Uri onActivityResultCamera(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);

        if (resultCode == RESULT_OK) {
            Uri mFileUri = null;
            switch (requestCode) {
                case RC_TAKE_PICTURE_GALLERY:
                    mFileUri = data.getData();
                    break;
                case RC_TAKE_PICTURE:
                    mFileUri = Uri.fromFile(mCameraFile);
                    break;
            }
            if (mFileUri != null) {
                return mFileUri;
            } else {
                Log.w(TAG, "File URI is null");
            }
        } else {
            Toast.makeText(mActivity, mActivity.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    public void launchCamera(BaseFragment fragment) {
        Log.d(TAG, "launchCamera");
        mCameraFile = createTemporalFile(fragment);

        if (mCameraFile.exists())
            mCameraFile.delete();
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCameraFile));

        if (intent.resolveActivity(fragment.getActivity().getPackageManager()) != null) {
            fragment.startActivityForResult(intent, RC_TAKE_PICTURE);
        }
    }

    public void takeGalleryImage(Fragment fragment) {
        Log.d(TAG, "takeGalleryImage");

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        if (intent.resolveActivity(fragment.getActivity().getPackageManager()) != null) {
            fragment.startActivityForResult(Intent.createChooser(intent,
                    "Select Picture"), RC_TAKE_PICTURE_GALLERY);
        }
    }

    private File createTemporalFile(BaseFragment fragment) {
        return new File(fragment.getActivity().getExternalCacheDir(), fragment.getAuth().getCurrentUser().getUid() + AVATAR_SEGMENT);
    }
}
