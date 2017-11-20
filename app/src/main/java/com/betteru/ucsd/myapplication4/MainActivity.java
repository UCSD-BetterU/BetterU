package com.betteru.ucsd.myapplication4;

import android.media.FaceDetector;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.DefaultAudience;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import android.content.Intent;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "BetterU";

    private static final int LOGGED_OUT_HOME = 0;
    private static final int HOME = 1;
    private static final int FRAGMENT_COUNT = HOME +1;
    private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];
    private CallbackManager callbackManager;
    protected FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setUserAndFriends();
    }

    protected void setUserAndFriends() {
        if (((BetterUApplication) getApplication()).getCurrentFBUser() != null) {
            return;
        }
        FBGraphAPICall meCall = FBGraphAPICall.callMe("name,first_name", new FBGraphAPICallback() {
            @Override
            public void handleResponse(GraphResponse response) throws JSONException {
                JSONObject userObject = response.getJSONObject();
                String name = userObject.getString("name");
                String firstName = userObject.getString("first_name");
                String userId = userObject.getString("id");
                UserModel user = new UserModel(name, firstName, userId);
                Log.i(BetterUApplication.TAG+"_USER", user.toString());
                ((BetterUApplication) getApplication()).setCurrentFBUser(user);
            }

            @Override
            public void handleError(FacebookRequestError error) {
                ((BetterUApplication) getApplication()).showError(error.toString());
            }
        });

        GraphRequestBatch requestBatch = FBGraphAPICall.createRequestBatch(meCall);

        requestBatch.addCallback(new GraphRequestBatch.Callback() {
            @Override
            public void onBatchCompleted(GraphRequestBatch batch) {
                UserModel user = ((BetterUApplication) getApplication()).getCurrentFBUser();
                if (user != null) {
                    String userId = null;
                    userId = user.getUserId() ;
                    final CollectionReference userCollection = db.collection("Users");
                    DocumentReference friendRef = db.collection("friends").document(userId);
                    friendRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document != null) {
                                    Map<String, Object> friendListData = document.getData();
                                    Log.d(BetterUApplication.TAG, "DocumentSnapshot data: " + friendListData);
                                    for (Map.Entry<String, Object> friend : friendListData.entrySet()) {
                                        String friendId = friend.getKey();
                                        DocumentReference userRef = userCollection.document(friendId);
                                        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        Map<String, Object> friend = document.getData();
                                                        UserModel friendData = new UserModel(
                                                            (String) friend.get("name"),
                                                            (String) friend.get("firstName"),
                                                            (String) friend.get("userId"));
                                                        ((BetterUApplication) getApplication()).addToFriendList(friendData);
                                                    }
                                                }
                                            }
                                        });
                                    }
                                } else {
                                    Log.d(BetterUApplication.TAG, "No such document");
                                }
                            } else {
                                Log.d(BetterUApplication.TAG, "get failed with ", task.getException());
                            }
                        }
                    });
                }
            }
        });

        requestBatch.executeAsync();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        Class fragmentClass = BasicFragment.class;

        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            FacebookLogin.logout();
            Intent startupIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(startupIntent);
            finish();
            return true;
        }

        if (id == R.id.nav_goal) {
            fragmentClass = GoalFragment.class;
        } else if (id == R.id.nav_ranking) {
            fragmentClass = RankingFragment.class;
        } else if (id == R.id.nav_challenge) {
            fragmentClass = ChallengeFragment.class;
        } else if (id == R.id.nav_friends) {
            fragmentClass = FriendsFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragmentContent, fragment).addToBackStack(null).commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
