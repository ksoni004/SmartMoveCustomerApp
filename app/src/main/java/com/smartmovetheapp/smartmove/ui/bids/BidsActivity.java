package com.smartmovetheapp.smartmove.ui.bids;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.smartmovetheapp.smartmove.R;
import com.smartmovetheapp.smartmove.data.remote.ApiClient;
import com.smartmovetheapp.smartmove.data.remote.model.OrderBid;
import com.smartmovetheapp.smartmove.ui.base.BaseActivity;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BidsActivity extends BaseActivity implements BidPaymentFragment.PaymentActionListener {

    private static final String ORDER_ID_EXTRA = "order_id";

    private int orderId;
    private AlertDialog acceptLoading;
    private Snackbar loadingSnackbar;

    private final Callback<List<OrderBid>> bidsCallback = new Callback<List<OrderBid>>() {
        @Override
        public void onResponse(Call<List<OrderBid>> call, Response<List<OrderBid>> response) {
            hideLoading();
            if (response.isSuccessful()) {
                loadBids(response.body());
            } else {
                showError(R.string.default_error);
            }
        }

        @Override
        public void onFailure(Call<List<OrderBid>> call, Throwable t) {
            hideLoading();
            showError(R.string.default_error);
        }
    };
    private TextView txtEmptyTrips;
    private RecyclerView rvTrips;
    private BidAdapter bidAdapter;
    private final Callback<Void> acceptBidCallback = new Callback<Void>() {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {
            hideAcceptBidLoading();
            if (response.isSuccessful()) {
                new AlertDialog.Builder(BidsActivity.this, R.style.SMDatePickerTheme)
                        .setTitle("Bid has been accepted.")
                        .setMessage("Bid has been allocated to your order.")
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
            hideAcceptBidLoading();
            showError(R.string.default_error);
        }
    };

    public static void start(Activity context, int orderId, int requestCode) {
        Log.d("BidsActivity", "start: called");
        Intent starter = new Intent(context, BidsActivity.class);
        starter.putExtra(ORDER_ID_EXTRA, orderId);
        context.startActivityForResult(starter, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("BidsActivity", "onCreate: called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bids);

        if (!getIntent().hasExtra(ORDER_ID_EXTRA)) {
            finish();
            return;
        }

        acceptLoading = new AlertDialog.Builder(this, R.style.SMDatePickerTheme)
                .setMessage("Finalizing your Order..")
                .setCancelable(false)
                .create();

        orderId = getIntent().getIntExtra(ORDER_ID_EXTRA, 0);

        Toolbar toolbar = findViewById(R.id.toolbar);
        attachToolbar(toolbar, true);

        loadingSnackbar = Snackbar.make(findViewById(android.R.id.content),
                "Getting Received Bids..", Snackbar.LENGTH_INDEFINITE);

        rvTrips = findViewById(R.id.rv_bids);
        txtEmptyTrips = findViewById(R.id.txt_empty_trips);

        rvTrips.setLayoutManager(new LinearLayoutManager(this));
        bidAdapter = new BidAdapter(this::onAcceptBid);
        rvTrips.setAdapter(bidAdapter);

        performServerCall();
    }

    private void onAcceptBid(OrderBid orderBid) {
        setTitle("Payment");
        attachFragment(BidPaymentFragment.newInstance(orderBid, String.valueOf(orderBid.getBidAmount() - 25.0)), R.id.frm_fragment_container);
    }

    private void showAcceptBidLoading() {
        acceptLoading.show();
    }

    private void hideAcceptBidLoading() {
        acceptLoading.dismiss();
    }

    @Override
    public void onNextOfPaymentClick(OrderBid orderBid) {
        showAcceptBidLoading();
        ApiClient.create().selectBid(orderBid.bidId)
                .enqueue(acceptBidCallback);
    }

    private void performServerCall() {
        showLoading();
        ApiClient.create().getBids(orderId)
                .enqueue(bidsCallback);
    }

    private void loadBids(List<OrderBid> bids) {
        if (bids == null || bids.isEmpty()) {
            txtEmptyTrips.setVisibility(View.VISIBLE);
        } else {
            for (OrderBid bid : bids) {
                if (bid.getBidStatus().equals("ACCEPTED")) {
                    bidAdapter.submitList(Collections.singletonList(bid));
                    return;
                }
            }

            bidAdapter.submitList(bids);
        }
    }

    private void showLoading() {
        loadingSnackbar.show();
    }

    private void hideLoading() {
        loadingSnackbar.dismiss();
    }

    @Override
    public void onToolbarBackPress() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {

            setTitle("Bids received");
        }

        super.onBackPressed();
    }
}
