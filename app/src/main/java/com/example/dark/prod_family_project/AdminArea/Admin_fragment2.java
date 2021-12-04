package com.example.dark.prod_family_project.AdminArea;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dark.prod_family_project.R;

/**
 * Created by abd on 18-Jan-18.
 */

public class Admin_fragment2 extends Fragment {

    private static final String TAG="delete_prod";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.delete_prod,container,false);

        return view;
    }
}
