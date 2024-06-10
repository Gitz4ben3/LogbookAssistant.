package com.example.logbookassistant;

public class Info {
    String name, surname, email, phone, persal, supervisor;

    public Info() {
    }


    public Info(String name, String surname, String email, String phone, String persal, String supervisor) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.persal = persal;
        this.supervisor = supervisor;
    }

    public String getPersal() {
        return persal;
    }

    public void setPersal(String persal) {
        this.persal = persal;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}