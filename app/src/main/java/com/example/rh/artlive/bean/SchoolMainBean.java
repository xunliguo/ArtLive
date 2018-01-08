package com.example.rh.artlive.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rh on 2017/12/25.
 */

public class SchoolMainBean implements Parcelable {

    private String class_name;
//    private String tag;
//    private boolean isTitle;
//    private String img_url;

    //    private String title;
//    private String id;
    private String price;
    private String shuliang;


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getShuliang() {
        return shuliang;
    }

    public void setShuliang(String shuliang) {
        this.shuliang = shuliang;
    }


    public static final Creator<SchoolMainBean> CREATOR = new Creator<SchoolMainBean>() {
        @Override
        public SchoolMainBean createFromParcel(Parcel in) {
            return new SchoolMainBean(in);
        }

        @Override
        public SchoolMainBean[] newArray(int size) {
            return new SchoolMainBean[size];
        }
    };
    private ArrayList<SchoolMainBean.CategoryMajorArrayBean> major;


    public ArrayList<CategoryMajorArrayBean> getMajor() {
        return major;
    }

    public void setMajor(ArrayList<CategoryMajorArrayBean> major) {
        this.major = major;
    }




    protected SchoolMainBean(Parcel in) {

        price=in.readString();
        shuliang=in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(class_name);
//        dest.writeString(id);
//        dest.writeString(tag);
//        dest.writeByte((byte) (isTitle ? 1 : 0));
        dest.writeString(price);
        dest.writeString(shuliang);
        dest.writeTypedList(major);
    }




    public static class CategoryMajorArrayBean implements Parcelable {

        private String name;
        private String img;
        private String sex;
        private String school;
        private String wishimg;
        private String major_content;

        public String getMajor_content() {
            return major_content;
        }

        public void setMajor_content(String major_content) {
            this.major_content = major_content;
        }


        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getSchool() {
            return school;
        }

        public void setSchool(String school) {
            this.school = school;
        }

        public String getWishimg() {
            return wishimg;
        }

        public void setWishimg(String wishimg) {
            this.wishimg = wishimg;
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }




        protected CategoryMajorArrayBean(Parcel in) {
            name = in.readString();
            img = in.readString();
            sex = in.readString();
            school = in.readString();
            wishimg = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

            dest.writeString(name);
            dest.writeString(img);
            dest.writeString(sex);
            dest.writeString(school);
            dest.writeString(wishimg);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<SchoolMainBean.CategoryMajorArrayBean> CREATOR = new Creator<SchoolMainBean.CategoryMajorArrayBean>() {
            @Override
            public SchoolMainBean.CategoryMajorArrayBean createFromParcel(Parcel in) {
                return new SchoolMainBean.CategoryMajorArrayBean(in);
            }

            @Override
            public SchoolMainBean.CategoryMajorArrayBean[] newArray(int size) {
                return new SchoolMainBean.CategoryMajorArrayBean[size];
            }
        };
    }
}
