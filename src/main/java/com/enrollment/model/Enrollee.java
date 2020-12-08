package com.enrollment.model;

import org.springframework.lang.NonNull;

import java.util.List;

public class Enrollee {
    @NonNull
    long id;
    @NonNull
    String name;
    @NonNull
    Boolean activationStatus;
    int phoneNumber;
    @NonNull
    String birthDate;

    List<Dependants> dependantsList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setActivationStatus(Boolean activationStatus) {
        this.activationStatus = activationStatus;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @NonNull
    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(@NonNull String birthDate) {
        this.birthDate = birthDate;
    }

    @NonNull
    public Boolean getActivationStatus() {
        return activationStatus;
    }

    public List<Dependants> getDependantsList() {
        return dependantsList;
    }

    public void setDependantsList(List<Dependants> dependantsList) {
        this.dependantsList = dependantsList;
    }

    @Override
    public String toString() {
        return "Enrollee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", activationStatus=" + activationStatus +
                ", phoneNumber=" + phoneNumber +
                ", birthDate='" + birthDate + '\'' +
                ", dependantsList=" + dependantsList +
                '}';
    }
}

