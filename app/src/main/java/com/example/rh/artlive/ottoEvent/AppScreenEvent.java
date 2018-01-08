package com.example.rh.artlive.ottoEvent;

/**
 * Created by rh on 2017/9/27.
 */

public class AppScreenEvent {


   private String PriceClass;
    private String AgeClass;
    private String LocalClass;


    public String getAgeClass() {
        return AgeClass;
    }

    public void setAgeClass(String ageClass) {
        AgeClass = ageClass;
    }

    public String getLocalClass() {
        return LocalClass;
    }

    public void setLocalClass(String localClass) {
        LocalClass = localClass;
    }


    public String getPriceClass() {
        return PriceClass;
    }

    public void setPriceClass(String priceClass) {
        PriceClass = priceClass;
    }


    public AppScreenEvent(String PriceClass,String AgeClass,String LocalClass) {
        this.PriceClass = PriceClass;
        this.AgeClass = AgeClass;
        this.LocalClass = LocalClass;
    }

}
