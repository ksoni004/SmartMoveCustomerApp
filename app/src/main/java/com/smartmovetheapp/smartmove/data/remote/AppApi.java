package com.smartmovetheapp.smartmove.data.remote;

import com.smartmovetheapp.smartmove.data.remote.model.Customer;
import com.smartmovetheapp.smartmove.data.remote.model.User;
import com.smartmovetheapp.smartmove.data.remote.model.Order;
import com.smartmovetheapp.smartmove.data.remote.model.TripResponse;

import retrofit2.Call;
import retrofit2.http.*;

public interface AppApi {

    /*api calls*/
    @POST("api/Account/Login")
    Call<User> login(@Body Customer request);

    @POST("api/users")
    Call<User> signup(@Body Customer request);

    @POST("api/Customer/CreateOrder")
    Call<Void> createOrder(@Body Order order);

    //api/Customer/CreateOrder?customerId=2
    @GET("api/Customer/GetOrders")
    Call<TripResponse> getTrips(@Query("customerId") Long customerId);
}