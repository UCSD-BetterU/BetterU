package com.betteru.ucsd.myapplication4;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class RankingSettingsDialogFragment extends DialogFragment {

    private CheckBox exercisingCheckbox;
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
    private boolean hangingoutRanking;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        return inflater.inflate(R.layout.fragment_ranking_settings_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        exercisingRanking = getArguments().getBoolean("exercisingRanking", false);
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
        );

        Button saveSettings = (Button) getView().findViewById(R.id.ranking_button_save_settings);
        saveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("DialogChangeSaved");
                intent.putExtra("exercisingRanking", exercisingRanking);
                intent.putExtra("cookingRanking", cookingRanking);
                intent.putExtra("houseworkRanking", houseworkRanking);
                intent.putExtra("shoppingRanking", shoppingRanking);
                intent.putExtra("sleepingRanking", sleepingRanking);
                intent.putExtra("workingRanking", workingRanking);
                intent.putExtra("studyingRanking", studyingRanking);
                intent.putExtra("hangingoutRanking", hangingoutRanking);
                getActivity().sendBroadcast(intent);
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

    public static RankingSettingsDialogFragment newInstance(
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
    }
}
