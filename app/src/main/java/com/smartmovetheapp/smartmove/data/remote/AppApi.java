package com.smartmovetheapp.smartmove.data.remote;

import com.smartmovetheapp.smartmove.data.remote.model.Customer;
import com.smartmovetheapp.smartmove.data.remote.model.LoginResponse;
import com.smartmovetheapp.smartmove.data.remote.model.Order;

import retrofit2.Call;
import retrofit2.http.*;

public interface AppApi {

    /*api calls*/
    @POST("api/Account/Login")
    Call<LoginResponse> login(@Body Customer request);

    @POST("api/users")
    Call<LoginResponse> signup(@Body Customer request);

    @POST("api/Customer/CreateOrder")
    Call<Order> createOrder(@Body Order order);
}