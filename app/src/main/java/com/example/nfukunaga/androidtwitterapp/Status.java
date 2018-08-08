package com.example.nfukunaga.androidtwitterapp;

import java.util.Date;
import java.util.concurrent.ExecutionException;

import twitter4j.ExtendedMediaEntity;
import twitter4j.MediaEntity;

//取得したタイムライン情報を扱うためのentityクラス
public class Status {
    private String text;
    private MediaEntity[] mediaEntities;
    private Date createdAt;
    private String userNameAndId;
    private String retweetUserName;

    public Status() {
    }

    public Status(String text, MediaEntity[] mediaEntities, Date date, String userNameAndId, String retweetUserName) {
        this.text = text;
        this.mediaEntities = mediaEntities;
        this.createdAt = date;
        this.userNameAndId = userNameAndId;
        this.retweetUserName=retweetUserName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MediaEntity[] getMediaEntities() {
        return mediaEntities;
    }

    public void setMediaEntities(MediaEntity[] mediaEntities) { this.mediaEntities = mediaEntities; }

    public Date getCreatedAt() { return createdAt; }

    public void setCreatedAt(Date date) {
        this.createdAt = date;
    }

    public String getUserNameAndId() {
        return userNameAndId;
    }

    public void setUserNameAndId(String userNameAndId) {
        this.userNameAndId = userNameAndId;
    }

    public String getRetweetUserName() { return retweetUserName; }

    public void setRetweetUserName(String retweetUserNameAndId) { this.retweetUserName = retweetUserNameAndId; }
}
