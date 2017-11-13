package com.betteru.ucsd.myapplication4;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yuting on 11/12/2017.
 */

public enum ChallengeActivityEnum {

        Running("running", R.drawable.ic_directions_run_black_48dp),
        Sitting("sitting", R.drawable.ic_directions_bike_black_48dp),
        Walking("walking", R.drawable.ic_directions_walk_black_48dp);

        private String name;
        private Integer icon;

        ChallengeActivityEnum(String name, Integer icon) {
            this.name = name;
            this.icon = icon;
        }

        // Reverse-lookup map for getting a day from an abbreviation
        private static final Map<String, ChallengeActivityEnum> lookup = new HashMap<String, ChallengeActivityEnum>();

        static {
            for (ChallengeActivityEnum d : ChallengeActivityEnum.values()) {
                lookup.put(d.getName(), d);
            }
        }



        public String getName() {
            return name;
        }

        public static ChallengeActivityEnum get(String name) {
            return lookup.get(name);
        }

}
