package com.example.rh.artlive.bean;

import java.util.List;

/**
 * Created by rh on 2017/12/1.
 */

public class FollowBean {

    private String i;

    private String forum_id;
    private String forum_title;
    private String forum_time;
    private String forum_content;
    private String forum_userid;
    private String user_nickname;
    private String user_img;
    private String time;
    private String zfcount;
    private String zan;
    private String zanstate;


    public String getZanstate() {
        return zanstate;
    }

    public void setZanstate(String zanstate) {
        this.zanstate = zanstate;
    }


    public List<String> imgList;// 图片集合


    public List<String> getImgList() {
        return imgList;
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
    }

//    private String [] picarr;
//
//
//    public String[] getPicarr_img_url() {
//        return picarr_img_url;
//    }
//
//    public void setPicarr_img_url(String[] picarr_img_url) {
//        this.picarr_img_url = picarr_img_url;
//    }


    public String getZfcount() {
        return zfcount;
    }

    public void setZfcount(String zfcount) {
        this.zfcount = zfcount;
    }

    public String getZan() {
        return zan;
    }

    public void setZan(String zan) {
        this.zan = zan;
    }


    private PicarrBean picarrBean;


    public PicarrBean getPicarrBean() {
        return picarrBean;
    }

    public void setPicarrBean(PicarrBean picarrBean) {
        this.picarrBean = picarrBean;
    }



    public String getForum_id() {
        return forum_id;
    }

    public void setForum_id(String forum_id) {
        this.forum_id = forum_id;
    }

    public String getForum_title() {
        return forum_title;
    }

    public void setForum_title(String forum_title) {
        this.forum_title = forum_title;
    }

    public String getForum_time() {
        return forum_time;
    }

    public void setForum_time(String forum_time) {
        this.forum_time = forum_time;
    }

    public String getForum_content() {
        return forum_content;
    }

    public void setForum_content(String forum_content) {
        this.forum_content = forum_content;
    }

    public String getForum_userid() {
        return forum_userid;
    }

    public void setForum_userid(String forum_userid) {
        this.forum_userid = forum_userid;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getI() {
        return i;
    }

    public void setI(String i) {
        this.i = i;
    }

}
