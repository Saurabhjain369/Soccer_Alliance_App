package com.example.soccerallianceapp;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.soccer_alliance_project_test.R;


/**
 * A simple {@link Fragment} subclass.
 *
 * forgot password with email instead of OTP.
 */
public class forgot_pass3_Fragment extends Fragment {


    public forgot_pass3_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_pass3, container, false);
    }

}
