package com.betteru.ucsd.myapplication4;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Created by verazou on 11/19/17.
 */

public class UserModel implements Serializable {
    String name;
    String firstName;
    String userId;

    UserModel(String name, String firstName, String userId)
    {
        this.name = name;
        this.firstName = firstName;
        this.userId = userId;
    }

    public String getName() { return this.name; }
    public String getFirstName() { return this.firstName; }
    public String getUserId() { return this.userId; }

}

