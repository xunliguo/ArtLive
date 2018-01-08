package com.example.rh.artlive.bean;

/**
 * Created by rh on 2017/11/25.
 */

public class HomeBean {
    private String title;
    private String id;
    private String teacher_name;
    private String teacher_userid;
    private String teacher_major;
    private String teacher_direction;
    private String picarr_img_url;
    private String user_img;
    private String help;
    private String user_nickname;
    private String time;

    public String getTeacher_userid() {
        return teacher_userid;
    }

    public void setTeacher_userid(String teacher_userid) {
        this.teacher_userid = teacher_userid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getForum_content() {
        return forum_content;
    }

    public void setForum_content(String forum_content) {
        this.forum_content = forum_content;
    }

    private String forum_content;

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }


    private TeacherPicarrBean teacherPicarrBean;


    public TeacherPicarrBean getTeacherPicarrBean() {
        return teacherPicarrBean;
    }

    public void setTeacherPicarrBean(TeacherPicarrBean teacherPicarrBean) {
        this.teacherPicarrBean = teacherPicarrBean;
    }


    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getTeacher_major() {
        return teacher_major;
    }

    public void setTeacher_major(String teacher_major) {
        this.teacher_major = teacher_major;
    }

    public String getTeacher_direction() {
        return teacher_direction;
    }

    public void setTeacher_direction(String teacher_direction) {
        this.teacher_direction = teacher_direction;
    }

    public String getPicarr_img_url() {
        return picarr_img_url;
    }

    public void setPicarr_img_url(String picarr_img_url) {
        this.picarr_img_url = picarr_img_url;
    }

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
