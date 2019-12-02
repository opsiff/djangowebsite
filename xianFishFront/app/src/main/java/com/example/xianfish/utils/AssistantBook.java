package com.example.xianfish.utils;

import android.graphics.Bitmap;
import android.media.Image;

import java.io.Serializable;

public class AssistantBook implements Serializable {
    private String name;
    private String price;
    private String discription;
    private String attachNum;//联系电话
    private String userID;
    private int asssistentBookImage;

    public AssistantBook getAssistantBook(){
        return this;
    }

    public int getAsssistentBookImage() {
        return asssistentBookImage;
    }

    public void setAsssistentBookImage(int asssistentBookImage) {
        this.asssistentBookImage = asssistentBookImage;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAttachNum() {
        return attachNum;
    }

    public void setAttachNum(String attachNum) {
        this.attachNum = attachNum;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
