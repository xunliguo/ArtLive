package com.example.rh.artlive.live;

/**
 * Created by rh on 2017/12/18.
 */

public class LiveGiftBean {


    public String gift_id ;

    public String title ;
    public String price ;
    public int num ;
    public String poster ;



    public LiveGiftBean() {
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




    public LiveGiftBean(String gift_id, String title,String price, String poster) {
        super();
        this.gift_id = gift_id;
        this.title = title;
        this.price = price;
        this.poster = poster;
    }


    @Override
    public String toString() {
        return "LiveGiftBean [gift_id=" + gift_id + ", title="
                + title + ", price=" + price
                + ", poster=" + poster + "]";
    }

}
