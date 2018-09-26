package com.smartmovetheapp.smartmove.ui.tripdetail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.smartmovetheapp.smartmove.R;
import com.smartmovetheapp.smartmove.data.remote.ApiClient;
import com.smartmovetheapp.smartmove.data.remote.model.Order;
import com.smartmovetheapp.smartmove.ui.base.BaseActivity;
import com.smartmovetheapp.smartmove.ui.bids.BidsActivity;
import com.smartmovetheapp.smartmove.util.CalenderUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripDetailActivity extends BaseActivity {

    private static final String ORDER_EXTRA = "order";
    private Order order;

    //order
    private TextView txtPickupPlace;
    private TextView txtDropPlace;
    private TextView txtDateTime;
    private TextView txtTruckType;
    private TextView txtTripCount;

    //pickup
    private TextView txtFloorLevel;
    private SwitchCompat swtElevator;
    private TextView txtParkingDistance;
    private TextView txtWeight;
    private TextView txtArea;
    private TextView txtExtra;

    //drop
    private TextView txtFloorLevelD;
    private SwitchCompat swtElevatorD;
    private TextView txtParkingDistanceD;
    private TextView txtExtraD;

    //initial payment
    private CardView cvInitialPaymentInfo;
    private TextView txtInitialPaymentAmount;
    private TextView txtInitialPaymentStatus;

    //final payment
    private CardView cvFinalPaymentInfo;
    private TextView txtFinalPaymentAmount;
    private TextView txtFinalPaymentStatus;

    //card buttons
    private CardView cvBidsButton;

    private AlertDialog loading;

    private final Callback<Void> cancelBidCallback = new Callback<Void>() {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {
            hideLoading();
            if (response.isSuccessful()) {
                new AlertDialog.Builder(TripDetailActivity.this, R.style.SMDatePickerTheme)
                        .setTitle("Order has been cancelled.")
                        //.setMessage("Your scheduled Bid has been cancelled successfully.")
                        .setCancelable(false)
                        .setPositiveButton("OK", (dialog, which) -> {
                            dialog.dismiss();

                            setResult(RESULT_OK);
                            finish();
                        })
                        .show();
            } else {
                showError(R.string.default_error);
            }
        }

        @Override
        public void onFailure(Call<Void> call, Throwable t) {
            hideLoading();
            showError(R.string.default_error);
        }
    };

    private void showLoading() {
        loading.show();
    }

    private void hideLoading() {
        loading.dismiss();
    }

    public static void start(Activity context, Order order, int requestCode) {
        Intent starter = new Intent(context, TripDetailActivity.class);
        Gson gson = new Gson();
        starter.putExtra(ORDER_EXTRA, gson.toJson(order));
        context.startActivityForResult(starter, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);

        if (!getIntent().hasExtra(ORDER_EXTRA)) {
            finish();
            return;
        }

        String json = getIntent().getStringExtra(ORDER_EXTRA);
        Gson gson = new Gson();
        order = gson.fromJson(json, Order.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        attachToolbar(toolbar, true);

        txtPickupPlace = findViewById(R.id.txt_pickup_dest);
        txtDropPlace = findViewById(R.id.txt_drop_dest);
        txtDateTime = findViewById(R.id.txt_date_time);
        txtTruckType = findViewById(R.id.txt_truck_type);
        txtTripCount = findViewById(R.id.txt_trip_count);

        txtFloorLevel = findViewById(R.id.txt_floor_level);
        swtElevator = findViewById(R.id.swt_elevator);
        txtParkingDistance = findViewById(R.id.txt_parking_distance);
        txtWeight = findViewById(R.id.txt_weight);
        txtArea = findViewById(R.id.txt_area);
        txtExtra = findViewById(R.id.txt_extra);

        txtFloorLevelD = findViewById(R.id.txt_floor_level_d);
        swtElevatorD = findViewById(R.id.swt_elevator_d);
        txtParkingDistanceD = findViewById(R.id.txt_parking_distance_d);
        txtExtraD = findViewById(R.id.txt_extra_d);

        cvInitialPaymentInfo = findViewById(R.id.cv_initial_payment_info);
        txtInitialPaymentAmount = findViewById(R.id.txt_initial_payment_amount);
        txtInitialPaymentStatus = findViewById(R.id.txt_initial_payment_status);

        cvFinalPaymentInfo = findViewById(R.id.cv_final_payment_info);
        txtFinalPaymentAmount = findViewById(R.id.txt_final_payment_amount);
        txtFinalPaymentStatus = findViewById(R.id.txt_final_payment_status);

        cvBidsButton = findViewById(R.id.cv_bids_click);
        CardView cvCancelButton = findViewById(R.id.cv_cancel_click);

        txtInitialPaymentAmount.setText("$25.00");
        if (order.getOrderStatus().equals("CANCELLED"))
            txtInitialPaymentStatus.setText("REVERSED");
        else
            txtInitialPaymentStatus.setText("SUCCESS");

        cvFinalPaymentInfo.setVisibility(View.GONE);
        /*if (order.getOrderStatus().equals("PENDING")) {
            cvFinalPaymentInfo.setVisibility(View.GONE);
        } else {
            txtFinalPaymentStatus.setText("SUCCESS");
        }*/

        if (order.getOrderStatus().equals("CONFIRMED")) {
            TextView txtButton = findViewById(R.id.txt_button);
            txtButton.setText("View Accepted Bid");
        }

        if (order.getOrderStatus().equals("COMPLETED")) {
            cvBidsButton.setVisibility(View.GONE);
        }

        if (order.getOrderStatus().equals("PENDING") || order.getOrderStatus().equals("CONFIRMED")) {
            cvCancelButton.setVisibility(View.VISIBLE);
        } else {
            cvCancelButton.setVisibility(View.GONE);
        }

        loading = new AlertDialog.Builder(this, R.style.SMDatePickerTheme)
                .setMessage("Cancelling order..")
                .setCancelable(false)
                .create();

        cvBidsButton.setOnClickListener(button -> {
            BidsActivity.start(this, order.getOrderId(), 11);
        });
        cvCancelButton.setOnClickListener(button -> {
            showLoading();
            ApiClient.create().cancelOrder(order.getOrderId())
                    .enqueue(cancelBidCallback);
        });

        populateOrder(order);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 11 && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    private void populateOrder(Order order) {
        txtPickupPlace.setText(order.getPickupPlace());
        txtDropPlace.setText(order.getDropPlace());
        txtDateTime.setText(CalenderUtil.getDisplayDateTime(order.getTime()));
        txtTruckType.setText(getTruckTypeText(order.getTruckTypeId()));
        txtTripCount.setText(order.getEstimatedNumOfTrips() == null ? "--" : String.valueOf(order.getEstimatedNumOfTrips()));

        txtFloorLevel.setText(order.getPickupFloor());
        swtElevator.setChecked(order.isPickupHasElevator());
        swtElevator.setEnabled(false);
        txtParkingDistance.setText(order.getPickupDistanceFromParking());
        txtWeight.setText(order.getEstimatedWeight().isEmpty() ? "--" : order.getEstimatedWeight());
        txtArea.setText(order.getEstimatedArea().isEmpty() ? "--" : order.getEstimatedArea());
        txtExtra.setText(order.getPickupAdditionalInfo().trim().isEmpty() ? "--" : order.getPickupAdditionalInfo());

        txtFloorLevelD.setText(order.getDropFloor());
        swtElevatorD.setChecked(order.isDropHasElevator());
        swtElevatorD.setEnabled(false);
        txtParkingDistanceD.setText(order.getDropDistanceFromParking());
        txtExtraD.setText(order.getDropAdditionalInfo().trim().isEmpty() ? "--" : order.getDropAdditionalInfo());

        txtInitialPaymentAmount.setText("$25.00");
        txtInitialPaymentStatus.setText("SUCCESS");
    }

    private String getTruckTypeText(int truckTypeId) {
        switch (truckTypeId) {
            case 1:
                return "Standard Car";
            case 2:
                return "Pickup Truck";
            case 3:
                return "Cargo Truck";
            default:
                return "--";
        }
    }
}
