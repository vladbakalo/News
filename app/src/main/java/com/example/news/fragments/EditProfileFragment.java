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
import com.example.news.application.enums.ECity;
import com.example.news.application.enums.ECountry;
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
public class EditProfileFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    public static final String TAG = EditProfileFragment.class.getSimpleName();

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

    public EditProfileFragment() {

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
        getActivity().setTitle(R.string.edit_profile);

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
}
