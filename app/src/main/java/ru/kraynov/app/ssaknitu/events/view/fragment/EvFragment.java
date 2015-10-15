package ru.kraynov.app.ssaknitu.events.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import ru.kraynov.app.ssaknitu.events.view.activity.EvActivity;

public class EvFragment extends Fragment{

    public android.support.v7.app.ActionBar actionBar(){
        return ((EvActivity)getActivity()).getSupportActionBar();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getParentFragment()==null) setRetainInstance(true);
    }
}
