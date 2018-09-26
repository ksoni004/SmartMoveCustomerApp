package com.smartmovetheapp.smartmove.data.repository;

import com.google.gson.Gson;
import com.smartmovetheapp.smartmove.data.remote.ApiClient;
import com.smartmovetheapp.smartmove.data.remote.model.Order;
import com.smartmovetheapp.smartmove.data.sharedpref.SharedPrefs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import retrofit2.Call;

public class OrderRepository {

    private static OrderRepository repositoryInstance;

    public static OrderRepository getInstance() {
        if (repositoryInstance == null) {
            repositoryInstance = new OrderRepository();
        }

        return repositoryInstance;
    }

    private static final String STORED_ORDERS = "stored_orders";

    public static void storeOrder(Order orderToStore) {
        List<Order> orders = getStoredListOfOrder();
        orders.add(orderToStore);

        Gson gson = new Gson();
        Set<String> stringList = new TreeSet<>();

        for (Order order : orders) {
            stringList.add(gson.toJson(order));
        }

        SharedPrefs.getInstance().addStringList(STORED_ORDERS, stringList);
    }

    public static List<Order> getStoredListOfOrder() {
        Set<String> stringList = SharedPrefs.getInstance().getStringList(STORED_ORDERS);

        Gson gson = new Gson();
        List<Order> orders = new ArrayList<>();

        if (stringList != null) {
            for (String orderString : stringList) {
                orders.add(gson.fromJson(orderString, Order.class));
            }
        }

        return orders;
    }

    public Call<Order> attemptCreateOrder(Order orderDto) {
        Order o = new Order();
        o = orderDto;
        return ApiClient.create().createOrder(o);
    }
}
