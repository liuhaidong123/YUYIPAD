package com.technology.yuyipad.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.technology.yuyipad.R;
import com.technology.yuyipad.activity.CurrentBloodActivity;
import com.technology.yuyipad.activity.CurrentTemActivity;
import com.technology.yuyipad.lzhUtils.StateBarUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeasureFragment extends Fragment {
    private RelativeLayout mBloodRL, mTemRL;

    public MeasureFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_measure, container, false);

        mBloodRL = view.findViewById(R.id.blood_rl);
        mBloodRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CurrentBloodActivity.class));
            }
        });
        mTemRL = view.findViewById(R.id.tem_rl);
        mTemRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CurrentTemActivity.class));
            }
        });
        return view;
    }


}
