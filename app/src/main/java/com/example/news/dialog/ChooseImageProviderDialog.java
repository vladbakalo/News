package com.example.news.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.news.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Влад on 01.01.2018.
 *
 */

public class ChooseImageProviderDialog extends android.support.v4.app.DialogFragment implements View.OnClickListener {

    public static final String B_CHOOSE_PROVIDER_ID = "B_CHOOSE_PROVIDER_ID";

    public static final int CHOOSE_IMAGE = 102;

    private Unbinder mUnbinder;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setView(getLayout());
        return adb.create();
    }

    private View getLayout() {
        View parent = getActivity().getLayoutInflater().inflate(R.layout.dialog_pick_image, null);

        mUnbinder = ButterKnife.bind(this, parent);
        return parent;
    }


    @OnClick({R.id.tv_choose_from_gallery, R.id.tv_take_photo})
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.putExtra(B_CHOOSE_PROVIDER_ID, v.getId());
        getTargetFragment().onActivityResult(CHOOSE_IMAGE, Activity.RESULT_OK, intent);
        dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
