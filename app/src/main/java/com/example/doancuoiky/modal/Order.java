package com.example.doancuoiky.modal;

public class Order {
    String id_bill_order;
    String date_order;
    int order_status;
    int total;

    public Order(String id_bill_order, String date_order, int order_status, int total) {
        this.id_bill_order = id_bill_order;
        this.date_order = date_order;
        this.order_status = order_status;
        this.total = total;
    }

    public String getId_bill_order() {
        return id_bill_order;
    }

    public void setId_bill_order(String id_bill_order) {
        this.id_bill_order = id_bill_order;
    }

    public String getDate_order() {
        return date_order;
    }

    public void setDate_order(String date_order) {
        this.date_order = date_order;
    }

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
