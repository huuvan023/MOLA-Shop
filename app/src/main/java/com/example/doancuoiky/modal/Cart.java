package com.example.doancuoiky.modal;

import android.content.Intent;

import java.lang.ref.SoftReference;

import javax.xml.namespace.QName;

public class Cart {

    private String ID;
    private String TypeID;
    private String name;
    private String description;
    private int price;
    private String cartProductImg;
    private int sale;
    private int count;

    public int getSale() {
        return sale;
    }

    public void setSale(int sale) {
        this.sale = sale;
    }

    public Cart(String ID, String typeID, String name, String description,
                int price, String cartProductImg, int sale, int count) {
        this.ID = ID;
        TypeID = typeID;
        this.name = name;
        this.description = description;
        this.price = price;
        this.cartProductImg = cartProductImg;
        this.count = count;
        this.sale = sale;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTypeID() {
        return TypeID;
    }

    public void setTypeID(String typeID) {
        TypeID = typeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCartProductImg() {
        return cartProductImg;
    }

    public void setCartProductImg(String cartProductImg) {
        this.cartProductImg = cartProductImg;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
