package com.example.news.fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.news.R;
import com.example.news.activities.MainActivity;
import com.example.news.application.enums.ECity;
import com.example.news.application.enums.ECountry;
import com.example.news.entity.User;
import com.example.news.utils.DBHelper;
import com.example.news.utils.SavedBundle;
import com.facebook.login.widget.LoginButton;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener {
    public static final String TAG = EditProfileFragment.class.getSimpleName();

    public static final String TYPE_EDIT = "TYPE_EDIT";

    public enum EditProfileMode {
        TYPE_COMPLETE_REGISTRATION, TYPE_SIMPLE_EDIT
    }

    EditProfileMode editProfileMode;

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

    public static SavedBundle newInstanceCompleteRegistration(){
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE_EDIT, EditProfileMode.TYPE_COMPLETE_REGISTRATION.ordinal());
        return SavedBundle.create(EditProfileFragment.class, bundle);
    }

    public static SavedBundle newInstanceSimpleEdit(){
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE_EDIT, EditProfileMode.TYPE_SIMPLE_EDIT.ordinal());
        return SavedBundle.create(EditProfileFragment.class, bundle);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mBirthDayDate = Calendar.getInstance();
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit_profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_save:
                if (validate()) {
                    saveProfile();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.input_date)
    public void onClickView(View view){
        int id = view.getId();

        switch (id){
            case R.id.input_date:
                showDatePickerDialog();
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

    private void showDatePickerDialog(){
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

    private void setEditProfileType(){
        if (getArguments() != null && getArguments().containsKey(TYPE_EDIT)) {
            editProfileMode = EditProfileMode.values()[getArguments().getInt(TYPE_EDIT)];
        } else {
            editProfileMode = EditProfileMode.TYPE_SIMPLE_EDIT;
        }
    }

    private void setUpToolBarTitle(){
        String str;
        switch (editProfileMode){
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

    private void setUpCountrySpinner(){
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

    private void setUpCitySpinner(){
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

    private void updateBirthDayDate(Calendar date){
        if (date == null){
            mInputDate.setText("");
        } else {
            mInputDate.setText(String.format("%d/%d/%d", date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.MONTH), date.get(Calendar.YEAR)));
        }
    }

    private void saveProfile(){
        switch (editProfileMode){
            case TYPE_SIMPLE_EDIT:
                getDBHelper().updateUser(getUser());
                getActivity().finish();
                break;
            case TYPE_COMPLETE_REGISTRATION:
                getDBHelper().updateUser(getUser());
                MainActivity.startMainActivity(getActivity());
                getActivity().finish();
                break;
        }
    }

    private User getUser(){
        User user = new User();
        user.setFirstName(mInputFirstName.getText().toString());
        user.setLastName(mInputLastName.getText().toString());
        user.setTelephone(Integer.valueOf(mInputPhone.getText().toString()));
        user.setBirthDay(mBirthDayDate.getTimeInMillis());
        user.setCityId(ECity.values()[mSpinnerCity.getSelectedItemPosition()].ordinal());
        user.setCountryId(ECity.values()[mSpinnerCountry.getSelectedItemPosition()].ordinal());
        return user;
    }

    private boolean validate(){
        return true;
    }


}
