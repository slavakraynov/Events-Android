package ru.kraynov.app.ssaknitu.events.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.kraynov.app.ssaknitu.events.R;
import ru.kraynov.app.ssaknitu.events.sdk.api.Api;
import ru.kraynov.app.ssaknitu.events.sdk.api.model.OrganisationModel;
import ru.kraynov.app.ssaknitu.events.util.helper.SharedPreferencesHelper;
import ru.kraynov.app.ssaknitu.events.view.adapter.SettingsPushAdapter;

/**
 * Редактирование списка источников у категории новостей
 */
public class SettingsPushNewsFragment extends EvFragment {
    @InjectView(android.R.id.list) RecyclerView rv_list;
    @InjectView(R.id.progressBar) ProgressBar pb_progress;
    LinearLayoutManager linearLayoutManager;

    View view;
    private SettingsPushAdapter pushNewsAdapter;
    private ArrayList<Integer> includedOrganisations;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        actionBar().setDisplayShowTitleEnabled(true);
        actionBar().setTitle(R.string.organisations);
        actionBar().setHomeButtonEnabled(true);
        actionBar().setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);

        view = inflater.inflate(R.layout.fragment_settings_push_events, null);
        ButterKnife.inject(this,view);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        rv_list.setLayoutManager(linearLayoutManager);
        rv_list.setHasFixedSize(true);

        includedOrganisations = new Gson().fromJson(SharedPreferencesHelper.getInstance().getString(SharedPreferencesHelper.PREFS.APP_PUSH_NEWS_ORGS), new TypeToken<ArrayList<Integer>>(){}.getType());

        new LoadOrganisations().execute();
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                getActivity().finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void savePushOrganisations() {
        if (pushNewsAdapter !=null){
            SharedPreferencesHelper.getInstance().set(
                    SharedPreferencesHelper.PREFS.APP_PUSH_NEWS_ORGS,
                    new Gson().toJson(
                            pushNewsAdapter.getActiveSources(),
                            new TypeToken<ArrayList<Integer>>() {
                            }.getType()
                    )
            );
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);

        savePushOrganisations();
    }

    class LoadOrganisations implements Callback<ArrayList<OrganisationModel>> {

        public void execute() {
            Api.Organisations().organisationsId(this);
        }

        @Override
        public void success(ArrayList<OrganisationModel> organisationModels, Response response) {
            if (getView()!=null){
                pushNewsAdapter = new SettingsPushAdapter(getActivity(),organisationModels, includedOrganisations);
                rv_list.setVisibility(View.VISIBLE);
                rv_list.setAdapter(pushNewsAdapter);
            }
        }

        @Override
        public void failure(RetrofitError error) {

        }

    }
}

