package com.example.pilaskow.bookrentallocator.model;

public class User {
    private String userName;
    private String userSurname;
    private String id;
    private static final User instance= new User();

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSurname() {
        return userSurname;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static User getInstance(){
        return instance;
    }
}
