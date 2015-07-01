package com.kramarenko.illia.musicvk.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kramarenko.illia.musicvk.R;
import com.kramarenko.illia.musicvk.activities.MainActivity;
import com.vk.sdk.VKSdk;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLogout extends Fragment implements View.OnClickListener{

    protected final String TAG = getClass().getSimpleName();

    private Button logIn;
    private String[] sMyScope;

    public FragmentLogout() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle args = getArguments();
        sMyScope = args.getStringArray(MainActivity.SCOPE_KEY);
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_logout, container, false);

        logIn = (Button) v.findViewById(R.id.log_in);
        logIn.setOnClickListener(this);
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
        VKSdk.authorize(sMyScope, true, false);
    }


}
