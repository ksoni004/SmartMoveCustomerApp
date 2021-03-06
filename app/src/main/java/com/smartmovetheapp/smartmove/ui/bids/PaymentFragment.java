package com.smartmovetheapp.smartmove.ui.bids;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smartmovetheapp.smartmove.R;

public class PaymentFragment extends Fragment {

    private CardView cvPayment;
    private TextView txtPaymentAmount;

    private PaymentActionListener actionListener;

    public static PaymentFragment newInstance() {
        return new PaymentFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cvPayment = view.findViewById(R.id.cv_start_payment);
        txtPaymentAmount = view.findViewById(R.id.txt_payment_amount);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //txtPaymentAmount.setText("$");
        cvPayment.setOnClickListener(button -> onNextClick());
    }

    private void onNextClick() {
        actionListener.onNextOfPaymentClick();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PaymentActionListener) {
            actionListener = (PaymentActionListener) context;
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

    public interface PaymentActionListener {
        void onNextOfPaymentClick();
    }
}
