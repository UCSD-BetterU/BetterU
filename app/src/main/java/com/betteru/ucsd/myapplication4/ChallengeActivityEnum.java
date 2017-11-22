package com.betteru.ucsd.myapplication4;

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
    Running("Running", R.drawable.running),
    Bicycling("Bicycling", R.drawable.bicycling),
    Sleeping("Sleeping", R.drawable.sleep),
    LabWork("Lab work", R.drawable.labwork),
    InClass("In class", R.drawable.inclass),
    InAMeeting("In a meeting", R.drawable.inmeeting),
    AtWork("At work", R.drawable.atwork),
    Indoors("Indoors", R.drawable.indoors),
    Outside("Outside", R.drawable.outside),
    InACar("In a car", R.drawable.inacar),
    OnABus("On a bus", R.drawable.inabus),
    Driver("Driver", R.drawable.driver),
    Passenger("Passenger", R.drawable.passenger),
    AtHome("At Home", R.drawable.athome),
    AtSchool("At School", R.drawable.school),
    AtARestaurant("At_a_restaurant", R.drawable.restaurant),
    Exercising("Exercising", R.drawable.exercising),
    Cooking("Cooking", R.drawable.cooking),
    Shopping("Shopping", R.drawable.shopping),
    Strolling("Strolling", R.drawable.strolling),
    Drinking("Drinking", R.drawable.drinking),
    Bathing("Bathing", R.drawable.bathing),
    Cleaning("Cleaning", R.drawable.cleaning),
    DoingLaundry("Doing laundry", R.drawable.laundry),
    WashingDishes("Washing dishes", R.drawable.washing_dishes),
    WatchingTV("Watching TV", R.drawable.tv),
    SurfingTheInternet("Surfing the internet", R.drawable.surfing_the_internet),
    AtAParty("At a party", R.drawable.at_a_party),
    AtABar("At a bar", R.drawable.at_the_bar),
    AtTheBeach("At the beach", R.drawable.at_the_beach),
    Singing("Singing", R.drawable.singing),
    Talking("Talking", R.drawable.talking),
    ComputerWork("Computer work", R.drawable.computer_work),
    Eating("Eating", R.drawable.eating),
    Toilet("Toilet", R.drawable.toilet),
    Grooming("Gromming", R.drawable.grooming),
    Dressing("Dressing", R.drawable.dressing),
    AtTheGym("At the gym", R.drawable.at_the_gym),
    StairsUp("Stairs up", R.drawable.stairs_up),
    StairsDown("Stairs down", R.drawable.stairs_down),
    Elevator("Elevator", R.drawable.elevator),
    PhoneInPocket("Phone in pocket", R.drawable.phone_in_pocket),
    PhoneInHand("Phone in hand", R.drawable.phone_in_hand),
    PhoneInBag("Phone in bag", R.drawable.phone_in_bag),
    PhoneOnTable("Phone on table", R.drawable.phone_on_table),
    WithCoworkers("With coworkers", R.drawable.with_coworkers),
    WithFriends("With friends", R.drawable.with_friends),


    Party("At a party", R.drawable.at_a_party),
    Bar("At a bar", R.drawable.at_the_bar),
    Beach("At the beach", R.drawable.at_the_beach),
    Gym("At the gym", R.drawable.at_the_gym),
    Home("at home", R.drawable.athome),
    Work("at work", R.drawable.atwork),
    Cycling("Cycling", R.drawable.bicycling),
    Computer("Computer Work", R.drawable.computer_work);





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
