package com.betteru.ucsd.myapplication4;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ExtrasensoryService extends IntentService {

    private String _uuidPrefix = null;
    private static final String NO_USER = "no user";
    private static final String NO_TIMESTAMP = "no timestamp";
    private String previousTimestamp = null;
    private String currentTimestamp = null;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //For testing
    private UserModel currentUser = new UserModel("Zhaowen Zou", "Zhaowen", "100000000");

    public ExtrasensoryService() {
        super("ExtrasensoryService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        Log.d(BetterUApplication.TAG+"ESA", "Extrasensory intent started! " + currentDateTimeString );

        UserModel currentFBUser = ((BetterUApplication) getApplication()).getCurrentFBUser();
        if (currentFBUser != null) {
            currentUser = currentFBUser;
        }

        if (intent == null) return;

        if (!getUser()) return;


        performAction();

    }

    private void performAction() {
        final DocumentReference docRef = db.collection("extrasensory").document(currentUser.getUserId());
        final List<String> timestamps = new ArrayList<>();
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> dataMap = document.getData();
                        previousTimestamp = (String) dataMap.get("current_timestamp");
                        getAndUpdateLabelAndTime();
                    } else {
                        Log.d(BetterUApplication.TAG+"ESAdoc", "Document doesn't exist");
                        Map<String, String> currentTimestampMap = new HashMap<>();
                        currentTimestampMap.put("current_timestamp", "");
                        db.collection("extrasensory").document(currentUser.getUserId()).set(currentTimestampMap, SetOptions.merge());
                        previousTimestamp = "";
                        getAndUpdateLabelAndTime();
                    }
                } else {
                    Log.d(BetterUApplication.TAG+"ESAtask", "task failed");
                }
            }
        });
    }

    private void getAndUpdateLabelAndTime() {
        List<String> timestamps = getTimestampsForUser(_uuidPrefix);
        if (timestamps == null || timestamps.size() == 0)   return;
        Map<String, List<Pair<String, Double>>> records = new HashMap<>();
        boolean foundEmptyLabel = false;
        for (String timestamp : timestamps) {
            List<Pair<String, Double>> labelsAndTime = getSpecificTimestampContent(timestamp);
            if (labelsAndTime.size() == 0 && !foundEmptyLabel) {
                foundEmptyLabel = true;
                currentTimestamp = timestamp;
                break;
            }
            records.put(timestamp, labelsAndTime);
        }
        if (!foundEmptyLabel) {
            if (timestamps.size() > 0) {
                currentTimestamp = String.valueOf(Integer.parseInt(timestamps.get(timestamps.size() - 1)) + 1);
            } else {
                currentTimestamp = previousTimestamp;
            }
        }
        Log.d(BetterUApplication.TAG+"ESAcurrent", currentTimestamp);
        Log.d(BetterUApplication.TAG+"ESAfinish", "finished");
        updateRecords(records);
    }



    private boolean getUser() {
        Log.d(BetterUApplication.TAG+"ESA", "Getting user!");

        boolean haveUsers = true;
        List<String> uuidPrefixes = getAllUsers();
        // first check if there are any users at all:
        if ((uuidPrefixes == null) || (uuidPrefixes.isEmpty())) {
            uuidPrefixes = new ArrayList<>();
            uuidPrefixes.add(NO_USER);
            return false;
        }

        _uuidPrefix = uuidPrefixes.get(0);
        Log.d(BetterUApplication.TAG+"ESA","id: "+_uuidPrefix);

        return haveUsers;
    }

    /**
     * Get the list of users (UUID prefixes).
     * @return List of UUID prefixes (strings).
     * In case the user's directory was not found, null will be returned.
     */
    private List<String> getAllUsers() {
        Log.d(BetterUApplication.TAG+"ESA", "Getting all users!");
        try {
            File esaFilesDir = getUsersFilesDirectory();
            if (esaFilesDir == null) {
                Log.d(BetterUApplication.TAG+"ESA", "esaFilesDir is null");
                return null;
            }

            String[] filenames = esaFilesDir.list(new FilenameFilter() {
                @Override
                public boolean accept(File file, String s) {
                    return s.startsWith(UUID_DIR_PREFIX);
                }
            });

            for (String name : filenames) {
                Log.d(BetterUApplication.TAG+"ESA", name);
            }

            SortedSet<String> usersSet = new TreeSet<>();
            for (String filename : filenames) {
                String uuidPrefix = filename.replace(UUID_DIR_PREFIX,"");
                usersSet.add(uuidPrefix);
            }

            List<String> uuidPrefixes = new ArrayList<>(usersSet);
            return uuidPrefixes;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Get the list of timestamps, for which this user has saved files from ExtraSensory App.
     * @param uuidPrefix The prefix (8 characters) of the user's UUID
     * @return List of timestamps (strings), each representing a minute that has a file for this user.
     * The list will be sorted from earliest to latest.
     * In case the user's directory was not found, null will be returned.
     */
    private List<String> getTimestampsForUser(String uuidPrefix) {
        Log.d(BetterUApplication.TAG+"ESA", "Getting timestamp for user " + uuidPrefix);
        try {
            File esaFilesDir = getUserFilesDirectory(uuidPrefix);
            if (esaFilesDir == null) {
                return new ArrayList<>();
            }

            String[] filenames = esaFilesDir.list(new FilenameFilter() {
                @Override
                public boolean accept(File file, String s) {
//                    return s.endsWith(SERVER_PREDICTIONS_FILE_SUFFIX) || s.endsWith(USER_REPORTED_LABELS_FILE_SUFFIX);
                    return s.endsWith(SERVER_PREDICTIONS_FILE_SUFFIX);
                }
            });

            SortedSet<String> userTimestampsSet = new TreeSet<>();
            for (String filename : filenames) {
                String timestamp = filename.substring(0,10); // The timestamps always occupy 10 characters
                userTimestampsSet.add(timestamp);
            }

            if (!userTimestampsSet.isEmpty()) {
                if (previousTimestamp != null && previousTimestamp != "") {
                    userTimestampsSet = userTimestampsSet.tailSet(previousTimestamp);
                }
            }
            List<String> timestamps = new ArrayList<>(userTimestampsSet);
            return timestamps;
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

    }

    private static final String SERVER_PREDICTIONS_FILE_SUFFIX = ".server_predictions.json";
    private static final String USER_REPORTED_LABELS_FILE_SUFFIX = ".user_reported_labels.json";
    private static final String UUID_DIR_PREFIX = "extrasensory.labels.";

    /**
     * Return the super-directory, where a users' ExtraSensory-App label files should be
     * @return The users' files' directory
     * @throws PackageManager.NameNotFoundException
     */
    private File getUsersFilesDirectory() throws PackageManager.NameNotFoundException {
        // Locate the ESA saved files directory, and the specific minute-example's file:
        Context extraSensoryAppContext = getApplicationContext().createPackageContext("edu.ucsd.calab.extrasensory",0);
        File esaFilesDir = extraSensoryAppContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
    //    Log.d(BetterUApplication.TAG+"ESA", esaFilesDir.getName());
        if (!esaFilesDir.exists()) {
            Log.d(BetterUApplication.TAG+"ESA", "esaFilesDir is null hohoho");
            return null;
        }
        return esaFilesDir;
    }

    /**
     * Return the directory, where a user's ExtraSensory-App label files should be
     * @param uuidPrefix The prefix (8 characters) of the user's UUID
     * @return The user's files' directory
     * @throws PackageManager.NameNotFoundException
     */
    private File getUserFilesDirectory(String uuidPrefix) throws PackageManager.NameNotFoundException {
        // Locate the ESA saved files directory, and the specific minute-example's file:
        Context extraSensoryAppContext = getApplicationContext().createPackageContext("edu.ucsd.calab.extrasensory",0);
        File esaFilesDir = new File(extraSensoryAppContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),UUID_DIR_PREFIX + uuidPrefix);
        if (!esaFilesDir.exists()) {
            return null;
        }
        return esaFilesDir;
    }

    private List<Pair<String, Double>> getSpecificTimestampContent(String timestamp) {

        //Log.d(BetterUApplication.TAG+"ESAtime", timestamp);

        if (_uuidPrefix == null || _uuidPrefix == NO_USER) {
            Log.d(BetterUApplication.TAG+"ESA", "no _uuidPrefix");
            return null;
        }
        else if (timestamp == null || timestamp == NO_TIMESTAMP) {
            Log.d(BetterUApplication.TAG+"ESA", "no timestamp");
            return null;
        }

        String fileContent = readESALabelsFileForMinute(_uuidPrefix, timestamp, true);
        List<Pair<String, Double>> labelsAndProbs = parseServerPredictionLabelProbabilities(fileContent);

        String pairsStr = labelsAndProbs.size() + " labels:\n";
        for (Pair pair : labelsAndProbs) {
            //   Log.d(BetterUApplication.TAG+"ESAdata", pair.first + " " + pair.second);
            pairsStr += pair.first + ": " + pair.second + "\n";
        }

        String textToPresent =
                "Timestamp: " + timestamp + "\n\n" +
                        "Server predictions:\n" + pairsStr + "\n" + "-------------\n";
        //Log.d(BetterUApplication.TAG+"ESA", textToPresent);
        return labelsAndProbs;

    }


    /**
     * Read text from the label file saved by ExtraSensory App, for a particualr minute-example.
     * @param uuidPrefix The prefix (8 characters) of the user's UUID
     * @param timestamp The timestamp of the desired minute example
     * @param serverOrUser Read the server-predictions if true, and the user-reported labels if false
     * @return The text inside the file, or null if had trouble finding or reading the file
     */
    private String readESALabelsFileForMinute(String uuidPrefix, String timestamp, boolean serverOrUser) {
        try {
            File esaFilesDir = getUserFilesDirectory(uuidPrefix);
            if (esaFilesDir == null) {
                // Cannot find the directory where the label files should be
                return null;
            }
            String fileSuffix = serverOrUser ? SERVER_PREDICTIONS_FILE_SUFFIX : USER_REPORTED_LABELS_FILE_SUFFIX;
            //Log.d(BetterUApplication.TAG+"ESAfile", timestamp + " " + fileSuffix);
            File minuteLabelsFile = new File(esaFilesDir,timestamp + fileSuffix);

            // Check if file exists:
            if (!minuteLabelsFile.exists()) {
                Log.d(BetterUApplication.TAG+"ESAmin", "doesn't have min file");
                return null;
            }

            // Read the file:
            StringBuilder text = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(minuteLabelsFile));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            //Log.d(BetterUApplication.TAG+"ESAdata", text.toString());
            bufferedReader.close() ;
            return text.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static final String JSON_FIELD_LABEL_NAMES = "label_names";
    private static final String JSON_FIELD_LABEL_PROBABILITIES = "label_probs";
    private static final String JSON_FIELD_LOCATION_COORDINATES = "location_lat_long";

    /**
     * Prse the content of a minute's server-prediction file to extract the labels and probabilities assigned to the labels.
     * @param predictionFileContent The content of a specific minute server-prediction file
     * @return List of label name and probability pairs, or null if had trouble.
     */
    private List<Pair<String,Double>> parseServerPredictionLabelProbabilities(String predictionFileContent) {
        if (predictionFileContent == null) {
            return null;
        }
        try {
            JSONObject jsonObject = new JSONObject(predictionFileContent);
            JSONArray labelArray = jsonObject.getJSONArray(JSON_FIELD_LABEL_NAMES);
            JSONArray probArray = jsonObject.getJSONArray(JSON_FIELD_LABEL_PROBABILITIES);
            // Make sure both arrays have the same size:
            if (labelArray == null || probArray == null || labelArray.length() != probArray.length()) {
                return null;
            }
            List<Pair<String,Double>> labelsAndProbabilities = new ArrayList<>(labelArray.length());
            for (int i = 0; i < labelArray.length(); i ++) {
                String label = labelArray.getString(i);
                Double prob = probArray.getDouble(i);
                labelsAndProbabilities.add(new Pair<String, Double>(label,prob));
            }
            return labelsAndProbabilities;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void updateRecords(final Map<String, List<Pair<String, Double>>> records) {
        if (previousTimestamp != null && previousTimestamp != "") {
            final Pair<String, String> previousDate = parseDate(previousTimestamp);
            DocumentReference userRef = db.collection("extrasensory").document(currentUser.getUserId());
            DocumentReference docRef = userRef.collection(previousDate.first).document(previousDate.second);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> labelData = document.getData();
                        Map<String, Long> previousRecord = new HashMap<>();
                        Log.d(BetterUApplication.TAG, "DocumentSnapshot data: " + labelData);
                        for (Map.Entry<String, Object> labelTime : labelData.entrySet()) {
                            String label = labelTime.getKey();
                            Long time = (long) labelTime.getValue();
                            previousRecord.put(label, time);
                            //Log.d(BetterUApplication.TAG + "ESAdownload", label + " " + String.valueOf(time));
                        }
                        Map<String, Map<String, Long>> timeOfLabels = new HashMap<>();
                        timeOfLabels.put(previousDate.first+previousDate.second, previousRecord);
                        uploadRecords(records, timeOfLabels);
                    } else {
                        Log.d(BetterUApplication.TAG, "No such document");
                    }
                } else {
                    Log.d(BetterUApplication.TAG, "get failed with ", task.getException());
                }
                }
            });
        } else {
            Map<String, Map<String, Long>> timeOfLabels = new HashMap<>();
            uploadRecords(records, timeOfLabels);
        }

    }

    private void uploadRecords(Map<String, List<Pair<String, Double>>> records, Map<String, Map<String, Long>> timeOfLabels) {
        timeOfLabels = addToTimeOfLabels(records, timeOfLabels);
        for (Map.Entry<String, Map<String, Long>> a : timeOfLabels.entrySet()) {
            //Log.d(BetterUApplication.TAG+"ESAtimedate", a.getKey());
            for (Map.Entry<String, Long> b : a.getValue().entrySet()) {
                //    Log.d(BetterUApplication.TAG+"ESAlabel", b.getKey());
                //    Log.d(BetterUApplication.TAG+"ESAlabel", String.valueOf(b.getValue()));
            }
        }
        DocumentReference userRef = db.collection("extrasensory").document(currentUser.getUserId());
        Map<String, String> currentTimestampMap = new HashMap<>();
        currentTimestampMap.put("current_timestamp", currentTimestamp);
        userRef.set(currentTimestampMap, SetOptions.merge());
        for (Map.Entry<String, Map<String, Long>> labelTime : timeOfLabels.entrySet()) {
            String yearMonth = labelTime.getKey().substring(0, 6);
            String day = labelTime.getKey().substring(6);
            DocumentReference docRef = userRef.collection(yearMonth).document(day);
            docRef.set(labelTime.getValue(), SetOptions.merge());
        }
    }

    private Pair<String, String> parseDate(String timestamp) {
        Date date = new Date(Long.parseLong(timestamp) * 1000L);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getDefault());
        String formatted = format.format(date);
        String yearMonth = formatted.substring(0, 4) + formatted.substring(5, 7);
        String day = formatted.substring(8, 10);
    //    Log.d(BetterUApplication.TAG+"yearmonth", yearMonth);
    //    Log.d(BetterUApplication.TAG+"day", day);
        return new Pair<>(yearMonth, day);
    }

    private Map<String, Map<String, Long>> addToTimeOfLabels(Map<String, List<Pair<String, Double>>> records, Map<String, Map<String, Long>> timeOfLabels) {
        for (Map.Entry<String, List<Pair<String, Double>>> record : records.entrySet()) {
            Pair<String, String> parsedDate = parseDate(record.getKey());
            String parsedDateKey = parsedDate.first + parsedDate.second;
            if (!timeOfLabels.containsKey(parsedDateKey)) {
                timeOfLabels.put(parsedDateKey, new TreeMap<String, Long>());
            }
            Map<String, Long> timeMap = timeOfLabels.get(parsedDateKey);
            for (Pair<String, Double> labelTime : record.getValue()) {
                String label = labelTime.first;
                Double percent = labelTime.second;
                if (percent > 0.5) {
                    Long curTime = timeMap.containsKey(label) ? timeMap.get(label) : 0;
                    timeMap.put(label, curTime + 1);
                }
            }
        }
        return timeOfLabels;
    }

}
