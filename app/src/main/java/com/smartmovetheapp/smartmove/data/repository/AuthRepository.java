package com.smartmovetheapp.smartmove.data.repository;

import com.smartmovetheapp.smartmove.data.remote.ApiClient;
import com.smartmovetheapp.smartmove.data.remote.model.LoginResponse;
import com.smartmovetheapp.smartmove.data.remote.model.User;

import retrofit2.Call;

public class AuthRepository {

    private static AuthRepository repositoryInstance;

    public static AuthRepository getInstance() {
        if (repositoryInstance == null) {
            repositoryInstance = new AuthRepository();
        }

        return repositoryInstance;
    }

    public Call<LoginResponse> attemptLogin(String username, String password) {
        return ApiClient.create().login(new User(/*username, password*/));
    }

    public Call<LoginResponse> registerNewUser(User user) { return ApiClient.create().signup(user); }
}
