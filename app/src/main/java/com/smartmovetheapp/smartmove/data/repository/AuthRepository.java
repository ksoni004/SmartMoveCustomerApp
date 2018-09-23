package com.smartmovetheapp.smartmove.data.repository;

import com.smartmovetheapp.smartmove.data.remote.ApiClient;
import com.smartmovetheapp.smartmove.data.remote.model.User;
import com.smartmovetheapp.smartmove.data.remote.model.Customer;

import retrofit2.Call;

public class AuthRepository {

    private static AuthRepository repositoryInstance;

    public static AuthRepository getInstance() {
        if (repositoryInstance == null) {
            repositoryInstance = new AuthRepository();
        }

        return repositoryInstance;
    }

    public Call<User> attemptLogin(String username, String password) {
        return ApiClient.create().login(new Customer(username, password, "C"));
    }

    public Call<User> registerNewUser(Customer customer) { return ApiClient.create().signup(customer); }
}
