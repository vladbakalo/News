package com.example.news.fragments;


import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.news.R;
import com.example.news.activities.MainActivity;
import com.example.news.application.enums.ECity;
import com.example.news.application.enums.ECountry;
import com.example.news.dialog.ChooseImageProviderDialog;
import com.example.news.entity.User;
import com.example.news.utils.ImageHelper;
import com.example.news.utils.PermissionUtils;
import com.example.news.utils.SavedBundle;
import com.example.news.utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener, OnSuccessListener<UploadTask.TaskSnapshot>, OnFailureListener {
    public static final String TAG = EditProfileFragment.class.getSimpleName();

    public static final String TYPE_EDIT = "TYPE_EDIT";

    public enum EditProfileMode {
        TYPE_COMPLETE_REGISTRATION, TYPE_SIMPLE_EDIT
    }

    EditProfileMode editProfileMode;

    @BindView(R.id.iv_person_avatar)
    ImageView mUserAvatar;
    @BindView(R.id.rl_pick_new_image)
    RelativeLayout mPickAvatarBlock;
    @BindView(R.id.input_first_name)
    TextInputEditText mInputFirstName;
    @BindView(R.id.input_last_name)
    TextInputEditText mInputLastName;
    @BindView(R.id.input_phone)
    TextInputEditText mInputPhone;
    @BindView(R.id.input_date)
    TextView mInputDate;
    @BindView(R.id.spinner_country)
    AppCompatSpinner mSpinnerCountry;
    @BindView(R.id.spinner_city)
    AppCompatSpinner mSpinnerCity;

    @BindView(R.id.input_layout_first_name)
    TextInputLayout mInputLayoutFirstName;
    @BindView(R.id.input_layout_last_name)
    TextInputLayout mInputLayoutLastName;
    @BindView(R.id.input_layout_phone)
    TextInputLayout mInputLayoutPhone;


    private Unbinder butterKniefUnbinder;
    private Calendar mBirthDayDate;
    private Uri imageUri;
    private ImageHelper imageHelper;
    private boolean isImageSaved = false;
    private User mUser;

    public static SavedBundle newInstanceCompleteRegistration() {
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE_EDIT, EditProfileMode.TYPE_COMPLETE_REGISTRATION.ordinal());
        return SavedBundle.create(EditProfileFragment.class, bundle);
    }

    public static SavedBundle newInstanceSimpleEdit() {
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE_EDIT, EditProfileMode.TYPE_SIMPLE_EDIT.ordinal());
        return SavedBundle.create(EditProfileFragment.class, bundle);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mBirthDayDate = Calendar.getInstance();
        mUser = new User();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        butterKniefUnbinder = ButterKnife.bind(this, root);

        setUpCountrySpinner();
        setUpCitySpinner();
        setEditProfileType();
        setUpToolBarTitle();

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        imageHelper = new ImageHelper(getActivity());
        getProgressDialog().setMessage(getString(R.string.loading));
        getProgressDialog().show();
        requestUserData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri = imageHelper.onActivityResultCamera(requestCode, resultCode, data);
        if (uri != null) {
            setUpAvatar(uri);
            return;
        }
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ChooseImageProviderDialog.CHOOSE_IMAGE:
                    if (data != null && data.getExtras().containsKey(ChooseImageProviderDialog.B_CHOOSE_PROVIDER_ID)) {
                        switch (data.getExtras().getInt(ChooseImageProviderDialog.B_CHOOSE_PROVIDER_ID)) {
                            case R.id.tv_choose_from_gallery:
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PermissionUtils.MY_PERMISSIONS_REQUEST_GALLERY);
                                } else {
                                    imageHelper.takeGalleryImage(this);
                                }
                                break;
                            case R.id.tv_take_photo:
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(new String[]{Manifest.permission.CAMERA}, PermissionUtils.MY_PERMISSIONS_REQUEST_CAMERA);
                                } else {
                                    imageHelper.launchCamera(this);
                                }
                                break;
                        }
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            switch (requestCode) {
                case PermissionUtils.MY_PERMISSIONS_REQUEST_CAMERA:
                    imageHelper.launchCamera(this);
                    break;
                case PermissionUtils.MY_PERMISSIONS_REQUEST_GALLERY:
                    imageHelper.takeGalleryImage(this);
                    break;
            }

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit_profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                if (validate()) {
                    processSaveProfile();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.input_date, R.id.rl_pick_new_image})
    public void onClickView(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.input_date:
                showDatePickerDialog();
                break;
            case R.id.rl_pick_new_image:
                pickPhoto();
                break;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        butterKniefUnbinder.unbind();
    }

    private void showDatePickerDialog() {
        int year = mBirthDayDate.get(Calendar.YEAR);
        int month = mBirthDayDate.get(Calendar.MONTH);
        int day = mBirthDayDate.get(Calendar.DAY_OF_MONTH);

        Dialog picker = new DatePickerDialog(getActivity(), this,
                year, month, day);
        picker.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        updateBirthDayDate(calendar);
        mBirthDayDate = calendar;
    }

    private void setEditProfileType() {
        if (getArguments() != null && getArguments().containsKey(TYPE_EDIT)) {
            editProfileMode = EditProfileMode.values()[getArguments().getInt(TYPE_EDIT)];
        } else {
            editProfileMode = EditProfileMode.TYPE_SIMPLE_EDIT;
        }
    }

    private void setUpToolBarTitle() {
        String str;
        switch (editProfileMode) {
            case TYPE_SIMPLE_EDIT:
                str = getString(R.string.edit_profile);
                break;
            case TYPE_COMPLETE_REGISTRATION:
                str = getString(R.string.complete_registration);
                break;
            default:
                str = getString(R.string.edit_profile);
        }
        getActivity().setTitle(str);
    }

    private void setUpCountrySpinner() {
        mSpinnerCountry.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, ECountry.getCountry(getActivity())));
        mSpinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setUpCitySpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setUpCitySpinner() {
        ECountry country = ECountry.values()[mSpinnerCountry.getSelectedItemPosition()];
        List<ECity> cities = ECity.getByCountry(country);
        mSpinnerCity.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, ECity.getCities(cities, getActivity())));
        mSpinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void updateBirthDayDate(Calendar date) {
        if (date == null) {
            mInputDate.setText("");
        } else {
            mInputDate.setText(Utils.getReadableDate(date));
        }
    }

    private void processSaveProfile() {
        getProgressDialog().setMessage(getString(R.string.saving));
        getProgressDialog().show();
        if (imageUri == null) {
            saveProfile();
        } else {
            saveImage();
        }
    }

    private void saveProfile() {
        updateUser();
        getDBHelper().updateUser(mUser);
        switch (editProfileMode) {
            case TYPE_SIMPLE_EDIT:

                break;
            case TYPE_COMPLETE_REGISTRATION:
                MainActivity.startMainActivity(getActivity());
                break;
        }
        getProgressDialog().dismiss();
        getActivity().finish();
    }

    private void updateUser() {
        mUser.setFirstName(mInputFirstName.getText().toString());
        mUser.setLastName(mInputLastName.getText().toString());
        mUser.setPhone(mInputPhone.getText().toString());
        mUser.setBirthDay(mBirthDayDate.getTimeInMillis());
        ECountry country = ECountry.values()[mSpinnerCountry.getSelectedItemPosition()];
        ECity city = ECity.getByCountry(country).get(mSpinnerCity.getSelectedItemPosition());
        mUser.setCityId(city.getId());
        mUser.setCountryId(country.getId());
    }

    private void updateUserAvatar(String uri) {
        mUser.setPhotoPath(uri);
    }

    private void saveImage() {
        StorageReference riversRef = getStorageHelper().getRefOfFile(getAuth().getUid() + ImageHelper.AVATAR_SEGMENT);

        riversRef.putFile(imageUri)
                .addOnSuccessListener(this)
                .addOnFailureListener(this);
    }

    private boolean validate() {
        boolean valid = true;

        String firstName = mInputFirstName.getText().toString();
        String lastName = mInputLastName.getText().toString();
        String telephone = mInputPhone.getText().toString();

        if (firstName.isEmpty()) {
            mInputLayoutFirstName.setError(getString(R.string.field_required));
            valid = false;
        } else {
            mInputLayoutFirstName.setError(null);
        }

        if (lastName.isEmpty()) {
            mInputLayoutLastName.setError(getString(R.string.field_required));
            valid = false;
        } else {
            mInputLayoutLastName.setError(null);
        }

        if (telephone.isEmpty()) {
            mInputLayoutPhone.setError(getString(R.string.field_required));
            valid = false;
        } else {
            mInputLayoutPhone.setError(null);
        }

        return valid;
    }

    private void pickPhoto() {
        ChooseImageProviderDialog dialog = new ChooseImageProviderDialog();
        dialog.setTargetFragment(this, R.id.rl_pick_new_image);
        dialog.show(getFragmentManager(), dialog.getClass().getName());
    }

    private void setUpAvatar(Uri uri) {
        imageUri = uri;
        mUserAvatar.setImageURI(uri);
    }

    private void requestUserData() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                mUser = user;
                displayUserData();
                getDBHelper().getUserReference().removeEventListener(this);
                getProgressDialog().dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        getDBHelper().getUserReference().addValueEventListener(postListener);
    }

    private void displayUserData(){
        Picasso.with(getActivity())
                .load(mUser.getPhotoPath())
                .placeholder(R.drawable.ic_image_load)
                .error(R.drawable.ic_image_error)
                .into(mUserAvatar);
        mInputFirstName.setText(mUser.getFirstName());
        mInputLastName.setText(mUser.getLastName());
        mInputPhone.setText(mUser.getPhone());

        //Set up b-day
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mUser.getBirthDay());
        updateBirthDayDate(calendar);
        mBirthDayDate = calendar;

        //Set up country
        ECountry country = ECountry.getContryById(mUser.getCountryId());
        if (country != null) {
            mSpinnerCountry.setSelection(country.ordinal());
            setUpCitySpinner();
            //Set up city
            List<ECity> cities = ECity.getByCountry(country);
            int position = 0;
            for (ECity item :
                    cities) {
                if (item.getId() == mUser.getCityId())
                    mSpinnerCity.setSelection(position);
                position++;
            }
        }
    }

    @Override
    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
        Uri downloadUrl = taskSnapshot.getDownloadUrl();
        updateUserAvatar(downloadUrl.toString());
        saveProfile();
    }

    @Override
    public void onFailure(@NonNull Exception e) {

    }

}
