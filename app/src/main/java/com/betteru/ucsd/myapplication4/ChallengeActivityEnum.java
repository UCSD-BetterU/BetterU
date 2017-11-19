package com.betteru.ucsd.myapplication4;

import com.squareup.okhttp.Challenge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yuting on 11/12/2017.
 */

public enum ChallengeActivityEnum {

        Lyingdown("Lying down", R.drawable.lying_down),
        Sitting("Sitting", R.drawable.ic_directions_bike_black_48dp),
        Walking("Walking", R.drawable.ic_directions_walk_black_48dp),
        Showering("Showering", R.drawable.ic_directions_walk_black_48dp),
        Studying("Studying", R.drawable.ic_directions_walk_black_48dp),
        Working("Working", R.drawable.ic_directions_walk_black_48dp);

        private final String name;
        private final Integer icon;

        // Reverse-lookup map for getting a day from an abbreviation
        private static final Map<String, ChallengeActivityEnum> lookup = new HashMap<String, ChallengeActivityEnum>();

        static {
            for (ChallengeActivityEnum d : ChallengeActivityEnum.values()) {
                lookup.put(d.getName(), d);
            }
        }

        private ChallengeActivityEnum(String name, Integer icon) {
            this.name = name;
            this.icon = icon;
        }

        public String getName() {
            return name;
        }
        public Integer getIcon() {return icon;}

        public static ChallengeActivityEnum get(String name) {
            return lookup.get(name);
        }

        public static String[] getAllName(){
            ArrayList<String> ret = new ArrayList<String>();
            for (ChallengeActivityEnum d : ChallengeActivityEnum.values()) {
                ret.add(d.getName());
            }
            return Arrays.copyOf(ret.toArray(), ret.toArray().length, String[].class);

        }
    }

