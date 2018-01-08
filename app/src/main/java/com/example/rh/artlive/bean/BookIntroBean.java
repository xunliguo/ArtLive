package com.example.rh.artlive.bean;

/**
 * Created by rh on 2017/12/20.
 */

public class BookIntroBean {

    private  String name;
    private String id;



    private String content;
    private String img;




    public BookIntroBean(String name, String id,
                    String content, String img) {
        super();
        this.name = name;
        this.id = id;
        this.content = content;
        this.img = img;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "BookBean [name=" + name + ", id="
                + id + ", content=" + content
                + ", img=" + img + "]";
    }



}
