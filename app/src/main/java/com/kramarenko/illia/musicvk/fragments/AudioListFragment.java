package com.kramarenko.illia.musicvk.fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;

import com.kramarenko.illia.musicvk.ops.AudioItem;
import com.kramarenko.illia.musicvk.ops.AudioItemsAdapter;


public class AudioListFragment extends ListFragment {


    private AudioItemsAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AudioListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new AudioItemsAdapter(getActivity().getApplicationContext());
        setListAdapter(adapter);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public void addItems(AudioItem[] items){
        for (AudioItem anA : items) {
            adapter.add(anA);
        }
    }



}
