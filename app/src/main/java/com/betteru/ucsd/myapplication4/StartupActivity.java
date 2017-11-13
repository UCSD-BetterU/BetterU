package com.betteru.ucsd.myapplication4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.app.Activity;

import com.facebook.AccessToken;

public class StartupActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (isLoggedIn()) {
            Intent startupIntent = new Intent(this, MainActivity.class);
            startupIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(startupIntent);
            finish();
        } else {
            Intent startupIntent = new Intent(this, LoginActivity.class);
            startActivity(startupIntent);
            finish();
        }

        super.onCreate(savedInstanceState);
    }

    private boolean isLoggedIn() {
        return true;
    /*
        AccessToken accesstoken = AccessToken.getCurrentAccessToken();
        return !(accesstoken == null || accesstoken.getPermissions().isEmpty());
    */
    }
}
