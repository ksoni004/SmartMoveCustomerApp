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

import com.smartmovetheapp.smartmove.R;
import com.smartmovetheapp.smartmove.ui.base.BaseActivity;

public class SummaryFragment extends Fragment {

    private FloatingActionButton fabNext;
    private EditText edtFloorLevel;
    private EditText edtParkingDistance;
    private EditText edtWeight;
    private EditText edtArea;
    private EditText edtExtra;
    private SwitchCompat swtElevator;

    private SummaryActionListener actionListener;

    public static SummaryFragment newInstance() {
        return new SummaryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_place_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fabNext = view.findViewById(R.id.fab_next);
        edtFloorLevel = view.findViewById(R.id.edt_floor_level);
        edtParkingDistance = view.findViewById(R.id.edt_parking_distance);
        edtWeight = view.findViewById(R.id.edt_weight);
        edtArea = view.findViewById(R.id.edt_area);
        edtExtra = view.findViewById(R.id.edt_extra);
        swtElevator = view.findViewById(R.id.swt_elevator);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fabNext.setOnClickListener(button -> onNextClick());
    }

    private void onNextClick() {
        try {
            validateInput();
            actionListener.onNextOfSummaryClick(
                    edtFloorLevel.getText().toString(),
                    swtElevator.isChecked(),
                    edtParkingDistance.getText().toString(),
                    edtWeight.getText().toString(),
                    edtArea.getText().toString(),
                    edtExtra.getText().toString()
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
        void onNextOfSummaryClick(String floorLevel, boolean elevator, String parkingDistance, String weight, String area, String extra);
    }
}
