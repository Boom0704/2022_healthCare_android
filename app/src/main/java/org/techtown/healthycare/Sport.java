package org.techtown.healthycare;

public class Sport {

    private int calorie =0 ;
    private String earningPoint="";
    private int runTime=0;
    private String sportName="";

    public Sport(){}

    public Sport(String sportName, int calorie,  String earningPoint){
        this.sportName =sportName;
        this.calorie =calorie;
        this.earningPoint = earningPoint;
    }


    public Sport(String sportName, int calorie, String earningPoint, int runTime) {
        this.sportName = sportName;
        this.calorie = calorie;
        this.earningPoint = earningPoint;
        this.runTime = runTime;
    }

    public int getRunTime() {
        return runTime;
    }

    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }

    public String getEarningPoint() {
        return earningPoint;
    }

    public void setEarningPoint(String earningPoint) {
        this.earningPoint = earningPoint;
    }
}
