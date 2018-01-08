package com.example.rh.artlive.bean;

import com.example.rh.artlive.articlechoice.QuestionOptionBean;

import java.util.List;

/**
 * Created by rh on 2017/12/13.
 */

public class CorrectBean {

    private String error_id;

    public CorrectBean(String error_id) {
        super();
        this.error_id = error_id;
    }

    public String getError_id() {
        return error_id;
    }

    public void setError_id(String error_id) {
        this.error_id = error_id;
    }

    @Override
    public String toString() {
        return "CorrectBean [error_id=" + error_id + "]";
    }

}
