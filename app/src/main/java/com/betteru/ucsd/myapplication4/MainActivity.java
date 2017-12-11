package com.betteru.ucsd.myapplication4;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.ProfileTracker;
import com.facebook.login.widget.ProfilePictureView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
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
    private ProfileTracker profileTracker;
    protected FirebaseFirestore db = FirebaseFirestore.getInstance();
    protected BetterUApplication app;
    private Intent extrasensoryService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app = (BetterUApplication) getApplication();
        Log.d(BetterUApplication.TAG, "main activity!");
        Log.d(BetterUApplication.TAG+"LOGIN", String.valueOf(app.isLoggedIn()));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (app.getFriendList() != null) {
            for (int i = 0; i < app.getFriendList().size(); ++i) {
                Log.d(BetterUApplication.TAG + "main", app.getFriendList().get(i).getUserId() + " " + app.getFriendList().get(i).getName());
            }
        } else {
            Log.d(BetterUApplication.TAG + "main", "no friend list");
        }

        setUserAndFriends();

        setExtrasensoryService();
    }

    /**
     * Return the super-directory, where a users' ExtraSensory-App label files should be
     * @return The users' files' directory
     * @throws PackageManager.NameNotFoundException
     */
    private File getUsersFilesDirectory() throws PackageManager.NameNotFoundException {
        Log.d(BetterUApplication.TAG+"ESA", "getUsersFilesDirectory");
        // Locate the ESA saved files directory, and the specific minute-example's file:
        Context extraSensoryAppContext = getApplicationContext().createPackageContext("edu.ucsd.calab.extrasensory",0);
        File esaFilesDir = extraSensoryAppContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        Log.d(BetterUApplication.TAG+"ESA", esaFilesDir.getName());
        if (!esaFilesDir.exists()) {
            Log.d(BetterUApplication.TAG+"ESA", "esaFilesDir is null hohoho");
            return null;
        }
        return esaFilesDir;
    }

    protected void setUserAndFriends() {

        FBGraphAPICall meCall = FBGraphAPICall.callMe("name,first_name", new FBGraphAPICallback() {
            @Override
            public void handleResponse(GraphResponse response) throws JSONException {
                JSONObject userObject = response.getJSONObject();
                String name = userObject.getString("name");
                String firstName = userObject.getString("first_name");
                String userId = userObject.getString("id");
                UserModel user = new UserModel(name, firstName, userId);
                Log.i(BetterUApplication.TAG+"_USER", user.toString());
                BetterUApplication app = (BetterUApplication) getApplication();
                Log.i(BetterUApplication.TAG+"_USER", "user set" + name);
                app.setCurrentFBUser(user);
                Fragment fragment = null;
                try {
                    fragment = (Fragment) RRFragment.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                getFragmentManager().beginTransaction().replace(R.id.fragmentContent, fragment).addToBackStack(null).commit();
            }

            @Override
            public void handleError(FacebookRequestError error) {
                ((BetterUApplication) getApplication()).showError(error.toString());
            }
        });
        FBGraphAPICall myFriendsCall = FBGraphAPICall.callMeFriends("name,first_name", new FBGraphAPICallback() {
            @Override
            public void handleResponse(GraphResponse response) {
                JSONArray friendsData = FBGraphAPICall.getDataFromResponse(response);
                Log.i(BetterUApplication.TAG+"_FRIENDS", friendsData.toString());
                ArrayList<UserModel> friendList = new ArrayList<>();
                for (int i = 0; i < friendsData.length(); i++) {
                    try {
                        JSONObject userObject = friendsData.getJSONObject(i);
                        String name = userObject.getString("name");
                        String firstName = userObject.getString("first_name");
                        String userId = userObject.getString("id");
                        UserModel user = new UserModel(name, firstName, userId);
                        friendList.add(user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                BetterUApplication app = (BetterUApplication) getApplication();
                app.setFriendList(friendList);
                for (int i = 0; i < app.getFriendList().size(); ++i) {
                    Log.d(BetterUApplication.TAG + "FriendList", app.getFriendList().get(i).getUserId());
                }
            }

            @Override
            public void handleError(FacebookRequestError error) {
                ((BetterUApplication) getApplication()).showError(error.toString());
            }
        });

        // Create a RequestBatch and add a callback once the batch of requests completes
        GraphRequestBatch requestBatch = FBGraphAPICall.createRequestBatch(myFriendsCall, meCall);

        requestBatch.addCallback(new GraphRequestBatch.Callback() {
            @Override
            public void onBatchCompleted(GraphRequestBatch batch) {
                UserModel user = ((BetterUApplication) getApplication()).getCurrentFBUser();
                if (user != null) {
                    Map<String, Object> userMap = new HashMap<>();
                    String userId = user.getUserId();
                    String firstName = user.getFirstName();
                    String name = user.getName();
                    // Set profile pic
                    ProfilePictureView profilePicView = findViewById(R.id.userProfile);
                    profilePicView.setProfileId(userId);
                    profilePicView.setCropped(true);
                    // Set user name
                    TextView userNameView = findViewById(R.id.userName);
                    userNameView.setText(firstName);
                    userMap.put("first name", firstName);
                    userMap.put("name", name);
                    userMap.put("user id", userId);
                    CollectionReference userCollection = db.collection("Users");
                    userCollection.document(userId).set(userMap, SetOptions.merge());
                    ArrayList<UserModel> friendList = ((BetterUApplication) getApplication()).getFriendList();
                    if ( friendList != null ) {
                        Map<String, Integer> friendMap = new HashMap<>();
                        for (UserModel friend : friendList) {
                            String friendId = friend.getUserId();
                            friendMap.put(friendId, 1);
                        }
                        CollectionReference friendCollection = db.collection("friends");
                        friendCollection.document(userId).set(friendMap, SetOptions.merge());
                    }

                }
            }
        });

        requestBatch.executeAsync();
    }

    @SuppressLint("ShortAlarm")
    private void setExtrasensoryService() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 10);

        Intent extrasensoryIntent = new Intent(this, ExtrasensoryService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, extrasensoryIntent, 0);

        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //for 30 mint 60*60*1000
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() + 10000,
                60*60*1000, pendingIntent);

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

        /*if (id == R.id.nav_goal) {
            fragmentClass = GoalFragment.class;
        } else */
        if (id == R.id.nav_report) {
            fragmentClass = RRFragment.class;
        } else if (id == R.id.nav_challenge) {
            fragmentClass = ChallengePagerFragment.class;
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
