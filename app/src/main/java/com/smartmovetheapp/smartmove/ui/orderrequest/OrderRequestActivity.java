package com.smartmovetheapp.smartmove.ui.orderrequest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.smartmovetheapp.smartmove.R;
import com.smartmovetheapp.smartmove.data.remote.model.Order;
import com.smartmovetheapp.smartmove.data.remote.model.Place;
import com.smartmovetheapp.smartmove.ui.base.BaseActivity;
import com.smartmovetheapp.smartmove.ui.orderrequest.fragments.DropFragment;
import com.smartmovetheapp.smartmove.ui.orderrequest.fragments.OrderRequestFragment;
import com.smartmovetheapp.smartmove.ui.orderrequest.fragments.PickupFragment;

public class OrderRequestActivity extends BaseActivity
        implements OrderRequestFragment.OrderRequestActionListener,
        PickupFragment.PickUpActionListener, DropFragment.DropActionListener {

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
}
