package com.technology.yuyipad.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.technology.yuyipad.R;
import com.technology.yuyipad.lzhUtils.StateBarUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeasureFragment extends Fragment {


    public MeasureFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_measure, container, false);
        return view;
    }

}
