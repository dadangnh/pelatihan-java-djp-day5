package com.dadangnh;

import java.time.LocalDate;

public class User extends ObjectEntity{
    private String email;
    private LocalDate registereddate;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getRegistereddate() {
        return registereddate;
    }

    public void setRegistereddate(LocalDate registereddate) {
        this.registereddate = registereddate;
    }
}
