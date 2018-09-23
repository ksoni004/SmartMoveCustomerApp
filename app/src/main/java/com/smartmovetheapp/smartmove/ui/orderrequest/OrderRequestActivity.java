package com.smartmovetheapp.smartmove.ui.orderrequest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;

/*import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;*/
import com.smartmovetheapp.smartmove.R;
import com.smartmovetheapp.smartmove.data.remote.model.LoginResponse;
import com.smartmovetheapp.smartmove.data.remote.model.Order;
import com.smartmovetheapp.smartmove.data.repository.AuthRepository;
import com.smartmovetheapp.smartmove.data.repository.OrderRepository;
import com.smartmovetheapp.smartmove.ui.base.BaseActivity;
import com.smartmovetheapp.smartmove.ui.orderrequest.fragments.DropFragment;
import com.smartmovetheapp.smartmove.ui.orderrequest.fragments.OrderRequestFragment;
import com.smartmovetheapp.smartmove.ui.orderrequest.fragments.PaymentFragment;
import com.smartmovetheapp.smartmove.ui.orderrequest.fragments.PickupFragment;
import com.smartmovetheapp.smartmove.ui.orderrequest.fragments.SummaryFragment;

import java.net.HttpURLConnection;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderRequestActivity extends BaseActivity
        implements OrderRequestFragment.OrderRequestActionListener,
        PickupFragment.PickUpActionListener, DropFragment.DropActionListener,
        SummaryFragment.SummaryActionListener, PaymentFragment.PaymentActionListener {

    private int runningOrderState = OrderState.INTIAL_SCREEN;
    private Order order;
    AlertDialog loading;

    public static void start(Context context) {
        Intent starter = new Intent(context, OrderRequestActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_request);

        loading = new AlertDialog.Builder(this, R.style.SMDatePickerTheme)
                .setMessage("Creating your order..")
                .setCancelable(false)
                .create();

        order = new Order();

        attachToolbar(findViewById(R.id.toolbar), true);
        attachFragment(OrderRequestFragment.newInstance(), R.id.frm_fragment_container);
        getSupportActionBar().setTitle(OrderFactory.getTitleForState(runningOrderState));

        /*PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                //Log.i(TAG, "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                //Log.i(TAG, "An error occurred: " + status);
            }
        });*/
    }

    @Override
    public void onToolbarBackPress() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (runningOrderState == OrderState.INTIAL_SCREEN) {
            finish();
            return;
        }

        switch (runningOrderState) {
            case OrderState.PICK_UP_PLACE:
                runningOrderState = OrderState.INTIAL_SCREEN;
                break;
            case OrderState.DROP_PLACE:
                runningOrderState = OrderState.PICK_UP_PLACE;
                break;
            case OrderState.SUMMARY:
                runningOrderState = OrderState.DROP_PLACE;
                break;
            case OrderState.PAYMENT:
                runningOrderState = OrderState.SUMMARY;
                break;
        }
        getSupportActionBar().setTitle(OrderFactory.getTitleForState(runningOrderState));

        super.onBackPressed();
    }

    @Override
    public void onNextOfOrderClick(String pickup, String drop, long dateTime, String truckType, String tripCount) {
        order.setPickupPlace(pickup);
        order.setDropPlace(drop);
        order.setDate(dateTime);
        order.setTime(dateTime);
        int truckTypeId = truckType.equals("Standard Car")? 1 : truckType.equals("Pickup Truck")? 2 : 3;
        order.setTruckTypeId(truckTypeId);
        order.setEstimatedNumOfTrips(Integer.parseInt(tripCount));

        runningOrderState = OrderState.PICK_UP_PLACE;

        attachFragment(OrderFactory.getFragmentForState(runningOrderState), R.id.frm_fragment_container);
        getSupportActionBar().setTitle(OrderFactory.getTitleForState(runningOrderState));
    }

    @Override
    public void onNextOfPickupClick(String floorLevel, boolean elevator, String parkingDistance, String weight, String area, String extra) {
        order.setPickupFloor(floorLevel);
        order.setPickupHasElevator(elevator);
        order.setPickupDistanceFromParking(parkingDistance);
        order.setEstimatedWeight(weight);
        order.setEstimatedArea(area);
        order.setPickupAdditionalInfo(extra);

        runningOrderState = OrderState.DROP_PLACE;

        attachFragment(OrderFactory.getFragmentForState(runningOrderState), R.id.frm_fragment_container);
        getSupportActionBar().setTitle(OrderFactory.getTitleForState(runningOrderState));
    }

    @Override
    public void onNextOfDropClick(String floorLevel, boolean elevator, String parkingDistance, String weight, String area, String extra) {
        order.setDropFloor(floorLevel);
        order.setDropHasElevator(elevator);
        order.setDropDistanceFromParking(parkingDistance);
        order.setEstimatedWeight(weight);
        order.setEstimatedArea(area);
        order.setDropAdditionalInfo(extra);

        runningOrderState = OrderState.SUMMARY;

        attachFragment(OrderFactory.getFragmentForState(runningOrderState), R.id.frm_fragment_container);
        getSupportActionBar().setTitle(OrderFactory.getTitleForState(runningOrderState));
    }

    @Override
    public Order providePlacedOrder() {
        return order;
    }

    @Override
    public void onNextOfSummaryClick() {
        runningOrderState = OrderState.PAYMENT;

        attachFragment(OrderFactory.getFragmentForState(runningOrderState), R.id.frm_fragment_container);
        getSupportActionBar().setTitle(OrderFactory.getTitleForState(runningOrderState));
    }

    @Override
    public void onNextOfPaymentClick() {
        showLoading();
        performServerCall(providePlacedOrder());
    }

    private void showLoading() {
        loading.show();
    }

    private void hideLoading() {
        loading.dismiss();
    }

    private void performServerCall(Order orderDTO) {
        Date date = new Date();
        orderDTO.setOrderDateTime(new Timestamp(date.getTime()));
        orderDTO.setCustomerId(2);
        orderDTO.setPickupLat(1.11);
        orderDTO.setPickupLong(2.22);
        orderDTO.setDropLat(3.33);
        orderDTO.setDropLong(4.44);
        orderDTO.setOrderStatus("PENDING");
        OrderRepository.getInstance().attemptCreateOrder(orderDTO).enqueue(createOrderCallback);
    }

    private final Callback<Order> createOrderCallback = new Callback<Order>() {
        @Override
        public void onFailure(Call<Order> call, Throwable t) {
            Log.d("==AAAA1==", t.getLocalizedMessage());
            Log.d("==AAAA2==", t.getMessage());
            hideLoading();
            showError("Please try again we are facing some issue");
        }

        @Override
        public void onResponse(Call<Order> call, Response<Order> response) {
            hideLoading();
            if (response.isSuccessful() && response.body() != null) {
                if (response.body() != null) {
                    //Successs
                    new AlertDialog.Builder(OrderRequestActivity.this, R.style.SMDatePickerTheme)
                            .setTitle("Your order has been placed.")
                            .setMessage("We will notify you once someone bids for your order.")
                            .setCancelable(false)
                            .setPositiveButton("OK", (dialog, which) -> {
                                order.setOrderStatus("Pending");
                                OrderRepository.storeOrder(order);
                                dialog.dismiss();
                                finish();
                            })
                            .show();
                    Log.d("OrderDTO", response.body().toString());
                    //showError("Aaala" + response.body().getCustomerId());
                } else {
                    showError("Please try try again we are facing some issue");
                }
            } else {
                if (response.code() == HttpURLConnection.HTTP_BAD_REQUEST) {
                    showError("Bad request error");
                } else {
                    showError("Other error code");
                }
            }
        }
    };
}