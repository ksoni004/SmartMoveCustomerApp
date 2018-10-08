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
import com.smartmovetheapp.smartmove.data.remote.model.OrderBid;

public class BidPaymentFragment extends Fragment {

    private CardView cvPayment;

    private PaymentActionListener actionListener;
    private String amount;
    private OrderBid orderBid;

    public static BidPaymentFragment newInstance(OrderBid orderBid, String amount) {
        BidPaymentFragment bidPaymentFragment = new BidPaymentFragment();
        bidPaymentFragment.amount = amount;
        bidPaymentFragment.orderBid = orderBid;
        return bidPaymentFragment;
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
        TextView txtAmount = view.findViewById(R.id.txt_payment_amount);
        txtAmount.setText(String.format("$%s", amount));
        TextView txtMessage = view.findViewById(R.id.textView2);
        txtMessage.setText("Remaining Payment will be needed to Accept this Bid");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cvPayment.setOnClickListener(button -> onNextClick());
    }

    private void onNextClick() {
        /*AlertDialog loading = new AlertDialog.Builder(getContext(), R.style.SMDatePickerTheme)
                .setMessage("Transaction in progress..")
                .create();

        loading.show();
        new Handler(Looper.getMainLooper())
                .postDelayed(() -> {
                    loading.dismiss();
                    actionListener.onNextOfPaymentClick();
                }, 3000L);*/
        actionListener.onNextOfPaymentClick(orderBid);
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
        void onNextOfPaymentClick(OrderBid orderBid);
    }
}
