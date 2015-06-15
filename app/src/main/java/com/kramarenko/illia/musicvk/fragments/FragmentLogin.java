package com.kramarenko.illia.musicvk.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kramarenko.illia.musicvk.R;
import com.kramarenko.illia.musicvk.activities.LoginActivity;
import com.vk.sdk.VKSdk;


public class FragmentLogin extends Fragment implements View.OnClickListener{


    private Button goDeeper;
    private Button logOut;

    public FragmentLogin() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        goDeeper = (Button) v.findViewById(R.id.go_deeper);
        logOut = (Button) v.findViewById(R.id.logout);
        logOut.setOnClickListener(this);
        goDeeper.setOnClickListener(this);
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.go_deeper:
                ((LoginActivity) getActivity()).startMainActivity();
                break;
            case R.id.logout:
                VKSdk.logout();
                if (!VKSdk.isLoggedIn()) {
                    ((LoginActivity)getActivity()).showLogout();
                }
                break;
        }
    }
}
