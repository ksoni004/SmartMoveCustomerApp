package com.smartmovetheapp.smartmove.ui.orderrequest.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.smartmovetheapp.smartmove.R;
import com.smartmovetheapp.smartmove.data.remote.model.Order;
import com.smartmovetheapp.smartmove.ui.base.BaseActivity;
import com.smartmovetheapp.smartmove.util.CalenderUtil;

import org.w3c.dom.Text;

public class SummaryFragment extends Fragment {

    private FloatingActionButton fabNext;

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
    private TextView txtWeightD;
    private TextView txtAreaD;
    private TextView txtExtraD;

    private SummaryActionListener actionListener;

    public static SummaryFragment newInstance() {
        return new SummaryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fabNext = view.findViewById(R.id.fab_next);

        txtPickupPlace = view.findViewById(R.id.txt_pickup_dest);
        txtDropPlace = view.findViewById(R.id.txt_drop_dest);
        txtDateTime = view.findViewById(R.id.txt_date_time);
        txtTruckType = view.findViewById(R.id.txt_truck_type);
        txtTripCount = view.findViewById(R.id.txt_trip_count);

        txtFloorLevel = view.findViewById(R.id.txt_floor_level);
        swtElevator = view.findViewById(R.id.swt_elevator);
        txtParkingDistance = view.findViewById(R.id.txt_parking_distance);
        txtWeight = view.findViewById(R.id.txt_weight);
        txtArea = view.findViewById(R.id.txt_area);
        txtExtra = view.findViewById(R.id.txt_extra);

        txtFloorLevelD = view.findViewById(R.id.txt_floor_level_d);
        swtElevatorD = view.findViewById(R.id.swt_elevator_d);
        txtParkingDistanceD = view.findViewById(R.id.txt_parking_distance_d);
        txtWeightD = view.findViewById(R.id.txt_weight_d);
        txtAreaD = view.findViewById(R.id.txt_area_d);
        txtExtraD = view.findViewById(R.id.txt_extra_d);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fabNext.setOnClickListener(button -> onNextClick());

        populateOrder(actionListener.providePlacedOrder());
    }

    private void populateOrder(Order order) {
        txtPickupPlace.setText(order.getPickupPlace().getLocationName());
        txtDropPlace.setText(order.getDropPlace().getLocationName());
        txtDateTime.setText(CalenderUtil.getDisplayDateTime(order.getDate()));
        txtTruckType.setText(order.getTruckType());
        txtTripCount.setText(order.getNoOfTrips());

        txtFloorLevel.setText(order.getPickupPlace().getFloorLevel());
        swtElevator.setChecked(order.getPickupPlace().isElevator());
        swtElevator.setEnabled(false);
        txtParkingDistance.setText(order.getPickupPlace().getParkingDistance());
        txtWeight.setText(order.getPickupPlace().getWeight());
        txtArea.setText(order.getPickupPlace().getArea());
        txtExtra.setText(order.getPickupPlace().getAdditionalInfo());

        txtFloorLevelD.setText(order.getDropPlace().getFloorLevel());
        swtElevatorD.setChecked(order.getDropPlace().isElevator());
        swtElevatorD.setEnabled(false);
        txtParkingDistanceD.setText(order.getDropPlace().getParkingDistance());
        txtWeightD.setText(order.getDropPlace().getWeight());
        txtAreaD.setText(order.getDropPlace().getArea());
        txtExtraD.setText(order.getDropPlace().getAdditionalInfo());
    }

    private void onNextClick() {
        actionListener.onNextOfSummaryClick();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SummaryActionListener) {
            actionListener = (SummaryActionListener) context;
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

    public interface SummaryActionListener {
        Order providePlacedOrder();

        void onNextOfSummaryClick();
    }
}
