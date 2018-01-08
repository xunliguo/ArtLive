package com.example.rh.artlive.ottoEvent;

/**
 * Created by Administrator on 2017/8/25.
 */

public class AppDeleteEvent {
    private String delete;
    private String id;
    private String beizhu;


    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDelete() {
        return delete;
    }

    public AppDeleteEvent(String delete, String id, String beizhu) {
        this.delete=delete;
        this.id=id;
        this.beizhu=beizhu;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }

}
