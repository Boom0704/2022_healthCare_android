package org.techtown.healthycare;

public class mainImageModel {

    String skinName;
    int resId;


    public mainImageModel( int resId, String skinName){
        this.skinName = skinName;
        this.resId = resId;
    }


    public String getSkinName() {
        return skinName;
    }

    public void setSkinName(String skinName) {
        this.skinName = skinName;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }



}
