package ru.kraynov.app.ssaknitu.events;

import android.os.Bundle;

import ru.kraynov.app.ssaknitu.events.util.helper.PushHelper;
import ru.kraynov.app.ssaknitu.events.view.activity.EvActivity;
import ru.kraynov.app.ssaknitu.events.view.fragment.MainFragment;


public class Main extends EvActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new PushHelper(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new MainFragment(), "main").commit();
    }
}
