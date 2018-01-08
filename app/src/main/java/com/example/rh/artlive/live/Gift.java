package com.example.rh.artlive.live;

import com.example.rh.artlive.articlechoice.BaseBean;
import com.example.rh.artlive.articlechoice.QuestionOptionBean;

import java.util.List;

/**
 * Created by WZH on 2016/12/25.
 */

public class Gift extends BaseBean{
    public String gift_id ;

    public String title ;
    public String price ;
    public int num ;
    public String poster ;



    public Gift() {
        super();
    }


    public String getGift_id() {
        return gift_id;
    }

    public void setGift_id(String gift_id) {
        this.gift_id = gift_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }




    public Gift(String gift_id, String title,String price, String poster) {
        super();
        this.gift_id = gift_id;
        this.title = title;
        this.price = price;
        this.poster = poster;
    }


    @Override
    public String toString() {
        return "Gift [gift_id=" + gift_id + ", title="
                + title + ", price=" + price
                + ", poster=" + poster + "]";
    }

}
