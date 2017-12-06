package com.betteru.ucsd.myapplication4;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RankingSettingsDialogFragment extends DialogFragment {
    FirebaseFirestore db;
    String userId = "user0001";

    /*private CheckBox exercisingCheckbox;
    private boolean exercisingRanking;
    private CheckBox cookingCheckbox;
    private boolean cookingRanking;
    private CheckBox houseworkCheckbox;
    private boolean houseworkRanking;
    private CheckBox shoppingCheckbox;
    private boolean shoppingRanking;
    private CheckBox sleepingCheckbox;
    private boolean sleepingRanking;
    private CheckBox workingCheckbox;
    private boolean workingRanking;
    private CheckBox studyingCheckbox;
    private boolean studyingRanking;
    private CheckBox hangingoutCheckbox;
    private boolean hangingoutRanking;*/

    private CheckBox[] boxes = new CheckBox[51];
    private boolean[] preferences = new boolean[51];
    private HashMap<Integer, Boolean> changedPreferences = new HashMap<Integer, Boolean>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        return inflater.inflate(R.layout.fragment_ranking_settings_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();

        boxes[0] = (CheckBox) getView().findViewById(R.id.checkBoxLyingDown);
        boxes[1] = (CheckBox) getView().findViewById(R.id.checkBoxSitting);
        boxes[2] = (CheckBox) getView().findViewById(R.id.checkBoxStanding);
        boxes[3] = (CheckBox) getView().findViewById(R.id.checkBoxWalking);
        boxes[4] = (CheckBox) getView().findViewById(R.id.checkBoxRunning);
        boxes[5] = (CheckBox) getView().findViewById(R.id.checkBoxBicycling);
        boxes[6] = (CheckBox) getView().findViewById(R.id.checkBoxSleeping);
        boxes[7] = (CheckBox) getView().findViewById(R.id.checkBoxLabWork);
        boxes[8] = (CheckBox) getView().findViewById(R.id.checkBoxInClass);
        boxes[9] = (CheckBox) getView().findViewById(R.id.checkBoxInAMeeting);
        boxes[10] = (CheckBox) getView().findViewById(R.id.checkBoxAtWork);
        boxes[11] = (CheckBox) getView().findViewById(R.id.checkBoxIndoors);
        boxes[12] = (CheckBox) getView().findViewById(R.id.checkBoxOutside);
        boxes[13] = (CheckBox) getView().findViewById(R.id.checkBoxInACar);
        boxes[14] = (CheckBox) getView().findViewById(R.id.checkBoxOnABus);
        boxes[15] = (CheckBox) getView().findViewById(R.id.checkBoxDriver);
        boxes[16] = (CheckBox) getView().findViewById(R.id.checkBoxPassenger);
        boxes[17] = (CheckBox) getView().findViewById(R.id.checkBoxAtHome);
        boxes[18] = (CheckBox) getView().findViewById(R.id.checkBoxAtSchool);
        boxes[19] = (CheckBox) getView().findViewById(R.id.checkBoxAtARestaurant);
        boxes[20] = (CheckBox) getView().findViewById(R.id.checkBoxExercising);
        boxes[21] = (CheckBox) getView().findViewById(R.id.checkBoxCooking);
        boxes[22] = (CheckBox) getView().findViewById(R.id.checkBoxShopping);
        boxes[23] = (CheckBox) getView().findViewById(R.id.checkBoxStrolling);
        boxes[24] = (CheckBox) getView().findViewById(R.id.checkBoxDrinking);
        boxes[25] = (CheckBox) getView().findViewById(R.id.checkBoxBathing);
        boxes[26] = (CheckBox) getView().findViewById(R.id.checkBoxCleaning);
        boxes[27] = (CheckBox) getView().findViewById(R.id.checkBoxDoingLaundry);
        boxes[28] = (CheckBox) getView().findViewById(R.id.checkBoxWashingDishes);
        boxes[29] = (CheckBox) getView().findViewById(R.id.checkBoxWatchingTV);
        boxes[30] = (CheckBox) getView().findViewById(R.id.checkBoxSurfingTheInternet);
        boxes[31] = (CheckBox) getView().findViewById(R.id.checkBoxAtAParty);
        boxes[32] = (CheckBox) getView().findViewById(R.id.checkBoxAtABar);
        boxes[33] = (CheckBox) getView().findViewById(R.id.checkBoxAtTheBeach);
        boxes[34] = (CheckBox) getView().findViewById(R.id.checkBoxSinging);
        boxes[35] = (CheckBox) getView().findViewById(R.id.checkBoxTalking);
        boxes[36] = (CheckBox) getView().findViewById(R.id.checkBoxComputerWork);
        boxes[37] = (CheckBox) getView().findViewById(R.id.checkBoxEating);
        boxes[38] = (CheckBox) getView().findViewById(R.id.checkBoxToilet);
        boxes[39] = (CheckBox) getView().findViewById(R.id.checkBoxGrooming);
        boxes[40] = (CheckBox) getView().findViewById(R.id.checkBoxDressing);
        boxes[41] = (CheckBox) getView().findViewById(R.id.checkBoxAtTheGym);
        boxes[42] = (CheckBox) getView().findViewById(R.id.checkBoxStairsUp);
        boxes[43] = (CheckBox) getView().findViewById(R.id.checkBoxStairsDown);
        boxes[44] = (CheckBox) getView().findViewById(R.id.checkBoxElevator);
        boxes[45] = (CheckBox) getView().findViewById(R.id.checkBoxPhoneInPocket);
        boxes[46] = (CheckBox) getView().findViewById(R.id.checkBoxPhoneInHand);
        boxes[47] = (CheckBox) getView().findViewById(R.id.checkBoxPhoneInBag);
        boxes[48] = (CheckBox) getView().findViewById(R.id.checkBoxPhoneOnTable);
        boxes[49] = (CheckBox) getView().findViewById(R.id.checkBoxWithCoworker);
        boxes[50] = (CheckBox) getView().findViewById(R.id.checkBoxWithFriends);

        //Arrays.fill(preferences, true);
        loadPreferences(userId);

        /*exercisingRanking = getArguments().getBoolean("exercisingRanking", false);
        cookingRanking = getArguments().getBoolean("cookingRanking", false);
        houseworkRanking = getArguments().getBoolean("houseworkRanking", false);
        shoppingRanking = getArguments().getBoolean("shoppingRanking", false);
        sleepingRanking = getArguments().getBoolean("sleepingRanking", false);
        workingRanking = getArguments().getBoolean("workingRanking", false);
        studyingRanking = getArguments().getBoolean("studyingRanking", false);
        hangingoutRanking = getArguments().getBoolean("hangingoutRanking", false);

        exercisingCheckbox = (CheckBox) getView().findViewById(R.id.ranking_checkbox_exercising);
        exercisingCheckbox.setChecked(exercisingRanking);
        exercisingCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                exercisingRanking = isChecked;
                Log.i("DYQQQ","exercising checked: "+Boolean.toString(isChecked));
            }}
        );
        cookingCheckbox = (CheckBox) getView().findViewById(R.id.ranking_checkbox_cooking);
        cookingCheckbox.setChecked(cookingRanking);
        cookingCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                cookingRanking = isChecked;
            }}
        );
        houseworkCheckbox = (CheckBox) getView().findViewById(R.id.ranking_checkbox_housework);
        houseworkCheckbox.setChecked(houseworkRanking);
        houseworkCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                houseworkRanking = isChecked;
            }}
        );
        shoppingCheckbox = (CheckBox) getView().findViewById(R.id.ranking_checkbox_shopping);
        shoppingCheckbox.setChecked(shoppingRanking);
        shoppingCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                shoppingRanking = isChecked;
            }}
        );
        sleepingCheckbox = (CheckBox) getView().findViewById(R.id.ranking_checkbox_sleeping);
        sleepingCheckbox.setChecked(sleepingRanking);
        sleepingCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                sleepingRanking = isChecked;
            }}
        );
        workingCheckbox = (CheckBox) getView().findViewById(R.id.ranking_checkbox_working);
        workingCheckbox.setChecked(workingRanking);
        workingCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                workingRanking = isChecked;
            }}
        );
        studyingCheckbox = (CheckBox) getView().findViewById(R.id.ranking_checkbox_studying);
        studyingCheckbox.setChecked(studyingRanking);
        studyingCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                studyingRanking = isChecked;
            }}
        );
        hangingoutCheckbox = (CheckBox) getView().findViewById(R.id.ranking_checkbox_hangingout);
        hangingoutCheckbox.setChecked(hangingoutRanking);
        hangingoutCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                hangingoutRanking = isChecked;
            }}
        );*/

        Button saveSettings = (Button) getView().findViewById(R.id.ranking_button_save_settings);
        saveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> childUpdates = new HashMap<>();
                for (HashMap.Entry<Integer, Boolean> entry: changedPreferences.entrySet()) {
                    Integer key = entry.getKey();
                    Boolean value = entry.getValue();
                    if (value != preferences[key]) {
                        childUpdates.put(Integer.toString(key),value);
                    }
                }
                db.collection("ranking_preferences").document(userId).update(childUpdates);
                /*Intent intent = new Intent("DialogChangeSaved");
                intent.putExtra("exercisingRanking", exercisingRanking);
                intent.putExtra("cookingRanking", cookingRanking);
                intent.putExtra("houseworkRanking", houseworkRanking);
                intent.putExtra("shoppingRanking", shoppingRanking);
                intent.putExtra("sleepingRanking", sleepingRanking);
                intent.putExtra("workingRanking", workingRanking);
                intent.putExtra("studyingRanking", studyingRanking);
                intent.putExtra("hangingoutRanking", hangingoutRanking);
                getActivity().sendBroadcast(intent);*/
                getDialog().dismiss();
            }
        });
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    /*public static RankingSettingsDialogFragment newInstance(
            boolean exercisingRanking,
            boolean cookingRanking,
            boolean houseworkRanking,
            boolean shoppingRanking,
            boolean sleepingRanking,
            boolean workingRanking,
            boolean studyingRanking,
            boolean hangingoutRanking
    ) {
        RankingSettingsDialogFragment fragment = new RankingSettingsDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean("exercisingRanking", exercisingRanking);
        args.putBoolean("cookingRanking", cookingRanking);
        args.putBoolean("houseworkRanking", houseworkRanking);
        args.putBoolean("shoppingRanking", shoppingRanking);
        args.putBoolean("sleepingRanking", sleepingRanking);
        args.putBoolean("workingRanking", workingRanking);
        args.putBoolean("studyingRanking", studyingRanking);
        args.putBoolean("hangingoutRanking", hangingoutRanking);
        fragment.setArguments(args);
        return fragment;
    }*/

    private void loadPreferences(String userId) {
        DocumentReference dbData;
        try {
            dbData = db.collection("ranking_preferences").document(userId);
        }catch (Exception e)
        {
            Log.d("Exception", e.toString());
            return;
        }
        dbData.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task){
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document != null){
                        if(!document.exists()){
                            Log.d("Data in Cloud","no preferences");
                        } else {
                            Map<String, Object> obj = document.getData();
                            for (String key : obj.keySet()) {
                                int k = Integer.valueOf(key);
                                preferences[k] = document.getBoolean(key);
                            }
                            for (int i = 0; i < 51; i++) {
                                final int k = i;
                                boxes[i].setChecked(preferences[i]);
                                boxes[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                        changedPreferences.put(k, b);
                                    }
                                });
                            }
                        }
                    }
                }else{
                    Log.d("Data in Cloud", "Error getting documents" , task.getException());
                }
            }
        });
    }
}
