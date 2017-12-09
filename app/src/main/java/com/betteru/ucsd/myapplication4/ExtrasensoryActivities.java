package com.betteru.ucsd.myapplication4;

import java.util.HashMap;

public class ExtrasensoryActivities {
    public HashMap<String, ExtrasensoryActivity> map;

    public ExtrasensoryActivities() {
        this.map = new HashMap<>();

        this.map.put("LyingDown",
                new ExtrasensoryActivity("lying down", "Lying Down",1, R.drawable.lying_down));
        this.map.put("Sitting",
                new ExtrasensoryActivity("sitting", "Sitting", 1, R.drawable.sitting));
        this.map.put("Standing",
                new ExtrasensoryActivity("standing", "Standing", 1, R.drawable.standing));
        this.map.put("Walking",
                new ExtrasensoryActivity("walking", "Walking", 5, R.drawable.walking));
        this.map.put("Running",
                new ExtrasensoryActivity("running", "Running", 5, R.drawable.running));
        this.map.put("Bicycling",
                new ExtrasensoryActivity("bicycling", "Bicycling", 2, R.drawable.bicycling));
        this.map.put("Sleeping",
                new ExtrasensoryActivity("Sleeping", "sleeping", 2, R.drawable.sleep));
        this.map.put("LabWork",
                new ExtrasensoryActivity("doing lab work", "Lab Work", 2, R.drawable.labwork));
        this.map.put("Class",
                new ExtrasensoryActivity("in class", "In A Class", 2, R.drawable.inclass));
        this.map.put("Meeting",
                new ExtrasensoryActivity("in meetings", "In A Meeting", 2, R.drawable.inmeeting));
        this.map.put("Work",
                new ExtrasensoryActivity("at work", "At Work", 3, R.drawable.atwork));
        this.map.put("Indoors",
                new ExtrasensoryActivity("indoors", "Indoors", 3, R.drawable.indoors));
        this.map.put("Outside",
                new ExtrasensoryActivity("outside", "Outside", 3, R.drawable.outside));
        this.map.put("Car",
                new ExtrasensoryActivity("in a car", "In A Car", 3, R.drawable.inacar));
        this.map.put("Bus",
                new ExtrasensoryActivity("on a bus", "On A Bus", 3, R.drawable.inabus));
        this.map.put("Driver",
                new ExtrasensoryActivity("driving", "Driver", 2, R.drawable.driver));
        this.map.put("Passenger",
                new ExtrasensoryActivity("taking a ride", "Passenger", 2, R.drawable.passenger));
        this.map.put("Home",
                new ExtrasensoryActivity("at home", "At Home", 3, R.drawable.athome));
        this.map.put("School",
                new ExtrasensoryActivity("at school", "At School", 3, R.drawable.school));
        this.map.put("Restaurant",
                new ExtrasensoryActivity("at a restaurant", "At A Restauran", 3, R.drawable.restaurant));
        this.map.put("Exercising",
                new ExtrasensoryActivity("exercising", "Exercising", 1, R.drawable.exercising));
        this.map.put("Cooking",
                new ExtrasensoryActivity("cooking", "Cooking", 2, R.drawable.cooking));
        this.map.put("Shopping",
                new ExtrasensoryActivity("shopping", "Shopping", 2, R.drawable.shopping));
        this.map.put("Strolling",
                new ExtrasensoryActivity("strolling", "Strolling", 2, R.drawable.strolling));
        this.map.put("Drinking",
                new ExtrasensoryActivity("drinking alcohol", "Drinking", 2, R.drawable.drinking));
        this.map.put("Bathing",
                new ExtrasensoryActivity("bathing", "Bathing", 2, R.drawable.bathing));
        this.map.put("Cleaning",
                new ExtrasensoryActivity("cleaning", "Cleaning", 2, R.drawable.cleaning));
        this.map.put("Laundry",
                new ExtrasensoryActivity("doing laundry", "Laundry", 2, R.drawable.laundry));
        this.map.put("Dishes",
                new ExtrasensoryActivity("washing dishes", "Washing Dishes", 2, R.drawable.washing_dishes));
        this.map.put("TV",
                new ExtrasensoryActivity("watching TV", "Watching TV", 2, R.drawable.tv));
        this.map.put("Internet",
                new ExtrasensoryActivity("surfing the Internet", "Surfing the Internet", 2, R.drawable.surfing_the_internet));
        this.map.put("Party",
                new ExtrasensoryActivity("at a party", "At A Party", 2, R.drawable.at_a_party));
        this.map.put("Bar",
                new ExtrasensoryActivity("at a bar", "At A Bar", 2, R.drawable.at_the_bar));
        this.map.put("Beach",
                new ExtrasensoryActivity("at the beach", "At the Beach",2, R.drawable.at_the_beach));
        this.map.put("Singing",
                new ExtrasensoryActivity("singing", "Singing", 2, R.drawable.singing));
        this.map.put("Talking",
                new ExtrasensoryActivity("talking", "Talking", 2, R.drawable.talking));
        this.map.put("Computerwork",
                new ExtrasensoryActivity("computer work", "Computer Work",2, R.drawable.computer_work));
        this.map.put("Eating",
                new ExtrasensoryActivity("eating", "Eating", 2, R.drawable.eating));
        this.map.put("Toilet",
                new ExtrasensoryActivity("in toilet", "Toilet", 2, R.drawable.toilet));
        this.map.put("Grooming",
                new ExtrasensoryActivity("grooming", "Grooming", 2, R.drawable.grooming));
        this.map.put("Dressing",
                new ExtrasensoryActivity("dressing", "Dressing", 2, R.drawable.dressing));
        this.map.put("Gym",
                new ExtrasensoryActivity("at a gym", "At A Gym", 2, R.drawable.at_the_gym));
        this.map.put("StairsUp",
                new ExtrasensoryActivity("going up stairs", "Going Up Stairs", 2, R.drawable.stairs_up));
        this.map.put("StairsDown",
                new ExtrasensoryActivity("going down stairs", "Going Down Stairs", 2, R.drawable.stairs_down));
        this.map.put("Elevator",
                new ExtrasensoryActivity("taking an elevator", "Elevator", 2, R.drawable.elevator));
        this.map.put("Pocket",
                new ExtrasensoryActivity("phone in pocket", "Phone In Pocket",4, R.drawable.phone_in_pocket));
        this.map.put("Band",
                new ExtrasensoryActivity("phone in hand", "Phone In Hand", 4, R.drawable.phone_in_hand));
        this.map.put("Bag",
                new ExtrasensoryActivity("phone in bag", "Phone In Bag", 4, R.drawable.phone_in_bag));
        this.map.put("Table",
                new ExtrasensoryActivity("phone on table", "Phone On Table", 4, R.drawable.phone_on_table));
        this.map.put("Coworkers",
                new ExtrasensoryActivity("with co-workers", "With Co-workers", 2, R.drawable.with_coworkers));
        this.map.put("friends",
                new ExtrasensoryActivity("with friends", "With Friends", 2, R.drawable.with_friends));
    }


}
