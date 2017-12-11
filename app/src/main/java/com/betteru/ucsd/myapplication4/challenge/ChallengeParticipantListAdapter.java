package com.betteru.ucsd.myapplication4.challenge;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.betteru.ucsd.myapplication4.R;
import com.facebook.login.widget.ProfilePictureView;

import java.util.ArrayList;

/**
 * Created by Yuting on 12/11/2017.
 */

public class ChallengeParticipantListAdapter extends BaseAdapter {
    //public ArrayList<UserModel> list = new ArrayList<>();
    public ArrayList<String> nameList;
    public ArrayList<String> idList;
    Activity activity;
    TextView txtName;
    ImageView imgIcon;
    ProfilePictureView profile;

    public ChallengeParticipantListAdapter(Activity activity,ArrayList<String> nameList, ArrayList<String> idList){
        super();
        this.activity =activity;
        this.nameList = nameList;
        this.idList = idList;
    }

    @Override
    public int getCount() {
        return nameList.size();
    }

    @Override
    public Object getItem(int position) {
        return nameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater=activity.getLayoutInflater();

        if(convertView == null){

            convertView=inflater.inflate(R.layout.item_challenge_activities, null);
            txtName = (TextView) convertView.findViewById(R.id.textView_icon_name);
            imgIcon = (ImageView) convertView.findViewById(R.id.imageView_icon);
            profile = (ProfilePictureView) convertView.findViewById(R.id.profileView_icon);
            profile = (ProfilePictureView) convertView.findViewById(R.id.profileView_icon);
        }
        txtName.setText(nameList.get(position));
        imgIcon.setVisibility(View.GONE);
        profile.setProfileId(idList.get(position));
        profile.setCropped(true);
        return convertView;
    }
}
