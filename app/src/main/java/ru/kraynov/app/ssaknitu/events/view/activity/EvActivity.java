package ru.kraynov.app.ssaknitu.events.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import ru.kraynov.app.ssaknitu.events.R;
import ru.kraynov.app.ssaknitu.events.sdk.util.UIHelper;

public class EvActivity extends ActionBarActivity {

    public static final String ARG_IS_FADING = "ARG_IS_FADING";
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getBooleanExtra(ARG_IS_FADING, false)) setContentView(R.layout.activity_fragment_container_with_fading);
        else setContentView(R.layout.activity_fragment_container);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        UIHelper.onCreate(this);
        Fabric.with(this, new Crashlytics());
    }

    @Override
    protected void onResume() {
        super.onResume();

        UIHelper.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UIHelper.onDestroy(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        UIHelper.onActivityResult(this, requestCode, resultCode, data);
    }
}
