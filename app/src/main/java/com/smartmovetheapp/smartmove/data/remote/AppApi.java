package com.smartmovetheapp.smartmove.data.remote;

import com.smartmovetheapp.smartmove.R;
import com.smartmovetheapp.smartmove.data.remote.model.Customer;
import com.smartmovetheapp.smartmove.data.remote.model.OrderBid;
import com.smartmovetheapp.smartmove.data.remote.model.Rating;
import com.smartmovetheapp.smartmove.data.remote.model.User;
import com.smartmovetheapp.smartmove.data.remote.model.Order;
import com.smartmovetheapp.smartmove.data.remote.model.TripResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface AppApi {

    /*api calls*/
    @POST("api/Account/Login")
    Call<User> login(@Body Customer request);

    @POST("api/users")
    Call<User> signup(@Body Customer request);

    @POST("api/Customer/CreateOrder")
    Call<Order> createOrder(@Body Order order);

    @GET("api/Customer/GetOrders")
    Call<TripResponse> getOrders(@Query("customerId") Long customerId);

    @GET("api/Customer/GetOrderBids")
    Call<List<OrderBid>> getBids(@Query("orderId") int orderId);

    //api/Customer/AcceptBid?bidId=value
    @POST("api/Customer/AcceptBid")
    Call<Void> selectBid(@Query("bidId") int bidId);

    @POST("api/Customer/CancelOrder")
    Call<Void> cancelOrder(@Query("orderId") int orderId);

    @POST("api/Customer/RateDriver")
    Call<Void> subitRating(@Body Rating rating);
}