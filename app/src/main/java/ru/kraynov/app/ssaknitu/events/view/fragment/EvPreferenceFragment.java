package ru.kraynov.app.ssaknitu.events.view.fragment;

import android.os.Bundle;
import android.support.v4.preference.PreferenceFragment;

import ru.kraynov.app.ssaknitu.events.view.activity.EvActivity;

public class EvPreferenceFragment extends PreferenceFragment{

    public android.support.v7.app.ActionBar actionBar(){
        return ((EvActivity)getActivity()).getSupportActionBar();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getParentFragment()==null) setRetainInstance(true);
    }
}
