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
    LabWok("Lab Wok", R.drawable.labwork),
    Class("Class", R.drawable.inclass),
    Meeting("Meeting", R.drawable.inmeeting),
    Work("Work", R.drawable.atwork),
    Indoors("Indoors", R.drawable.indoors),
    Outside("Outside", R.drawable.outside),
    Car("Car", R.drawable.inacar),
    Bus("Bus", R.drawable.inabus),
    Driver("Driver", R.drawable.driver),
    Passenger("Passenger", R.drawable.passenger),
    Home("Home", R.drawable.athome),
    School("School", R.drawable.school),
    Restaurant("Restaurant", R.drawable.restaurant),
    Exercising("Exercising", R.drawable.exercising),
    Cooking("Cooking", R.drawable.cooking),
    Shopping("Shopping", R.drawable.shopping),
    Strolling("Strolling", R.drawable.strolling),
    Drinking("Drinking",R.drawable.drinking),
    Bathing("Bathing", R.drawable.bathing),
    Cleaning("Cleaning", R.drawable.cleaning),
    Laundry("Laundry", R.drawable.laundry),
    Dishes("Dishes", R.drawable.washing_dishes),
    TV("TV", R.drawable.tv),
    Internet("Internet", R.drawable.surfing_the_internet),
    Party("Party", R.drawable.at_a_party),
    Bar("Bar", R.drawable.at_the_bar),
    Beach("Beach", R.drawable.at_the_beach),
    Singing("Singing", R.drawable.singing),
    Talking("Talking", R.drawable.talking),
    ComputerWork("Computer Work", R.drawable.computer_work),
    Eating("eating", R.drawable.eating),
    Toilet("Toiler", R.drawable.toilet),
    Grooming("Grooming", R.drawable.grooming),
    Dressing("Dressing", R.drawable.dressing),
    Gym("Gym", R.drawable.at_the_gym),
    StairsUp("Stairs Up", R.drawable.stairs_up),
    StairsDown("Stairs Down", R.drawable.stairs_down),
    Elevator("Elevator", R.drawable.elevator),
    Pocket("Pocket", R.drawable.phone_in_pocket),
    Hand("Hand", R.drawable.phone_in_hand),
    Bag("Bag", R.drawable.phone_in_bag),
    Table("Table", R.drawable.phone_on_table),
    Coworkers("Coworkers", R.drawable.with_coworkers),
    Friends("Friends", R.drawable.with_friends);

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

