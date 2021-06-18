package com.example.serenitea;

import androidx.annotation.NonNull;

public class User
{
    String ID, username, password, email, gender, name;

    public User(){}

    public User(String ID, String username, String password, String email, String gender, String name) {
        this.ID = ID;
        this.username = username;
        this.password = password;
        this.email = email;
        this.gender = gender;
        this.name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
