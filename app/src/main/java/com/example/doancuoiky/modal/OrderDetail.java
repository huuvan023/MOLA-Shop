package com.example.doancuoiky.modal;

import java.io.Serializable;

public class OrderDetail implements Serializable {

    private String id_bill;
    private String id_product;
    private String quanlity;

    public OrderDetail(String id_bill, String id_product, String quanlity) {
        this.id_bill = id_bill;
        this.id_product = id_product;
        this.quanlity = quanlity;
    }

    public String getId_bill() {
        return id_bill;
    }

    public void setId_bill(String id_bill) {
        this.id_bill = id_bill;
    }

    public String getId_product() {
        return id_product;
    }

    public void setId_product(String id_product) {
        this.id_product = id_product;
    }

    public String getQuanlity() {
        return quanlity;
    }

    public void setQuanlity(String quanlity) {
        this.quanlity = quanlity;
    }
}
