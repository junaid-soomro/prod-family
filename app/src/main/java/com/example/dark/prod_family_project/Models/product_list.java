package com.example.dark.prod_family_project.Models;

/**
 * Created by abd on 19-Jan-18.
 */

public class product_list {

    String ID,name,price,category,image,selected_category;

    public product_list(String ID, String name, String price, String category,String image) {
        this.ID = ID;
        this.name = name;
        this.price = price;
        this.category = category;
        this.image = image;
    }

    public product_list(String name,String category){

        this.name = name;
        this.category = category;

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public product_list(String selected_category){

        this.selected_category = selected_category;

    }





    public String getName() {
        return name;
    }

    public String getSelected_category() {
        return selected_category;
    }

    public void setSelected_category(String selected_category) {
        this.selected_category = selected_category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
