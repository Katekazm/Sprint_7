package com.example;

public class Courier {
    public String login;
    public String password;
    public String firstName;

    public Courier(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    public static Courier random() {
        return new Courier("user" + System.currentTimeMillis(), "1234", "Saske");
    }

    @Override
    public String toString() {
        return String.format("login=%s, password=%s, firstName=%s", login, password, firstName);
    }
}
