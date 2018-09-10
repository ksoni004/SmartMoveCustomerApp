package com.smartmovetheapp.smartmove.ui.orderrequest.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.smartmovetheapp.smartmove.R;
import com.smartmovetheapp.smartmove.ui.base.BaseActivity;
import com.smartmovetheapp.smartmove.util.CalenderUtil;

import java.util.Calendar;

public class OrderRequestFragment extends Fragment {

    private FloatingActionButton fabNext;
    private CardView cvPickup;
    private CardView cvDrop;
    private CardView cvDateTime;
    private TextView txtPickup;
    private TextView txtDrop;
    private TextView txtDateTime;
    private AppCompatSpinner spTruckType;
    private EditText edtTripCount;

    private long orderDateTime;

    private OrderRequestActionListener actionListener;

    public static OrderRequestFragment newInstance() {
        return new OrderRequestFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_request, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fabNext = view.findViewById(R.id.fab_next);
        cvPickup = view.findViewById(R.id.cv_pickup);
        cvDrop = view.findViewById(R.id.cv_destination);
        cvDateTime = view.findViewById(R.id.cv_date_time);
        txtPickup = view.findViewById(R.id.txt_pickup_value);
        txtDrop = view.findViewById(R.id.txt_destination_value);
        txtDateTime = view.findViewById(R.id.txt_date_time);
        spTruckType = view.findViewById(R.id.sp_truck_type);
        edtTripCount = view.findViewById(R.id.edt_trip_count);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        orderDateTime = System.currentTimeMillis();

        cvPickup.setOnClickListener(button -> onPickUpClick());
        cvDrop.setOnClickListener(button -> onDropClick());
        cvDateTime.setOnClickListener(button -> onDateTimeClick());
        fabNext.setOnClickListener(button -> onNextClick());
    }

    private void onNextClick() {
        try {
            validateInput();
            actionListener.onNextOfOrderClick(
                    "",
                    "",
                    orderDateTime,
                    getResources().getStringArray(R.array.truck_type_values)[spTruckType.getSelectedItemPosition()],
                    edtTripCount.getText().toString()
            );
        } catch (IllegalArgumentException error) {
            showError(error.getMessage());
        }
    }

    private void showError(String message) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showError(message);
        }
    }

    private void validateInput() throws IllegalArgumentException {

    }

    private void onDateTimeClick() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(orderDateTime);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                R.style.SMDatePickerTheme,
                (view, year, month, dayOfMonth) -> {
                    storeSelectedDate(year, month, dayOfMonth);
                    showTimePicker();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE)
        );

        datePickerDialog.getDatePicker().setMinDate(CalenderUtil.getStartOfDayTime(System.currentTimeMillis()));

        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(orderDateTime);
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext(),
                R.style.SMDatePickerTheme,
                (view, hourOfDay, minute) -> {
                    storeSelectedTime(hourOfDay, minute);
                    txtDateTime.setText(CalenderUtil.getDisplayDateInLongerFormat(orderDateTime) + " " + CalenderUtil.getDisplayDateTime(orderDateTime));
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
        );

        timePickerDialog.show();
    }

    private void storeSelectedTime(int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(orderDateTime);

        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        orderDateTime = calendar.getTimeInMillis();
    }

    private void storeSelectedDate(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(orderDateTime);

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        orderDateTime = calendar.getTimeInMillis();
    }

    private void onDropClick() {

    }

    private void onPickUpClick() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OrderRequestActionListener) {
            actionListener = (OrderRequestActionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        actionListener = null;
    }

    public interface OrderRequestActionListener {
        void onNextOfOrderClick(String pickup, String drop, long dateTime, String truckType, String tripCount);
    }
}
