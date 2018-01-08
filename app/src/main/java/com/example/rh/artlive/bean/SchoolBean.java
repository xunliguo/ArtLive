package com.example.rh.artlive.bean;

/**
 * Created by rh on 2017/12/23.
 */

public class SchoolBean {


    private String school_name;
    private String school_id;
    private String major_content;
    private String majortop;
    private String info;

    private String teacher_name;
    private String teacher_direction;

    private String user_theschool;
    private String user_major;
    private String user_img;


    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }


    public String getUser_theschool() {
        return user_theschool;
    }

    public void setUser_theschool(String user_theschool) {
        this.user_theschool = user_theschool;
    }

    public String getUser_major() {
        return user_major;
    }

    public void setUser_major(String user_major) {
        this.user_major = user_major;
    }



    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }


    public String getTeacher_direction() {
        return teacher_direction;
    }

    public void setTeacher_direction(String teacher_direction) {
        this.teacher_direction = teacher_direction;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }



    public String getMajortop() {
        return majortop;
    }

    public void setMajortop(String majortop) {
        this.majortop = majortop;
    }



    public String getMajor_content() {
        return major_content;
    }

    public void setMajor_content(String major_content) {
        this.major_content = major_content;
    }


    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

}
