package ru.kraynov.app.ssaknitu.events.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import ru.kraynov.app.ssaknitu.events.R;
import ru.kraynov.app.ssaknitu.events.sdk.api.model.PostModel;
import ru.kraynov.app.ssaknitu.events.view.fragment.EventFragment;
import ru.kraynov.app.ssaknitu.events.view.fragment.EventsFragment;
import ru.kraynov.app.ssaknitu.events.view.fragment.PostWebFragment;
import ru.kraynov.app.ssaknitu.events.view.fragment.PreferencesFragment;
import ru.kraynov.app.ssaknitu.events.view.fragment.SettingsPushEventsFragment;
import ru.kraynov.app.ssaknitu.events.view.fragment.SettingsPushNewsFragment;

public class EvFragmentContainerActivity extends EvActivity {

    public static final String ARG_FRAGMENT_ID = "ARG_F_ID";
    private int fragmentId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentId = getIntent().getIntExtra(ARG_FRAGMENT_ID,-1);
        if (fragmentId!=-1){
            Fragment fragment = null;

            switch (fragmentId){
                case 0:
                    fragment = EventsFragment.newInstance();
                    break;
                case 1:
                    fragment = EventFragment.newInstance((ru.kraynov.app.ssaknitu.events.sdk.api.model.EventModel) getIntent().getSerializableExtra(EventFragment.ARG_EVENT_DATA), getIntent().getIntExtra(EventFragment.ARG_EVENT_ID, -1));
                    break;
                case 2:
                    fragment = PostWebFragment.newInstance((PostModel) getIntent().getSerializableExtra(PostWebFragment.ARG_POST_DATA),  getIntent().getIntExtra(PostWebFragment.ARG_POST_ID, -1));
                    break;
                case 3:
                    fragment = new PreferencesFragment();
                    break;
                case 4:
                    fragment = new SettingsPushEventsFragment();
                    break;
                case 5:
                    fragment = new SettingsPushNewsFragment();
                    break;
            }

            if (getIntent().getExtras().size()>1) fragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, fragmentId+"").commit();
        } else throw new IllegalArgumentException("fragmentId must be more than -1");
    }
}
