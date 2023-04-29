package org.techtown.healthycare.rank;

public class RankItem {
    String sportName;
    String calorie;
    String userId;
    int rank;

    public RankItem(){}



    public RankItem(String sportName, String calorie, String userId, int rank) {
        this.sportName = sportName;
        this.calorie = calorie;
        this.userId = userId;
        this.rank = rank;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public String getCalorie() {
        return calorie;
    }

    public void setCalorie(String calorie) {
        this.calorie = calorie;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
