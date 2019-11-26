package com.example.archer.myapplication;

/**
 * Created by archer on 2018/4/21.
 */

import android.view.View;


public class Item {
    private int image;
    private String title;
    private String description;
    private String AC;



    public void setImage(int image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAC(String AC) {
        this.AC = AC;
    }

    public String getAC(){return AC;}

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }





}
