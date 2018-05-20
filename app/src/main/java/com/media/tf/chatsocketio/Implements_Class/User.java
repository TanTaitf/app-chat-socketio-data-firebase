package com.media.tf.chatsocketio.Implements_Class;

/**
 * Created by Windows 8.1 Ultimate on 11/08/2017.
 */

public class User {
    public String ten;
    public String linkimg;

    public User() {
        // mặc định nhận dữ liêu
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public void setLinkimg(String linkimg) {
        this.linkimg = linkimg;
    }

    public String getLinkimg() {
        return linkimg;
    }

    public User(String ten, String linkimg) {
        this.ten = ten;
        this.linkimg = linkimg;
    }
}
