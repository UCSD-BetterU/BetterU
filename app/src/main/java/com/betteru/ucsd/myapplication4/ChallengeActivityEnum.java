package com.betteru.ucsd.myapplication4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yuting on 11/12/2017.
 */

public enum ChallengeActivityEnum {

        Lying("Lying", R.drawable.lying_down),
        Sitting("Sitting", R.drawable.sitting),
        Standing("Standing", R.drawable.standing),
        Running("Running", R.drawable.running),
        Walking("Walking", R.drawable.walking),
        Bicycling("Bicycling", R.drawable.bicycling),
        Sleeping("Sleeping", R.drawable.sleep),
        Bathing("Bathing", R.drawable.bathing),
        Party("Party", R.drawable.at_a_party),
        Bar("Bar", R.drawable.at_the_bar),
        Beach("Beach", R.drawable.at_the_beach),
        Gym("Gym", R.drawable.at_the_gym),
        Home("Home", R.drawable.athome),
        Work("Work", R.drawable.atwork),

        Cleaning("Cleaning", R.drawable.cleaning),
        Computer("Computer", R.drawable.computer_work),
        Cooking("Cooking", R.drawable.cooking),
        Shopping("Shopping", R.drawable.shopping);

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

