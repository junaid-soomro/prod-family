package com.example.dark.prod_family_project.Holder;

import com.example.dark.prod_family_project.Models.all_categories;

import java.util.ArrayList;

/**
 * Created by abd on 19-Jan-18.
 */

public class all_holders {

    public int Amount;

    public ArrayList<all_categories> categ = new ArrayList<>();

    public ArrayList<all_categories> all_holders() {
        return categ;
    }

    public all_holders(ArrayList<all_categories> categ) {
        this.categ = categ;
    }

    public void set_amount(int amount) {
        Amount = amount;
    }

    public int getAmount() {
        return Amount;
    }
}
