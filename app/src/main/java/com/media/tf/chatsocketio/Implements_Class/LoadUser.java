package com.media.tf.chatsocketio.Implements_Class;

/**
 * Created by Windows 8.1 Ultimate on 11/08/2017.
 */

public class LoadUser implements Comparable<LoadUser>{
    String ten;
    String link;
    Boolean onl;

    public String getTen() {
        return ten;
    }

    public String getLink() {
        return link;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public Boolean getOnl() {
        return onl;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setOnl(Boolean onl) {
        this.onl = onl;
    }

    public LoadUser(String ten, String link, Boolean onl) {
        this.ten = ten;
        this.link = link;
        this.onl = onl;

    }

    // sắp xếp theo tên và theo ngay.
    @Override
    public int compareTo(LoadUser o) {
        return this.ten.compareTo(o.ten);
    }
}
