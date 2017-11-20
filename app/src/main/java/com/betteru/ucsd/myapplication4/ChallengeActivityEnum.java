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
        Sitting("Sitting", R.drawable.sitting),
        Walking("Walking", R.drawable.walking),
        Bathing("Bathing", R.drawable.bathing),
        Party("At a party", R.drawable.at_a_party),
        Bar("At a bar", R.drawable.at_the_ar),
        Beach("At the beach", R.drawable.at_the_beach),
        Gym("At the gym", R.drawable.at_the_gym),
        Home("at home", R.drawable.athome),
        Work("at work", R.drawable.atwork),
        Cycling("Cycling", R.drawable.bicycling),
        Cleaning("Cleaning", R.drawable.cleaning),
        Computer("Computer Work", R.drawable.computer_work),
        Cooking("Cooking", R.drawable.cooking);



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

