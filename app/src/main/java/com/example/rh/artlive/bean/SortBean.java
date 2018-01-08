package com.example.rh.artlive.bean;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class SortBean implements Parcelable {
//    private String class_name;
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



    public static final Creator<SortBean> CREATOR = new Creator<SortBean>() {
        @Override
        public SortBean createFromParcel(Parcel in) {
            return new SortBean(in);
        }

        @Override
        public SortBean[] newArray(int size) {
            return new SortBean[size];
        }
    };


    private ArrayList<CategoryOneArrayBean> left;
    private ArrayList<CategoryPersonArrayBean> personal;
    private ArrayList<CategoryCateArrayBean> category;
    private ArrayList<CategoryWishLegenArrayBean> legendary;


    public ArrayList<CategoryWishLegenArrayBean> getLegendary() {
        return legendary;
    }

    public void setLegendary(ArrayList<CategoryWishLegenArrayBean> legendary) {
        this.legendary = legendary;
    }



    public ArrayList<CategoryCateArrayBean> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<CategoryCateArrayBean> category) {
        this.category = category;
    }



    public ArrayList<CategoryPersonArrayBean> getPersonal() {
        return personal;
    }

    public void setPersonal(ArrayList<CategoryPersonArrayBean> personal) {
        this.personal = personal;
    }





    protected SortBean(Parcel in) {

        price=in.readString();
        shuliang=in.readString();
    }


    public ArrayList<CategoryOneArrayBean> getCategoryOneArray() {
        return left;
    }

    public void setCategoryOneArray(ArrayList<CategoryOneArrayBean> left) {
        this.left = left;
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
        dest.writeTypedList(left);
        dest.writeTypedList(personal);
        dest.writeTypedList(category);
        dest.writeTypedList(legendary);
    }



    public static class CategoryWishLegenArrayBean implements Parcelable {

        private String name;
        private String img;
        private String sex;
        private String school;
        private String wishimg;

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




        protected CategoryWishLegenArrayBean(Parcel in) {
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

        public static final Creator<CategoryWishLegenArrayBean> CREATOR = new Creator<CategoryWishLegenArrayBean>() {
            @Override
            public CategoryWishLegenArrayBean createFromParcel(Parcel in) {
                return new CategoryWishLegenArrayBean(in);
            }

            @Override
            public CategoryWishLegenArrayBean[] newArray(int size) {
                return new CategoryWishLegenArrayBean[size];
            }
        };

    }




    public static class CategoryCateArrayBean implements Parcelable {


        private String tag_name;


        public String getTag_name() {
            return tag_name;
        }

        public void setTag_name(String tag_name) {
            this.tag_name = tag_name;
        }


        protected CategoryCateArrayBean(Parcel in) {
            tag_name = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

            dest.writeString(tag_name);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<CategoryCateArrayBean> CREATOR = new Creator<CategoryCateArrayBean>() {
            @Override
            public CategoryCateArrayBean createFromParcel(Parcel in) {
                return new CategoryCateArrayBean(in);
            }

            @Override
            public CategoryCateArrayBean[] newArray(int size) {
                return new CategoryCateArrayBean[size];
            }
        };

    }


    public static class CategoryPersonArrayBean implements Parcelable {

        private String user_img;


        protected CategoryPersonArrayBean(Parcel in) {
            user_img = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

            dest.writeString(user_img);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<CategoryPersonArrayBean> CREATOR = new Creator<CategoryPersonArrayBean>() {
            @Override
            public CategoryPersonArrayBean createFromParcel(Parcel in) {
                return new CategoryPersonArrayBean(in);
            }

            @Override
            public CategoryPersonArrayBean[] newArray(int size) {
                return new CategoryPersonArrayBean[size];
            }
        };


        public String getUser_img() {
            return user_img;
        }

        public void setUser_img(String user_img) {
            this.user_img = user_img;
        }

    }


    public static class CategoryOneArrayBean implements Parcelable {
        /**
         * categoryTwoArray : [{"name":"处方药(RX)","imgsrc":"https://121.10.217.171:9002/_ui/desktop/common/cmyy/image/app_tongsufenlei_chufangyao.png","cacode":"chufangyao"},{"name":"非处方(OTC)","imgsrc":"https://121.10.217.171:9002/_ui/desktop/common/cmyy/image/app_tongsufenlei_feichufang.png","cacode":"feichufang"},{"name":"抗生素","imgsrc":"https://121.10.217.171:9002/_ui/desktop/common/cmyy/image/app_tongsufenlei_kangshengsu.png","cacode":"kangshengsu"}]
         * name : 通俗分类
         * imgsrc : https://121.10.217.171:9002/_ui/desktop/common/cmyy/image/app_0.png
         * cacode : tongsufenlei
         */

        private String class_name;
//        private String img_url;
        private String cacode;
        private String tag_name;

        private String name;
        private List<CategoryTwoArrayBean> commodity;

        private List<CategoryAdvertArrayBean> advert;
        private String id;
        private String jiage;
        private String shuliang;
        private String i;

        protected CategoryOneArrayBean(Parcel in) {
            class_name = in.readString();
            id = in.readString();
//            img_url = in.readString();
            cacode = in.readString();
            jiage = in.readString();
            shuliang = in.readString();
            i = in.readString();
            tag_name = in.readString();
            name = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(class_name);
            dest.writeString(id);
//            dest.writeString(img_url);
            dest.writeString(cacode);
            dest.writeString(jiage);
            dest.writeString(shuliang);
            dest.writeString(i);
            dest.writeString(tag_name);
            dest.writeString(name);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<CategoryOneArrayBean> CREATOR = new Creator<CategoryOneArrayBean>() {
            @Override
            public CategoryOneArrayBean createFromParcel(Parcel in) {
                return new CategoryOneArrayBean(in);
            }

            @Override
            public CategoryOneArrayBean[] newArray(int size) {
                return new CategoryOneArrayBean[size];
            }
        };


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }



        public String getTag_name() {
            return tag_name;
        }

        public void setTag_name(String tag_name) {
            this.tag_name = tag_name;
        }

        public String getI() {
            return i;
        }

        public void setI(String i) {
            this.i = i;
        }

        public String getJiage() {
            return jiage;
        }

        public void setJiage(String jiage) {
            this.jiage = jiage;
        }

        public String getShuliang() {
            return shuliang;
        }

        public void setShuliang(String shuliang) {
            this.shuliang = shuliang;
        }

        public String getClass_name() {
            return class_name;
        }

        public void setClass_name(String class_name) {
            this.class_name = class_name;
        }
        public void setId(){
            this.id=id;
        }
        public String getId(){
            return id;
        }

//        public String getImg_url() {
//            return img_url;
//        }
//
//        public void setImg_url(String img_url) {
//            this.img_url = img_url;
//        }

        public String getCacode() {
            return cacode;
        }

        public void setCacode(String cacode) {
            this.cacode = cacode;
        }

        public List<CategoryTwoArrayBean> getCategoryTwoArray() {
            return commodity;
        }

        public void setCategoryTwoArray(List<CategoryTwoArrayBean> commodity) {
            this.commodity = commodity;
        }

        public List<CategoryAdvertArrayBean> getAdvert() {
            return advert;
        }

        public void setAdvert(List<CategoryAdvertArrayBean> advert) {
            this.advert = advert;
        }

        public static class CategoryAdvertArrayBean implements Parcelable {
            /**
             * name : 处方药(RX)
             * imgsrc : https://121.10.217.171:9002/_ui/desktop/common/cmyy/image/app_tongsufenlei_chufangyao.png
             * cacode : chufangyao
             */

            private String title;
            private String img_url;
            private String cacode;
            private String id;
            private String jiage;
            private String shuliang;
            private String i;
            private String img_thumb;





            protected CategoryAdvertArrayBean(Parcel in) {
                title = in.readString();
                id = in.readString();
                img_url = in.readString();
                cacode = in.readString();
                jiage=in.readString();
                shuliang=in.readString();
                i=in.readString();
                img_thumb=in.readString();
            }

            public static final Creator<CategoryTwoArrayBean> CREATOR = new Creator<CategoryTwoArrayBean>() {
                @Override
                public CategoryTwoArrayBean createFromParcel(Parcel in) {
                    return new CategoryTwoArrayBean(in);
                }

                @Override
                public CategoryTwoArrayBean[] newArray(int size) {
                    return new CategoryTwoArrayBean[size];
                }
            };

            public String getImg_thumb() {
                return img_thumb;
            }

            public void setImg_thumb(String img_thumb) {
                this.img_thumb = img_thumb;
            }


            public String getI() {
                return i;
            }

            public void setI(String i) {
                this.i = i;
            }

            public String getJiage() {
                return jiage;
            }

            public void setJiage(String jiage) {
                this.jiage = jiage;
            }

            public String getShuliang() {
                return shuliang;
            }

            public void setShuliang(String shuliang) {
                this.shuliang = shuliang;
            }


            public void setId(){
                this.id=id;
            }
            public String getId(){
                return id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getImg_url() {
                return img_url;
            }

            public void setImg_url(String img_url) {
                this.img_url = img_url;
            }

            public String getCacode() {
                return cacode;
            }

            public void setCacode(String cacode) {
                this.cacode = cacode;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(title);
                dest.writeString(id);
                dest.writeString(img_url);
                dest.writeString(cacode);
                dest.writeString(jiage);
                dest.writeString(shuliang);
                dest.writeString(i);
                dest.writeString(img_thumb);
            }
        }

        public static class CategoryTwoArrayBean implements Parcelable {
            /**
             * name : 处方药(RX)
             * imgsrc : https://121.10.217.171:9002/_ui/desktop/common/cmyy/image/app_tongsufenlei_chufangyao.png
             * cacode : chufangyao
             */

            private String title;
            private String img_url;
            private String cacode;
            private String id;
            private String jiage;
            private String shuliang;
            private String i;

            protected CategoryTwoArrayBean(Parcel in) {
                title = in.readString();
                id = in.readString();
                img_url = in.readString();
                cacode = in.readString();
                jiage=in.readString();
                shuliang=in.readString();
                i=in.readString();
            }

            public static final Creator<CategoryTwoArrayBean> CREATOR = new Creator<CategoryTwoArrayBean>() {
                @Override
                public CategoryTwoArrayBean createFromParcel(Parcel in) {
                    return new CategoryTwoArrayBean(in);
                }

                @Override
                public CategoryTwoArrayBean[] newArray(int size) {
                    return new CategoryTwoArrayBean[size];
                }
            };


            public String getI() {
                return i;
            }

            public void setI(String i) {
                this.i = i;
            }

            public String getJiage() {
                return jiage;
            }

            public void setJiage(String jiage) {
                this.jiage = jiage;
            }

            public String getShuliang() {
                return shuliang;
            }

            public void setShuliang(String shuliang) {
                this.shuliang = shuliang;
            }


            public void setId(){
                this.id=id;
            }
            public String getId(){
                return id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getImg_url() {
                return img_url;
            }

            public void setImg_url(String img_url) {
                this.img_url = img_url;
            }

            public String getCacode() {
                return cacode;
            }

            public void setCacode(String cacode) {
                this.cacode = cacode;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(title);
                dest.writeString(id);
                dest.writeString(img_url);
                dest.writeString(cacode);
                dest.writeString(jiage);
                dest.writeString(shuliang);
                dest.writeString(i);
            }
        }
    }
}
