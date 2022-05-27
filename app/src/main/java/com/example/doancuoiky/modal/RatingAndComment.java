package com.example.doancuoiky.modal;

public class RatingAndComment {
    String id_user;
    String id_product;
    int rate;
    String ratedate;
    String cmt;

    public RatingAndComment(String id_user, String id_product, int rate, String ratedate, String cmt) {
        this.id_user = id_user;
        this.id_product = id_product;
        this.rate = rate;
        this.ratedate = ratedate;
        this.cmt = cmt;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_product() {
        return id_product;
    }

    public void setId_product(String id_product) {
        this.id_product = id_product;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getRatedate() {
        return ratedate;
    }

    public void setRatedate(String ratedate) {
        this.ratedate = ratedate;
    }

    public String getCmt() {
        return cmt;
    }

    public void setCmt(String cmt) {
        this.cmt = cmt;
    }
}
