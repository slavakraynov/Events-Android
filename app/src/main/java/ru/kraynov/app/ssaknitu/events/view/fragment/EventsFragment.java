package ru.kraynov.app.ssaknitu.events.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.kraynov.app.ssaknitu.events.R;
import ru.kraynov.app.ssaknitu.events.sdk.api.Api;
import ru.kraynov.app.ssaknitu.events.sdk.api.model.EventModel;
import ru.kraynov.app.ssaknitu.events.view.adapter.EventsAdapter;

import static ru.kraynov.app.ssaknitu.events.util.Logger.log;

public class EventsFragment extends EvFragment implements SwipeRefreshLayout.OnRefreshListener {
    @InjectView(android.R.id.list) RecyclerView mRecyclerView;
    @InjectView(R.id.refresh) SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLayoutManager;
    private EventsAdapter mAdapter;

    /* FRAGMENT LIFECYCLE */

    public static Fragment newInstance() {
        EventsFragment mFragment = new EventsFragment();
        return mFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, null);
        ButterKnife.inject(this,view);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        start();
        return view;
    }

    /* FRAGMENT LIFECYCLE END */

    private void start() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        Api.Events().events(new Callback<ArrayList<EventModel>>() {
            @Override
            public void success(ArrayList<EventModel> eventModels, Response response) {
                if (mAdapter==null) {
                    mAdapter = new EventsAdapter(getActivity(), eventModels);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    mAdapter.updateItems(eventModels);
                }

                mSwipeRefreshLayout.post(new Runnable() {
                    @Override public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                log(error.getLocalizedMessage());

                mSwipeRefreshLayout.post(new Runnable() {
                    @Override public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    @Override
    public void onRefresh() {
        start();
    }
}
