package com.betteru.ucsd.myapplication4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.app.Activity;
import android.util.Log;

import com.facebook.AccessToken;

public class StartupActivity extends Activity {

    protected BetterUApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        app = (BetterUApplication) getApplication();
        boolean isLoggedIn = isLoggedIn();

        app.clearCurrentFBUser();
        app.clearFriendList();

        if (isLoggedIn) {
            Log.d(BetterUApplication.TAG, "LOGGED IN");
            app.setLoggedIn(true);
            Intent startupIntent = new Intent(this, MainActivity.class);
            startupIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(startupIntent);
            finish();
        } else {
            Log.d(BetterUApplication.TAG, "LOGGED OUT");
            app.setLoggedIn(false);
            Intent startupIntent = new Intent(this, LoginActivity.class);
            startActivity(startupIntent);
            finish();
        }

        super.onCreate(savedInstanceState);
    }

    private boolean isLoggedIn() {
        //return true;

        AccessToken accesstoken = AccessToken.getCurrentAccessToken();
        Log.d(BetterUApplication.TAG+"LogIn", String.valueOf(accesstoken != null));
        return accesstoken != null;
    }

}
