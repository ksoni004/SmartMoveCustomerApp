package com.smartmovetheapp.smartmove.ui.bids;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.smartmovetheapp.smartmove.R;
import com.smartmovetheapp.smartmove.data.remote.ApiClient;
import com.smartmovetheapp.smartmove.data.remote.model.OrderBid;
import com.smartmovetheapp.smartmove.ui.base.BaseActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BidsActivity extends BaseActivity {

    private static final String ORDER_ID_EXTRA = "order_id";

    private int orderId;
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

    public static void start(Context context, int orderId) {
        Log.d("BidsActivity", "start: called");
        Intent starter = new Intent(context, BidsActivity.class);
        starter.putExtra(ORDER_ID_EXTRA, orderId);
        context.startActivity(starter);
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

        orderId = getIntent().getIntExtra(ORDER_ID_EXTRA, 0);

        Toolbar toolbar = findViewById(R.id.toolbar);
        attachToolbar(toolbar, true);

        loadingSnackbar = Snackbar.make(findViewById(android.R.id.content),
                "Getting received bids..", Snackbar.LENGTH_INDEFINITE);

        rvTrips = findViewById(R.id.rv_bids);
        txtEmptyTrips = findViewById(R.id.txt_empty_trips);

        rvTrips.setLayoutManager(new LinearLayoutManager(this));
        bidAdapter = new BidAdapter(orderBid -> {

        });
        rvTrips.setAdapter(bidAdapter);

        performServerCall();
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
            bidAdapter.submitList(bids);
        }
    }

    private void showLoading() {
        loadingSnackbar.show();
    }

    private void hideLoading() {
        loadingSnackbar.dismiss();
    }
}
