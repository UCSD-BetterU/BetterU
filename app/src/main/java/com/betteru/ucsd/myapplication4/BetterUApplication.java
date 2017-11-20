package com.betteru.ucsd.myapplication4;

/**
 * Created by verazou on 11/12/17.
 */

import android.app.Application;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.facebook.FacebookSdk;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class BetterUApplication extends Application {

    // Tag used when logging all messages with the same tag (e.g. for demoing purposes)
    public static final String TAG = "BetterU";

    private boolean loggedIn = false;
    public static final String LOGGED_IN_KEY = "logged_in";

    private UserModel currentFBUser;
    public static final String CURRENT_FB_USER_KEY = "current_fb_user";

    private ArrayList<UserModel> friendList;

    public static final String FRIENDS_KEY = "friends";

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
        if (!loggedIn) {

        }
    }

    public UserModel getCurrentFBUser() { return currentFBUser; }

    public void setCurrentFBUser(UserModel currentFBUser) {
        this.currentFBUser = currentFBUser;
    }

    public ArrayList<UserModel> getFriendList() {
        return this.friendList;
    }

    public void clearCurrentFBUser() { this.currentFBUser = null; }

    public void setFriendList(ArrayList<UserModel> friendList) {
        this.friendList = friendList;
    }

    public void addToFriendList(UserModel friend) {
        if (this.friendList == null) {
            this.friendList = new ArrayList<>();
        }
        this.friendList.add(friend);
    }

    public void clearFriendList() { this.friendList = null; }

    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

}
