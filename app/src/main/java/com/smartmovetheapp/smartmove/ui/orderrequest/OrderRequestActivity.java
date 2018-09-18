package com.smartmovetheapp.smartmove.ui.orderrequest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

/*import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;*/
import com.smartmovetheapp.smartmove.R;
import com.smartmovetheapp.smartmove.data.remote.model.Order;
import com.smartmovetheapp.smartmove.data.remote.model.Place;
import com.smartmovetheapp.smartmove.data.repository.OrderRepository;
import com.smartmovetheapp.smartmove.ui.base.BaseActivity;
import com.smartmovetheapp.smartmove.ui.orderrequest.fragments.DropFragment;
import com.smartmovetheapp.smartmove.ui.orderrequest.fragments.OrderRequestFragment;
import com.smartmovetheapp.smartmove.ui.orderrequest.fragments.PaymentFragment;
import com.smartmovetheapp.smartmove.ui.orderrequest.fragments.PickupFragment;
import com.smartmovetheapp.smartmove.ui.orderrequest.fragments.SummaryFragment;

public class OrderRequestActivity extends BaseActivity
        implements OrderRequestFragment.OrderRequestActionListener,
        PickupFragment.PickUpActionListener, DropFragment.DropActionListener,
        SummaryFragment.SummaryActionListener, PaymentFragment.PaymentActionListener {

    private int runningOrderState = OrderState.INTIAL_SCREEN;
    private Order order;

    public static void start(Context context) {
        Intent starter = new Intent(context, OrderRequestActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_request);

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
        order.setPickupPlace(new Place(pickup));
        order.setDropPlace(new Place(drop));
        order.setDate(dateTime);
        order.setTime(dateTime);
        order.setTruckType(truckType);
        order.setNoOfTrips(tripCount);

        runningOrderState = OrderState.PICK_UP_PLACE;

        attachFragment(OrderFactory.getFragmentForState(runningOrderState), R.id.frm_fragment_container);
        getSupportActionBar().setTitle(OrderFactory.getTitleForState(runningOrderState));
    }

    @Override
    public void onNextOfPickupClick(String floorLevel, boolean elevator, String parkingDistance, String weight, String area, String extra) {
        order.getPickupPlace().setFloorLevel(floorLevel);
        order.getPickupPlace().setElevator(elevator);
        order.getPickupPlace().setParkingDistance(parkingDistance);
        order.getPickupPlace().setWeight(weight);
        order.getPickupPlace().setArea(area);
        order.getPickupPlace().setAdditionalInfo(extra);

        runningOrderState = OrderState.DROP_PLACE;

        attachFragment(OrderFactory.getFragmentForState(runningOrderState), R.id.frm_fragment_container);
        getSupportActionBar().setTitle(OrderFactory.getTitleForState(runningOrderState));
    }

    @Override
    public void onNextOfDropClick(String floorLevel, boolean elevator, String parkingDistance, String weight, String area, String extra) {
        order.getDropPlace().setFloorLevel(floorLevel);
        order.getDropPlace().setElevator(elevator);
        order.getDropPlace().setParkingDistance(parkingDistance);
        order.getDropPlace().setWeight(weight);
        order.getDropPlace().setArea(area);
        order.getDropPlace().setAdditionalInfo(extra);

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
        new AlertDialog.Builder(this, R.style.SMDatePickerTheme)
                .setTitle("Your order has been placed.")
                .setMessage("We will notify you once someone bids for your order.")
                .setPositiveButton("OK", (dialog, which) -> {
                    order.setStatus("Pending");
                    OrderRepository.storeOrder(order);
                    dialog.dismiss();
                    finish();
                })
                .show();
    }
}
