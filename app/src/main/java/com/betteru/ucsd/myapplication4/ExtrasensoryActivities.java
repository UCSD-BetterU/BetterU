package com.betteru.ucsd.myapplication4;

import java.util.HashMap;

public class ExtrasensoryActivities {
    public HashMap<String, ExtrasensoryActivity> map;

    public ExtrasensoryActivities() {
        this.map = new HashMap<>();

        this.map.put("Lying down",
                new ExtrasensoryActivity("lying down", "Lying",1, R.drawable.lying_down, 0));
        this.map.put("Sitting",
                new ExtrasensoryActivity("sitting", "Sitting", 1, R.drawable.sitting, 1));
        this.map.put("Standing",
                new ExtrasensoryActivity("standing", "Standing", 1, R.drawable.standing, 2));
        this.map.put("Walking",
                new ExtrasensoryActivity("walking", "Walking", 5, R.drawable.walking, 3));
        this.map.put("Running",
                new ExtrasensoryActivity("running", "Running", 5, R.drawable.running,4 ));
        this.map.put("Bicycling",
                new ExtrasensoryActivity("bicycling", "Bicycling", 2, R.drawable.bicycling, 5));
        this.map.put("Sleeping",
                new ExtrasensoryActivity("Sleeping", "sleeping", 2, R.drawable.sleep, 6));
        this.map.put("Lab work",
                new ExtrasensoryActivity("doing lab work", "Lab", 2, R.drawable.labwork, 7));
        this.map.put("In class",
                new ExtrasensoryActivity("in class", "Class", 2, R.drawable.inclass, 8));
        this.map.put("In a meeting",
                new ExtrasensoryActivity("in meetings", "Meeting", 2, R.drawable.inmeeting, 9));
        this.map.put("At work",
                new ExtrasensoryActivity("at work", "Work", 3, R.drawable.atwork, 10));
        this.map.put("Indoors",
                new ExtrasensoryActivity("indoors", "Indoors", 3, R.drawable.indoors, 11));
        this.map.put("Outside",
                new ExtrasensoryActivity("outside", "Outside", 3, R.drawable.outside, 12));
        this.map.put("In a car",
                new ExtrasensoryActivity("in a car", "Car", 3, R.drawable.inacar, 13));
        this.map.put("On a bus",
                new ExtrasensoryActivity("on a bus", "Bus", 3, R.drawable.inabus, 14));
        this.map.put("Drive - I'm the driver",
                new ExtrasensoryActivity("driving", "Driver", 2, R.drawable.driver, 15));
        this.map.put("Drive - I'm a passenger",
                new ExtrasensoryActivity("taking a ride", "Passenger", 2, R.drawable.passenger, 16));
        this.map.put("At home",
                new ExtrasensoryActivity("at home", "Home", 3, R.drawable.athome, 17));
        this.map.put("At school",
                new ExtrasensoryActivity("at school", "School", 3, R.drawable.school, 18));
        this.map.put("At a restaurant",
                new ExtrasensoryActivity("at a restaurant", "Restaurant", 3, R.drawable.restaurant, 19));
        this.map.put("Exercise",
                new ExtrasensoryActivity("exercising", "Exercise", 1, R.drawable.exercising, 20));
        this.map.put("Cooking",
                new ExtrasensoryActivity("cooking", "Cooking", 2, R.drawable.cooking, 21));
        this.map.put("Shopping",
                new ExtrasensoryActivity("shopping", "Shopping", 2, R.drawable.shopping, 22));
        this.map.put("Strolling",
                new ExtrasensoryActivity("strolling", "Strolling", 2, R.drawable.strolling, 23));
        this.map.put("Drinking (alcohol)",
                new ExtrasensoryActivity("drinking alcohol", "Drinking", 2, R.drawable.drinking, 24));
        this.map.put("Bathing - shower",
                new ExtrasensoryActivity("bathing", "Bathing", 2, R.drawable.bathing, 25));
        this.map.put("Cleaning",
                new ExtrasensoryActivity("cleaning", "Cleaning", 2, R.drawable.cleaning, 26));
        this.map.put("Doing laundry",
                new ExtrasensoryActivity("doing laundry", "Laundry", 2, R.drawable.laundry, 27));
        this.map.put("Washing dishes",
                new ExtrasensoryActivity("washing dishes", "Dishes", 2, R.drawable.washing_dishes, 28));
        this.map.put("Watching TV",
                new ExtrasensoryActivity("watching TV", "TV", 2, R.drawable.tv, 29));
        this.map.put("Surfing the internet",
                new ExtrasensoryActivity("surfing the Internet", "Internet", 2, R.drawable.surfing_the_internet, 30));
        this.map.put("At a party",
                new ExtrasensoryActivity("at a party", "Party", 2, R.drawable.at_a_party, 31));
        this.map.put("At a bar",
                new ExtrasensoryActivity("at a bar", "Bar", 2, R.drawable.at_the_bar, 32));
        this.map.put("At the beach",
                new ExtrasensoryActivity("at the beach", "Beach",2, R.drawable.at_the_beach, 33));
        this.map.put("Singing",
                new ExtrasensoryActivity("singing", "Singing", 2, R.drawable.singing, 34));
        this.map.put("Talking",
                new ExtrasensoryActivity("talking", "Talking", 2, R.drawable.talking, 35));
        this.map.put("Computer work",
                new ExtrasensoryActivity("computer work", "Computer",2, R.drawable.computer_work, 36));
        this.map.put("Eating",
                new ExtrasensoryActivity("eating", "Eating", 2, R.drawable.eating, 37));
        this.map.put("Toilet",
                new ExtrasensoryActivity("in toilet", "Toilet", 2, R.drawable.toilet, 38));
        this.map.put("Grooming",
                new ExtrasensoryActivity("grooming", "Grooming", 2, R.drawable.grooming, 39));
        this.map.put("Dressing",
                new ExtrasensoryActivity("dressing", "Dressing", 2, R.drawable.dressing, 40));
        this.map.put("At the gym",
                new ExtrasensoryActivity("at the gym", "Gym", 2, R.drawable.at_the_gym, 41));
        this.map.put("Stairs - going up",
                new ExtrasensoryActivity("going up stairs", "Stairs(Up)", 2, R.drawable.stairs_up, 42));
        this.map.put("Stairs - going down",
                new ExtrasensoryActivity("going down stairs", "Stairs(Down)", 2, R.drawable.stairs_down, 43));
        this.map.put("Elevator",
                new ExtrasensoryActivity("taking an elevator", "Elevator", 2, R.drawable.elevator, 44));
        this.map.put("Phone in pocket",
                new ExtrasensoryActivity("phone in pocket", "Pocket",4, R.drawable.phone_in_pocket, 45));
        this.map.put("Phone in hand",
                new ExtrasensoryActivity("phone in hand", "Hand", 4, R.drawable.phone_in_hand, 46));
        this.map.put("Phone in bag",
                new ExtrasensoryActivity("phone in bag", "Bag", 4, R.drawable.phone_in_bag, 47));
        this.map.put("Phone on table",
                new ExtrasensoryActivity("phone on table", "Table", 4, R.drawable.phone_on_table, 48));
        this.map.put("With co-workers",
                new ExtrasensoryActivity("with co-workers", "Co-workers", 2, R.drawable.with_coworkers, 49));
        this.map.put("With friends",
                new ExtrasensoryActivity("with friends", "Friends", 2, R.drawable.with_friends, 50));
    }


}
