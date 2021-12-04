package com.example.dark.prod_family_project.Models;

/**
 * Created by abd on 01-Feb-18.
 */

public class OrdersModels {

    String orderid,username,prod_image,prod_name,status,payment_type,price,address;



    public OrdersModels(String orderid, String username, String prod_image, String prod_name, String status, String payment_type, String price) {
        this.orderid = orderid;
        this.username = username;
        this.prod_image = prod_image;
        this.prod_name = prod_name;
        this.status = status;
        this.payment_type = payment_type;
        this.price = price;
        ;
    }

    public String getOrderid() {
        return orderid;
    }

    public String getUsername() {
        return username;
    }

    public String getProd_image() {
        return prod_image;
    }

    public String getProd_name() {
        return prod_name;
    }

    public String getStatus() {
        return status;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public String getPrice() {
        return price;
    }

    public String getAddress() {
        return address;
    }
}
