package com.betteru.ucsd.myapplication4;

/**
 * Created by verazou on 11/12/17.
 */

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.betteru.ucsd.myapplication4.MainActivity;
import com.betteru.ucsd.myapplication4.LoginActivity;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

public class FacebookLogin {

    /**
     * HomeActivity is the activity handling Facebook Login in the app. Needed here to send
     * the signal back when user successfully logged in.
     */
    private MainActivity activity;

    /**
     * CallbackManager is a Facebook SDK class managing the callbacks into the FacebookSdk from
     * an Activity's or Fragment's onActivityResult() method.
     * For more information see
     * https://developers.facebook.com/docs/reference/android/current/interface/CallbackManager/
     */
    private CallbackManager callbackManager;

    /**
     * CallbackManager is exposed here to so that onActivityResult() can be called from Activities
     * and Fragments when required. This is necessary so that the login result is passed to the
     * LoginManager
     */
    public CallbackManager getCallbackManager() { return callbackManager; }

    /**
     * AccessTokenTracker allows for tracking whenever the access token changes - whenever user logs
     * in, logs out etc abstract method onCurrentAccessTokenChanged is called.
     */
    private AccessTokenTracker tokenTracker;

    public FacebookLogin(MainActivity activity) {
        super();
        this.activity = activity;
    }

    /**
     * Needs to be called after Facebook SDK has been initialized with a FacebookSdk.sdkInitialize()
     * It overrides a method in AccessTokenTracker to get notified whenever AccessToken is
     * changed, which typically means user logged in, logged out or granted new permissions.
     * For more information see
     * https://developers.facebook.com/docs/reference/android/current/class/AccessTokenTracker/
     * https://developers.facebook.com/docs/reference/android/current/interface/FacebookCallback/
     */
    public void init() {
        callbackManager = CallbackManager.Factory.create();
        tokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
     //           onLoginStateChanged(oldAccessToken, currentAccessToken);
            }
        };
    }

    /**
     * Called when MainActivity resumes. Ensures tokenTracker tracks token changes
     */
    public void activate() {
        tokenTracker.startTracking();
    }

    /**
     * Called when MainActivity is paused. Ensures tokenTracker stops tracking
     */
    public void deactivate() {
        tokenTracker.stopTracking();
    }

    public static void logout() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
    }


    /**
     * LoginButton can be used to trigger the login dialog asking for any permission so it is
     * important to specify which permissions you want to request from a user. In BetterU case
     * only user_friends is required to enable access to friends.
     * For more info on permissions see
     * https://developers.facebook.com/docs/facebook-login/android/permissions
     * This method is called from onCreateView() of a Fragment displayed when user is logged out of
     * Facebook.
     */
    public void setUpLoginButton(LoginButton button) {
        button.setReadPermissions(FBPermission.USER_FRIENDS.toString());
        button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.w("DEBUG", "onLoginButtonSuccess");
   //             onLoginStateChanged(null, AccessToken.getCurrentAccessToken());
            }

            @Override
            public void onCancel() {
                Log.w("DEBUG", "on Login Cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e(MainActivity.TAG, error.toString());
            }
        });
    }

    /**
     * Uses LoginManager to request additional permissions when needed
     * See https://developers.facebook.com/docs/facebook-login/android/permissions for more info on
     * Login permissions
     */
    public void requestPermission (FBPermission permission) {
        if (!isPermissionGranted(permission)) {
            Collection<String> permissions = new ArrayList<String>(1);
            permissions.add(permission.toString());
            if (permission.isRead()) {
                LoginManager.getInstance().logInWithReadPermissions(activity, permissions);
            } else {
                LoginManager.getInstance().logInWithPublishPermissions(activity, permissions);
            }
        }
    }

    /**
     * Helper function checking if user is logged in and access token hasn't expired.
     */
    public static boolean isAccessTokenValid() {
        return testAccessTokenValid(AccessToken.getCurrentAccessToken());
    }

    /**
     * Helper function checking if user has granted particular permission to the app
     * For more info on permissions see
     * https://developers.facebook.com/docs/facebook-login/android/permissions
     */
    public static boolean isPermissionGranted(FBPermission permission) {
        return testTokenHasPermission(AccessToken.getCurrentAccessToken(), permission);
    }

    /**
     * Helper function checking if the given access token is valid
     */
    public static boolean testAccessTokenValid(AccessToken token) {
        return token != null && !token.isExpired();
    }

    /**
     * Helper function checking if the given access token includes specified login permission
     */
    public static boolean testTokenHasPermission(AccessToken token, FBPermission permission) {
        return testAccessTokenValid(token) && token.getPermissions().contains(permission.toString());
    }
/*
    private void fetchUserInformationAndLogin() {
        if (FacebookLogin.isAccessTokenValid()) {
            if (fragments[FB_LOGGED_OUT_HOME] != null &&
                    ((FBLoggedOutHomeFragment)fragments[FB_LOGGED_OUT_HOME]).progressContainer != null) {
                ((FBLoggedOutHomeFragment)fragments[FB_LOGGED_OUT_HOME]).progressContainer.setVisibility(View.VISIBLE);
            }

            GraphAPICall myFriendsCall = GraphAPICall.callMeFriends("name,first_name", new GraphAPICallback() {
                @Override
                public void handleResponse(GraphResponse response) {
                    JSONArray friendsData = GraphAPICall.getDataFromResponse(response);
                    ((FriendSmashApplication) getApplication()).setFriends(friendsData);
                }

                @Override
                public void handleError(FacebookRequestError error) {
                    showError(error.toString());
                }
            });

            GraphAPICall meCall = GraphAPICall.callMe("first_name", new GraphAPICallback() {
                @Override
                public void handleResponse(GraphResponse response) {
                    JSONObject user = response.getJSONObject();
                    ((FriendSmashApplication) getApplication()).setCurrentFBUser(user);
                    //saveUserToParse();
                    // it causes strange beaviour with AccessTokenTracker
                }

                @Override
                public void handleError(FacebookRequestError error) {
                    showError(error.toString());
                }
            });

            GraphAPICall meScoresCall = GraphAPICall.callMeScores(new GraphAPICallback() {
                @Override
                public void handleResponse(GraphResponse response) {
                    JSONObject data = GraphAPICall.getDataFromResponse(response).optJSONObject(0);
                    if (data != null) {
                        int score = data.optInt("score");
                        ((FriendSmashApplication) getApplication()).setTopScore(score);
                    }
                }

                @Override
                public void handleError(FacebookRequestError error) {
                    showError(error.toString());
                }
            });

            // Create a RequestBatch and add a callback once the batch of requests completes
            GraphRequestBatch requestBatch = GraphAPICall.createRequestBatch(myFriendsCall, meCall, meScoresCall);

            requestBatch.addCallback(new GraphRequestBatch.Callback() {
                @Override
                public void onBatchCompleted(GraphRequestBatch batch) {
                    if (((FriendSmashApplication)getApplication()).getCurrentFBUser() != null) {
                        loadPersonalizedFragment();
                    } else {
                        showError(getString(R.string.error_fetching_profile));
                    }
                }
            });

            requestBatch.executeAsync();
        }
    }
    */
}
