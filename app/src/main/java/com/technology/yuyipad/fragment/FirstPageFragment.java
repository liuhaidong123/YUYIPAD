package com.technology.yuyipad.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.technology.yuyipad.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstPageFragment extends Fragment {
    private RelativeLayout mTitle_Rl;

    public FirstPageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first_page, container, false);
        return view;
    }
    /**
     * 初始化UI
     * @param view
     */
}
