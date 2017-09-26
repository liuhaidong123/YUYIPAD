package com.technology.yuyipad.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.technology.yuyipad.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstPageFragment extends Fragment {


    public FirstPageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_first_page, container, false);
    }

}
