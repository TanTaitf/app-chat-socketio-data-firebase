package com.media.tf.chatsocketio.Implements_Class;

/**
 * Created by Windows 8.1 Ultimate on 12/08/2017.
 */

public class Usermessager {
    private String username;
    private String messager, time_messager;
    private boolean isSelf;
    private String linkimg;


    public Usermessager(String username, String messager, String time, String linkimg,Boolean isSelf) {
        this.username = username;
        this.messager = messager;
        this.isSelf = isSelf;
        this.time_messager = time;
        this.linkimg = linkimg;
    }

    public void setLinkimg(String linkimg) {
        this.linkimg = linkimg;
    }

    public String getLinkimg() {

        return linkimg;
    }

    public String getUsername() {
        return username;
    }

    public String getTime_messager() {
        return time_messager;
    }

    public void setTime_messager(String time_messager) {
        this.time_messager = time_messager;
    }

    public String getMessager() {
        return messager;
    }

    public void setSelf(boolean self) {
        isSelf = self;
    }

    public boolean isSelf() {

        return isSelf;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setMessager(String messager) {
        this.messager = messager;
    }
}
