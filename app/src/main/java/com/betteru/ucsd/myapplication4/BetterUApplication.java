package com.betteru.ucsd.myapplication4;

/**
 * Created by verazou on 11/12/17.
 */

import android.app.Application;
import android.content.SharedPreferences;

import com.facebook.FacebookSdk;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BetterUApplication extends Application {

    // Tag used when logging all messages with the same tag (e.g. for demoing purposes)
    public static final String TAG = "BetterU";

    private boolean loggedIn = false;
    public static final String LOGGED_IN_KEY = "logged_in";

    private JSONObject currentFBUser;
    public static final String CURRENT_FB_USER_KEY = "current_fb_user";

    private JSONArray friends;

    public static final String FRIENDS_KEY = "friends";

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
        if (!loggedIn) {

        }
    }

    public JSONObject getCurrentFBUser() {
        return currentFBUser;
    }

    public void setCurrentFBUser(JSONObject currentFBUser) {
        this.currentFBUser = currentFBUser;
    }

    public JSONArray getFriends() {
        return friends;
    }

    public ArrayList<String> getFriendsAsArrayListOfStrings() {
        ArrayList<String> friendsAsArrayListOfStrings = new ArrayList<String>();

        int numFriends = friends.length();
        for (int i = 0; i < numFriends; i++) {
            friendsAsArrayListOfStrings.add(getFriend(i).toString());
        }

        return friendsAsArrayListOfStrings;
    }

    public JSONObject getFriend(int index) {
        JSONObject friend = null;
        if (friends != null && friends.length() > index) {
            friend = friends.optJSONObject(index);
        }
        return friend;
    }

    public void setFriends(JSONArray friends) {
        this.friends = friends;
    }

}
