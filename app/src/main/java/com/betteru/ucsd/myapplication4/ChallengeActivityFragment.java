package com.betteru.ucsd.myapplication4;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */


public class ChallengeActivityFragment extends Fragment
        implements DatePickerDialog.OnDateSetListener{
    static ChallengeModel data;
    Boolean editable;
    View view;
    public final static int EDITDIALOG_FRAGMENT = 1;
    public final static int EDIT_ACTIVITY_FRAGMENT = 2;
    public final static int EDIT_PARTICIPANTS_FRAGMENT = 3;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle args = getArguments();
        data = (ChallengeModel) args.getSerializable("data");
        editable = (Boolean) args.getBoolean("edit");
        Log.d("Challenge Activity Fragment Edit", editable.toString());
        view = inflater.inflate(R.layout.fragment_challenge_detail, container, false);
        loadChallengeName();
        loadChallengeDate();
        loadChallengeParticipants();
        loadChallengeActivities();
        loadEditButton(editable);
        return view;
    }
    public void loadEditButton(Boolean edit){
        Log.d("challenge data", data.timeStamp);
        if((!edit) || (data.winner != null)  || (data.date.before(Calendar.getInstance()))){
            Log.d("challenge data edit gone", "");
            ImageButton button2 = (ImageButton) view.findViewById(R.id.imageButton_challengeDate);
            button2.setVisibility(View.GONE);
            ImageButton button3 = (ImageButton) view.findViewById(R.id.imageButton_challengeName);
            button3.setVisibility(View.GONE);
            ImageButton button4 = (ImageButton) view.findViewById(R.id.imageButton_challengeActivity);
            button4.setVisibility(View.GONE);
            ImageButton button5 = (ImageButton) view.findViewById(R.id.imageButton_challengeParticipant);
            button5.setVisibility(View.GONE);
            Button button1 = view.findViewById(R.id.button_saveChallenge);
            button1.setVisibility(View.GONE);
            return;
        }
        Log.d("challenge data edit", "load challenge edit button");
        loadChallengeNameButton();
        loadChallengeDateButton();
        loadChallengeActivityButton();
        loadChallengeParticipantsButton();
        loadChallengeSubmitButton();
    }
    public void loadChallengeSubmitButton(){
        Button button = view.findViewById(R.id.button_saveChallenge);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("challenge", "submit challenge");
                submitChallenge();
            }
        });
    }
    public void submitChallengeParticipants(String docId, ArrayList<String> participants){
        final Fragment f = this;
        for(int i = 0; i < participants.size(); i++) {
            try {
                CollectionReference cf = db.collection("challenge_user");
                DocumentReference docRef = cf.document(participants.get(i));
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put(docId, 1);
                docRef.set(dataMap, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("challenge participants", "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("challenge participants", "Error writing document", e);
                            }
                        });
            }catch(Exception e){
                Log.e("winner submit challenge participants error", e.toString());
            }
        }
    }
    public void submitChallenge(){
        final Fragment f = this;
        showProgressDialog();
        //get unique data id
        DocumentReference ref = db.collection("challenge").document();
        if(data.id.isEmpty()){
            data.setId(ref.getId());
        }
        Log.d("submit challenge", data.participants.toString());
        Log.d("submit challenge", data.participants_name.toString());
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("owner",data.ownerId );
        dataMap.put("owner_name", data.owner_name);
        dataMap.put("participants", data.participants);
        dataMap.put("participants_name", data.participants_name);
        dataMap.put("activities", data.activities);
        dataMap.put("time", data.timeStamp);
        dataMap.put("title", data.title);
        Log.d("challenge", data.id);
        db.collection("challenge").document(data.id).set(dataMap, SetOptions.merge())
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                submitChallengeParticipants(data.id, data.participants);
                Log.d("challenge", "DocumentSnapshot successfully written!");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("challenge", "Error writing document", e);
            }
        }).addOnCompleteListener(new OnCompleteListener<Void>() {
             @Override
             public void onComplete(@NonNull Task<Void> task) {
                 /*
                ChallengeActivityResultFragment fragment = new ChallengeActivityResultFragment();
                Bundle args = new Bundle();
                args.putSerializable("data",data);
                fragment.setArguments(args);
                fx.replace(R.id.fragmentContent, fragment);
                fx.commit();
                */
                 /*
                 getFragmentManager().popBackStack();
                 FragmentTransaction fx = getFragmentManager().beginTransaction();
                 FragmentTransaction tx = getActivity().getFragmentManager().beginTransaction();
                 tx.remove(f);
                 tx.commit();
                 */
                 FragmentManager fragmentManager = getFragmentManager();
                 fragmentManager.popBackStack();
                 ChallengePagerFragment fragment = new ChallengePagerFragment().newInstance();
                 FragmentTransaction fx = getFragmentManager().beginTransaction();
                 fx.replace(R.id.fragmentContent, fragment);
                 fx.commit();
                 hideProgressDialog();
             }
         });
    }

    public void loadChallengeActivityButton(){
        ImageButton button = (ImageButton) view.findViewById(R.id.imageButton_challengeActivity);
        button.setVisibility(View.VISIBLE);
        /*
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                ChallengeActivityListFragment fragment = new ChallengeActivityListFragment();
                Bundle args = new Bundle();
                args.putSerializable("data",data);
                args.putBoolean("participants", false);
                fragment.setArguments(args);
                fragmentTransaction.replace(R.id.fragmentContent, fragment);
                fragmentTransaction.commit();
            }
        });
        */
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment newFragment = EditActivityDialogFragment.newInstance(data, false);
                newFragment.setTargetFragment(ChallengeActivityFragment.this, EDIT_ACTIVITY_FRAGMENT);
                newFragment.show(getFragmentManager(), "challengeActivityEditDialog");
            }
        });
    }

    public void loadChallengeParticipantsButton(){
        ImageButton button = (ImageButton) view.findViewById(R.id.imageButton_challengeParticipant);
        /*
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                ChallengeActivityListFragment fragment = new ChallengeActivityListFragment();
                Bundle args = new Bundle();
                args.putSerializable("data",data);
                args.putBoolean("participants", true);
                fragment.setArguments(args);
                fragmentTransaction.replace(R.id.fragmentContent, fragment);
                fragmentTransaction.commit();
            }
        });
        */
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment newFragment = EditActivityDialogFragment.newInstance(data, true);
                newFragment.setTargetFragment(ChallengeActivityFragment.this, EDIT_PARTICIPANTS_FRAGMENT);
                newFragment.show(getFragmentManager(), "challengeParticipantsEditDialog");
            }
        });
    }
    public void loadChallengeName() {
        TextView name = (TextView) view.findViewById(R.id.textView_challengeName);
        name.setText(data.title);
    }
    public void loadChallengeDate(){
        TextView date = (TextView) view.findViewById(R.id.textView_challengeDate);
        date.setText(data.sdf.format(data.date.getTime()));
    }
    public void loadChallengeParticipants() {
        ArrayList<String> name = new ArrayList<String>();
        ArrayList<String> id = new ArrayList<String>();

        name.add(data.owner_name);
        id.add(data.ownerId);
        for(int i = 0; i < data.participants.size(); i++)
        {
            name.add(data.participants_name.get(i));
            id.add(data.participants.get(i));
        }
        GridView gridViewParticipants = (GridView) view.findViewById(R.id.gridview_challenge_participants);
        ChallengeParticipantsAdapter adapterPar = new ChallengeParticipantsAdapter(this.getActivity(),
                name,id);
        gridViewParticipants.setAdapter(adapterPar);
    }
    public void loadChallengeActivities(){
        //set GridView
        GridView gridViewActivities = (GridView) view.findViewById(R.id.gridView_challenge_activities);
        ChallengeActivityAdapter adapterAct = new ChallengeActivityAdapter(this.getActivity(),
                data.activities, data.activitiesIcon);
        Log.d("challenge detail page", data.activities.toString());
        Log.d("challenge detail page", data.activitiesIcon.toString());
        gridViewActivities.setAdapter(adapterAct);
    }

    private void loadChallengeDateButton(){
        ImageButton button = (ImageButton) view.findViewById(R.id.imageButton_challengeDate);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //showDialog();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        ChallengeActivityFragment.this,
                        data.date.get(Calendar.YEAR),
                        data.date.get(Calendar.MONTH),
                        data.date.get(Calendar.DAY_OF_MONTH)
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

    public void loadChallengeNameButton(){
        ImageButton button = (ImageButton) view.findViewById(R.id.imageButton_challengeName);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment newFragment = EditChallengeNameDialogFragment.newInstance(data.title);
                newFragment.setTargetFragment(ChallengeActivityFragment.this, EDITDIALOG_FRAGMENT);
                newFragment.show(getFragmentManager(), "challengeNameEditDialog");
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        data.date.set(Calendar.YEAR, year);
        data.date.set(Calendar.MONTH, monthOfYear);
        data.date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        data.timeStamp = data.sdf.format(data.date.getTime());
        loadChallengeDate();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle bundle = data.getExtras();
        switch (requestCode) {
            case EDITDIALOG_FRAGMENT:
                if (resultCode == Activity.RESULT_OK) {
                    String title = bundle.getString("challengeName");
                    this.data.title = title;
                    loadChallengeName();
                } else if (resultCode == Activity.RESULT_CANCELED) {
                }
                break;
            case EDIT_ACTIVITY_FRAGMENT:
                if (resultCode == Activity.RESULT_OK) {
                    this.data = (ChallengeModel) bundle.getSerializable("data");
                    loadChallengeActivities();
                } else if (resultCode == Activity.RESULT_CANCELED) {
                }
                break;
            case EDIT_PARTICIPANTS_FRAGMENT:
                if (resultCode == Activity.RESULT_OK) {
                    this.data = (ChallengeModel) bundle.getSerializable("data");
                    loadChallengeParticipants();
                } else if (resultCode == Activity.RESULT_CANCELED) {
                }
                break;
        }
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

    public static class EditChallengeNameDialogFragment extends DialogFragment
            implements DialogInterface.OnDismissListener{

        public static EditChallengeNameDialogFragment newInstance(String title) {
            EditChallengeNameDialogFragment f = new EditChallengeNameDialogFragment();
            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putString("challengeName", title);
            f.setArguments(args);
            return f;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            builder.setTitle("Challenge Name");
            final View dialogView = inflater.inflate(R.layout.dialog_challenge_name, null);
            builder.setView(dialogView);
            EditText edit = (EditText) dialogView.findViewById(R.id.editText_challenge_name);
            String challengeName = getArguments().getString("challengeName");
            edit.setText(challengeName);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    EditText edit = (EditText) dialogView.findViewById(R.id.editText_challenge_name);
                    String title = edit.getText().toString();
                    Intent i = new Intent().putExtra("challengeName", title);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
                    dismiss();
                    //EditChallengeNameDialogFragment.this.loadChallengeName();
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

    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this.getContext());
            mProgressDialog.setMessage("Loading");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }



}
