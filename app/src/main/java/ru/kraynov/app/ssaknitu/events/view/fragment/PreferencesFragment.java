package ru.kraynov.app.ssaknitu.events.view.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ru.kraynov.app.ssaknitu.events.R;
import ru.kraynov.app.ssaknitu.events.util.helper.DataLoadingHelper;
import ru.kraynov.app.ssaknitu.events.util.helper.SharedPreferencesHelper;
import ru.kraynov.app.ssaknitu.events.view.activity.EvFragmentContainerActivity;

public class PreferencesFragment extends EvPreferenceFragment implements Preference.OnPreferenceClickListener{

    private Preference push_events_orgs_customize;
    private Preference push_news_orgs_customize;
    private Preference push_events_enable;
    private Preference push_posts_enable;
    private Preference push_news_enable;
    private Preference information_events;
    private Preference information_ssaknitu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {

        actionBar().setDisplayShowTitleEnabled(true);
        actionBar().setTitle(R.string.settings);
        actionBar().setDisplayHomeAsUpEnabled(true);
        actionBar().setHomeButtonEnabled(true);
        addPreferencesFromResource(R.xml.preferences);

        push_events_orgs_customize = findPreference("push_events_orgs_customize");
        push_news_orgs_customize = findPreference("push_news_orgs_customize");

        push_events_enable = findPreference("PUSH_EVENTS_ENABLE");
        push_posts_enable = findPreference("PUSH_POSTS_ENABLE");
        push_news_enable = findPreference("PUSH_NEWS_ENABLE");
        information_events = findPreference("information_events");
        information_ssaknitu = findPreference("information_ssaknitu");

        push_events_orgs_customize.setOnPreferenceClickListener(this);
        push_news_orgs_customize.setOnPreferenceClickListener(this);
        information_ssaknitu.setOnPreferenceClickListener(this);
        information_events.setOnPreferenceClickListener(this);

        return super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                getActivity().finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        SharedPreferencesHelper.getInstance().notifyDataSetChanged();
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()){
            case "push_events_orgs_customize":
                if (DataLoadingHelper.isConnected(getActivity())) {
                    getActivity().startActivity(
                            new Intent(getActivity(), EvFragmentContainerActivity.class)
                            .putExtra(EvFragmentContainerActivity.ARG_FRAGMENT_ID, 4)
                    );
                } else Toast.makeText(getActivity(), R.string.no_connection, Toast.LENGTH_SHORT).show();
                break;
            case "push_news_orgs_customize":
                if (DataLoadingHelper.isConnected(getActivity())) {
                    getActivity().startActivity(
                            new Intent(getActivity(), EvFragmentContainerActivity.class)
                                    .putExtra(EvFragmentContainerActivity.ARG_FRAGMENT_ID, 5)
                    );
                } else Toast.makeText(getActivity(), R.string.no_connection, Toast.LENGTH_SHORT).show();
                break;
            case "information_events":
                startActivity(
                        new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://events.ssaknitu.ru"))
                );
                break;
            case "information_ssaknitu":
                startActivity(
                        new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://ssaknitu.ru"))
                );
                break;
        }

        return false;
    }
}
