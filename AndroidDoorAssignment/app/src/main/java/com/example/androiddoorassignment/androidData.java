package com.example.androiddoorassignment;

import java.io.Serializable;
public class androidData implements Serializable
{
    String androidId;
    String roomId;
    String tagId;
    int pinNumber;

    public androidData(String androidId, String roomId, String tagId, int pinNumber) {
        super();
        this.androidId = androidId;
        this.roomId = roomId;
        this.tagId = tagId;
        this.pinNumber = pinNumber;
    }

    public String getAndroidId() {
        return androidId;
    }
    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }
    public String getRoomId() {
        return roomId;
    }
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
    public String getTagId() {
        return tagId;
    }
    public void setTagId(String tagId) {
        this.tagId = tagId;
    }
    public int getPinNumber() {
        return pinNumber;
    }
    public void setPinNumber(int pinNumber) {
        this.pinNumber = pinNumber;
    }

    @Override
    public String toString() {
        return "AndroidData [androidId=" + androidId + ", roomId=" + roomId + ", tagId=" + tagId + ", pinNumber="
                + pinNumber + "]";
    }



}