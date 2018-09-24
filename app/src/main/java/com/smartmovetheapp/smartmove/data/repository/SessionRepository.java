package com.smartmovetheapp.smartmove.data.repository;

import com.google.gson.Gson;
import com.smartmovetheapp.smartmove.data.remote.model.User;
import com.smartmovetheapp.smartmove.data.sharedpref.SharedPrefs;
import com.smartmovetheapp.smartmove.data.sharedpref.StateMachine;

public class SessionRepository {

    private static SessionRepository repositoryInstance;

    public static SessionRepository getInstance() {
        if (repositoryInstance == null) {
            repositoryInstance = new SessionRepository();
        }

        return repositoryInstance;
    }

    private SessionRepository() {}

    public int getCustomerId() {
        User user = getLoggedInUser();

        if (user == null) {
            return 0;
        }

        try {
            return user.getCustomerId().intValue();
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public void logout() {
        StateMachine.changeStateTo(StateMachine.State.NO_LOGIN);
        SharedPrefs.getInstance().remove(SharedPrefKey.LOGGED_IN_USER);
    }

    private interface SharedPrefKey {
        String LOGGED_IN_USER = "logged_in_user";
    }

    public void storeUser(User user) {
        Gson gson = new Gson();
        SharedPrefs.getInstance().addString(SharedPrefKey.LOGGED_IN_USER, gson.toJson(user));
    }

    public User getLoggedInUser() {
        String json = SharedPrefs.getInstance().getString(SharedPrefKey.LOGGED_IN_USER, null);

        if (json == null) {
            return null;
        }

        Gson gson = new Gson();
        return gson.fromJson(json, User.class);
    }
}
