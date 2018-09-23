package com.smartmovetheapp.smartmove.ui.bids;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.smartmovetheapp.smartmove.R;
import com.smartmovetheapp.smartmove.data.remote.ApiClient;
import com.smartmovetheapp.smartmove.ui.base.BaseActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BidsActivity extends BaseActivity {

    private static final String ORDER_ID_EXTRA = "order_id";

    private int orderId;
    private Snackbar loadingSnackbar;
    private final Callback<Void> bidsCallback = new Callback<Void>() {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {
            hideLoading();
        }

        @Override
        public void onFailure(Call<Void> call, Throwable t) {
            hideLoading();
        }
    };

    public static void start(Context context, int orderId) {
        Intent starter = new Intent(context, BidsActivity.class);
        starter.putExtra(ORDER_ID_EXTRA, orderId);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bids);

        if (!getIntent().hasExtra(ORDER_ID_EXTRA)) {
            finish();
            return;
        }

        orderId = getIntent().getIntExtra(ORDER_ID_EXTRA, 0);

        Toolbar toolbar = findViewById(R.id.toolbar);
        attachToolbar(toolbar, true);

        loadingSnackbar = Snackbar.make(findViewById(android.R.id.content),
                "Getting active trips..", Snackbar.LENGTH_INDEFINITE);

        /*rvTrips = findViewById(R.id.rv_trips);
        txtEmptyTrips = findViewById(R.id.txt_empty_trips);

        rvTrips.setLayoutManager(new LinearLayoutManager(this));
        tripAdapter = new TripAdapter(order ->
                TripDetailActivity.start(this, order));
        rvTrips.setAdapter(tripAdapter);*/

        performServerCall();
    }

    private void performServerCall() {
        showLoading();
        ApiClient.create().getBids(orderId)
                .enqueue(bidsCallback);
    }

    /*private void loadBids(List<Order> trips) {
        if (trips == null || trips.isEmpty()) {
            txtEmptyTrips.setVisibility(View.VISIBLE);
        } else {
            tripAdapter.submitList(trips);
        }
    }*/

    private void showLoading() {
        loadingSnackbar.show();
    }

    private void hideLoading() {
        loadingSnackbar.dismiss();
    }
}
