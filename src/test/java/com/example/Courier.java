package com.example;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Courier {
    private String login;
    private String password;
    private String firstName;

    public static Courier random() {
        return new Courier("user" + System.currentTimeMillis(), "1234", "Saske");
    }

    @Override
    public String toString() {
        return String.format("login=%s, password=%s, firstName=%s", login, password, firstName);
    }
}
