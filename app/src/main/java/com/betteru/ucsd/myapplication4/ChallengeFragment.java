package com.betteru.ucsd.myapplication4;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Arrays;


public class ChallengeFragment extends Fragment {

    ArrayList<ChallengeModel> data = new ArrayList<>();
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_challenge, container, false);
        loadData();
        loadListView();
        return view;
    }

    public ChallengeFragment newInstance() {
        ChallengeFragment fragment = new ChallengeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void loadData(){
        data.clear();
        ArrayList<String> par = new ArrayList<String>(Arrays.asList("user0002", "user0003", "", ""));
        ArrayList<Integer> parIcon = new ArrayList<Integer>();
        parIcon.add(R.drawable.ic_face_black_48dp);
        parIcon.add(R.drawable.ic_face_black_48dp);
        parIcon.add(R.drawable.ic_add_circle_black_48dp);
        parIcon.add(R.drawable.ic_remove_circle_black_48dp);
        ArrayList<String> act = new ArrayList<String>(Arrays.asList("Running", "Walking", "Showering", "", ""));
        ArrayList<Integer> actIcon = new ArrayList<>();
        actIcon.add(R.drawable.ic_directions_run_black_48dp);
        actIcon.add(R.drawable.ic_directions_walk_black_48dp);
        actIcon.add(R.drawable.ic_directions_bike_black_48dp);
        actIcon.add(R.drawable.ic_add_circle_black_48dp);
        actIcon.add(R.drawable.ic_remove_circle_black_48dp);
        ChallengeModel data1 = new ChallengeModel("user0001",
                "My First Challenge", "2017-01-01", par, act);
        data1.setIcon(parIcon, actIcon);
        ChallengeModel data2 = new ChallengeModel("user0001",
                "My Second Challenge", "2017-01-02", par, act);
        data2.setIcon(parIcon, actIcon);
        ChallengeModel data3 = new ChallengeModel("user0001",
                "My Third Challenge", "2017-01-02", par, act);
        data3.setIcon(parIcon, actIcon);
        data.add(data1);
        data.add(data2);
        data.add(data3);
    }
    private void loadListView(){
        ListView listView=(ListView) view.findViewById(R.id.ListView_challenge);
        ChallengeAdapter adapter=new ChallengeAdapter(this.getActivity(), data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
            {
                int pos=position+1;
                Toast.makeText(getActivity(), "Hello", Toast.LENGTH_SHORT).show();

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                ChallengeActivityFragment fragment = new ChallengeActivityFragment();

                Bundle args = new Bundle();
                args.putSerializable("data",data.get(position));
                fragment.setArguments(args);
                fragmentTransaction.replace(R.id.fragmentContent, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }


}