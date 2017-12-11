package com.betteru.ucsd.myapplication4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yuting on 11/12/2017.
 */

public enum ChallengeActivityEnum {

    Lying("Lying down", R.drawable.lying_down),
    Sitting("Sitting", R.drawable.sitting),
    Standing("Standing", R.drawable.standing),
    Running("Running", R.drawable.running),
    Walking("Walking", R.drawable.walking),
    Bicycling("Bicycling", R.drawable.bicycling),
    Sleeping("Sleeping", R.drawable.sleep),
    LabWork("Lab work", R.drawable.labwork),
    Class("In class", R.drawable.inclass),
    Meeting("In a meeting", R.drawable.inmeeting),
    Work("At Work", R.drawable.atwork),
    Indoors("Indoors", R.drawable.indoors),
    Outside("Outside", R.drawable.outside),
    Car("In a car", R.drawable.inacar),
    Bus("On a bus", R.drawable.inabus),
    Driver("Drive - I'm the driver", R.drawable.driver),
    Passenger("Drive - I'm a passenger", R.drawable.passenger),
    Home("At home", R.drawable.athome),
    School("At school", R.drawable.school),
    Restaurant("At a restaurant", R.drawable.restaurant),
    Exercising("Exercise", R.drawable.exercising),
    Cooking("Cooking", R.drawable.cooking),
    Shopping("Shopping", R.drawable.shopping),
    Strolling("Strolling", R.drawable.strolling),
    Drinking("Drinking (alcohol)",R.drawable.drinking),
    Bathing("Bathing - shower", R.drawable.bathing),
    Cleaning("Cleaning", R.drawable.cleaning),
    Laundry("Doing laundry", R.drawable.laundry),
    Dishes("Washing dishes", R.drawable.washing_dishes),
    TV("Watching TV", R.drawable.tv),
    Internet("Surfing the internet", R.drawable.surfing_the_internet),
    Party("At a party", R.drawable.at_a_party),
    Bar("At a bar", R.drawable.at_the_bar),
    Beach("At the beach", R.drawable.at_the_beach),
    Singing("Singing", R.drawable.singing),
    Talking("Talking", R.drawable.talking),
    ComputerWork("Computer work", R.drawable.computer_work),
    Eating("Eating", R.drawable.eating),
    Toilet("Toilet", R.drawable.toilet),
    Grooming("Grooming", R.drawable.grooming),
    Dressing("Dressing", R.drawable.dressing),
    Gym("At the gym", R.drawable.at_the_gym),
    StairsUp("Stairs - going up", R.drawable.stairs_up),
    StairsDown("Stairs - going down", R.drawable.stairs_down),
    Elevator("Elevator", R.drawable.elevator),
    Pocket("Phone in pocket", R.drawable.phone_in_pocket),
    Hand("Phone in hand", R.drawable.phone_in_hand),
    Bag("Phone in bag", R.drawable.phone_in_bag),
    Table("Phone on table", R.drawable.phone_on_table),
    Coworkers("With co-workers", R.drawable.with_coworkers),
    Friends("With friends", R.drawable.with_friends);

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

