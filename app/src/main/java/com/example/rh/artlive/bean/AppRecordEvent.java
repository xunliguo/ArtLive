package com.example.rh.artlive.bean;

/**
 * Created by rh on 2018/1/2.
 */

public class AppRecordEvent {

    private String name;
    private String image;
    public AppRecordEvent(String image,String name) {
        this.name = name;
        this.image = image;
    }
    public String getName(){
        return name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
