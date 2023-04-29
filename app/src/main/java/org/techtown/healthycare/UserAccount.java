package org.techtown.healthycare;

import java.util.ArrayList;
import java.util.List;

public class UserAccount {


    private int age=0;
    private int calorie=0;
    private String pass="";
    private List<Sport> sport= new ArrayList<>();
    private String userEmail="";
    private int userPoint=0;

    public UserAccount(){}

    public UserAccount(String userEmail, String pass,int age, int calorie, int userPoint) {
        this.age = age;
        this.calorie = calorie;
        this.pass = pass;
        this.userEmail = userEmail;
        this.userPoint = userPoint;
    }

    public UserAccount(String userEmail, String pass, int age, int calorie, int userPoint, List<Sport> sport) {
        this.userEmail = userEmail;
        this.pass = pass;
        this.age = age;
        this.calorie = calorie;
        this.userPoint = userPoint;
        this.sport = sport;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }

    public int getUserPoint() {
        return userPoint;
    }

    public void setUserPoint(int userPoint) {
        this.userPoint = userPoint;
    }

    public List<Sport> getSport() {
        return sport;
    }

    public void setSport(List<Sport> sport) {
        this.sport = sport;
    }
}
