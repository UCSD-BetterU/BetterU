package com.betteru.ucsd.myapplication4;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.time.LocalDate;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChallengeActivityFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    static ChallengeModel data;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle args = getArguments();
        data = (ChallengeModel) args.getSerializable("data");
        Log.d("DATA", Integer.toString(data.activitiesIcon.size()));
        view = inflater.inflate(R.layout.fragment_challenge_detail, container, false);
        loadChallengeName();
        loadChallengeDate();
        loadChallengeParticipants();
        loadChallengeActivities();
        loadChallengeDateButton();
        loadChallengeNameButton();
        return view;
    }
    public void loadChallengeName() {
        TextView name = (TextView) view.findViewById(R.id.textView_challengeName);
        name.setText(data.title);
    }
    public void loadChallengeDate(){
        TextView date = (TextView) view.findViewById(R.id.textView_challengeDate);
        date.setText(data.date.format(data.formatter));
    }
    public void loadChallengeParticipants() {
        //set GridView
        GridView gridViewParticipants = (GridView) view.findViewById(R.id.gridview_challenge_participants);
        ChallengeActivityAdapter adapterPar = new ChallengeActivityAdapter(this.getActivity(), data.participants, data.participantsIcon);
        gridViewParticipants.setAdapter(adapterPar);
    }
    public void loadChallengeActivities(){
        //set GridView
        GridView gridViewActivities = (GridView) view.findViewById(R.id.gridView_challenge_activities);
        ChallengeActivityAdapter adapterAct =new ChallengeActivityAdapter(this.getActivity(), data.activities, data.activitiesIcon);
        gridViewActivities.setAdapter(adapterAct);
    }

    private void loadChallengeDateButton(){
        ImageButton button = (ImageButton) view.findViewById(R.id.imageButton_challengeDate);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //showDialog();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        ChallengeActivityFragment.this,
                        data.date.getYear(),
                        data.date.getMonthValue()-1,
                        data.date.getDayOfMonth()
                );
                dpd.setThemeDark(false);
                dpd.vibrate(true);
                dpd.dismissOnPause(false);
                dpd.showYearPickerFirst(false);
                dpd.setVersion(DatePickerDialog.Version.VERSION_1);
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
    }

    private void loadChallengeNameButton(){
        ImageButton button = (ImageButton) view.findViewById(R.id.imageButton_challengeName);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment newFragment = new EditChallengeNameDialogFragment();
                newFragment.show(getFragmentManager(), "challengeNameEditDialog");
                loadChallengeName();
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        data.date = LocalDate.of(year, monthOfYear+1, dayOfMonth);
        loadChallengeDate();
    }

    private class PickerAdapter extends FragmentPagerAdapter {
        private static final int NUM_PAGES = 2;
        Fragment datePickerFragment;

        PickerAdapter(FragmentManager fm) {
            super(fm);
            datePickerFragment = new DatePickerFragment1();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public Fragment getItem(int position) {
            return datePickerFragment;
        }
    }

    public static class EditChallengeNameDialogFragment extends DialogFragment {
        public EditChallengeNameDialogFragment(){}
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();

            builder.setTitle("Challenge Name");
            builder.setView(inflater.inflate(R.layout.dialog_challenge_name, null));
            EditText edit = (EditText) getView().findViewById(R.id.editText_challenge_name);
            edit.setText(data.title);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                            EditText edit = (EditText) getView().findViewById(R.id.editText_challenge_name);
                            String result = edit.getText().toString();
                            data.title = result;
                        }
                    });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            EditChallengeNameDialogFragment.this.getDialog().cancel();
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

}
