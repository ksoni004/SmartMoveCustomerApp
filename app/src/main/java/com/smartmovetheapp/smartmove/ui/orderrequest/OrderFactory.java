package com.smartmovetheapp.smartmove.ui.orderrequest;

import android.support.v4.app.Fragment;

import com.smartmovetheapp.smartmove.ui.orderrequest.fragments.DropFragment;
import com.smartmovetheapp.smartmove.ui.orderrequest.fragments.OrderRequestFragment;
import com.smartmovetheapp.smartmove.ui.orderrequest.fragments.PaymentFragment;
import com.smartmovetheapp.smartmove.ui.orderrequest.fragments.PickupFragment;
import com.smartmovetheapp.smartmove.ui.orderrequest.fragments.SummaryFragment;

public class OrderFactory {

    public static Fragment getFragmentForState(int state) {
        switch (state) {
            default:
            case OrderState.INTIAL_SCREEN:
                return OrderRequestFragment.newInstance();
            case OrderState.PICK_UP_PLACE:
                return PickupFragment.newInstance();
            case OrderState.DROP_PLACE:
                return DropFragment.newInstance();
            case OrderState.SUMMARY:
                return SummaryFragment.newInstance();
            case OrderState.PAYMENT:
                return PaymentFragment.newInstance();
        }
    }

    public static String getTitleForState(int state) {
        switch (state) {
            default:
            case OrderState.INTIAL_SCREEN:
                return "Enter Order";
            case OrderState.PICK_UP_PLACE:
                return "Pickup Details";
            case OrderState.DROP_PLACE:
                return "Destination Details";
            case OrderState.SUMMARY:
                return "Summary";
            case OrderState.PAYMENT:
                return "Payment";
        }
    }
}
