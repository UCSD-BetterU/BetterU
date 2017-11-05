package com.betteru.ucsd.myapplication4;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChallengeActivityFragment extends Fragment {
    ChallengeModel data;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle args = getArguments();
        data = (ChallengeModel) args.getSerializable("data");
        Log.d("DATA", Integer.toString(data.activitiesIcon.size()));
        view = inflater.inflate(R.layout.fragment_challenge_detail, container, false);
        loadData();
        loadGridView();
        return view;
    }
    public void loadData()
    {

        EditText name = (EditText) view.findViewById(R.id.editText_challengeName);
        name.setText(data.title);
        EditText date = (EditText) view.findViewById(R.id.editText_challengeTime);
        date.setText(data.timeStamp);
    }
    public void loadGridView()
    {
        //set GridView
        GridView gridViewParticipants = (GridView) view.findViewById(R.id.gridview_challenge_participants);
        ChallengeActivityAdapter adapterPar =new ChallengeActivityAdapter(this.getActivity(), data.participants, data.participantsIcon);
        gridViewParticipants.setAdapter(adapterPar);
        //set GridView
        GridView gridViewActivities = (GridView) view.findViewById(R.id.gridView_challenge_activities);
        ChallengeActivityAdapter adapterAct =new ChallengeActivityAdapter(this.getActivity(), data.activities, data.activitiesIcon);
        gridViewActivities.setAdapter(adapterAct);
    }

}
