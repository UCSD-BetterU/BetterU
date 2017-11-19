package com.betteru.ucsd.myapplication4;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

public class LoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private LoginButton fbLoginButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();
        fbLoginButton = findViewById(R.id.loginButton);

        fbLoginButton.setReadPermissions("email", "public_profile");
        fbLoginButton.setReadPermissions(FBPermission.USER_FRIENDS.toString());
        AccessToken accesstoken = AccessToken.getCurrentAccessToken();
        Log.i(BetterUApplication.TAG, "log in!!!!!!!");

        // Callback registration
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                Toast.makeText(
                        LoginActivity.this,
                        R.string.login_success,
                        Toast.LENGTH_LONG).show();
                handleFacebookAccessToken(loginResult.getAccessToken());
                FBGraphAPICall meCall = FBGraphAPICall.callMe("first_name", new FBGraphAPICallback() {
                    @Override
                    public void handleResponse(GraphResponse response) {
                        JSONObject user = response.getJSONObject();
                        Log.i(BetterUApplication.TAG+"_USER", user.toString());
                        ((BetterUApplication) getApplication()).setCurrentFBUser(user);
                    }

                    @Override
                    public void handleError(FacebookRequestError error) {
                        showError(error.toString());
                    }
                });
                FBGraphAPICall myFriendsCall = FBGraphAPICall.callMeFriends("name,first_name", new FBGraphAPICallback() {
                    @Override
                    public void handleResponse(GraphResponse response) {
                        JSONArray friendsData = FBGraphAPICall.getDataFromResponse(response);
                        Log.i(BetterUApplication.TAG+"_FRIENDS", friendsData.toString());
                        ((BetterUApplication) getApplication()).setFriends(friendsData);
                    }

                    @Override
                    public void handleError(FacebookRequestError error) {
                        showError(error.toString());
                    }
                });
                // Create a RequestBatch and add a callback once the batch of requests completes
                GraphRequestBatch requestBatch = FBGraphAPICall.createRequestBatch(myFriendsCall, meCall);

                requestBatch.addCallback(new GraphRequestBatch.Callback() {
                    @Override
                    public void onBatchCompleted(GraphRequestBatch batch) {
                        if (((BetterUApplication)getApplication()).getCurrentFBUser() != null) {
                        //    loadPersonalizedFragment();
                        } else {
                        //    showError(getString(R.string.error_fetching_profile));
                        }
                    }
                });

                requestBatch.executeAsync();
                Intent startupIntent = new Intent(LoginActivity.this, MainActivity.class);
                startupIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(startupIntent);
                finish();
            }

            @Override
            public void onCancel() {
                Toast.makeText(
                        LoginActivity.this,
                        R.string.login_cancel,
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(final FacebookException exception) {
                Toast.makeText(
                        LoginActivity.this,
                        R.string.login_error,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(BetterUApplication.TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i(BetterUApplication.TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.i(BetterUApplication.TAG+"_USER", user.toString());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(BetterUApplication.TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

}
