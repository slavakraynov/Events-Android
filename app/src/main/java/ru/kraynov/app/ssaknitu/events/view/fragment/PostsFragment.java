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
import ru.kraynov.app.ssaknitu.events.sdk.api.model.PostModel;
import ru.kraynov.app.ssaknitu.events.sdk.api.model.PostsResultModel;
import ru.kraynov.app.ssaknitu.events.util.helper.DataLoadingHelper;
import ru.kraynov.app.ssaknitu.events.view.adapter.PostsAdapter;

public class PostsFragment extends EvFragment implements SwipeRefreshLayout.OnRefreshListener {
    @InjectView(android.R.id.list) RecyclerView mRecyclerView;
    @InjectView(R.id.refresh) SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLayoutManager;
    private PostsAdapter mAdapter;
    private LoadPosts loadPosts;

    /* FRAGMENT LIFECYCLE */

    public static Fragment newInstance() {
        PostsFragment mFragment = new PostsFragment();
        return mFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts, null);
        ButterKnife.inject(this,view);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        loadPosts = new LoadPosts();
        loadPosts.execute(true);
        return view;
    }

    /* FRAGMENT LIFECYCLE END */

    @Override
    public void onRefresh() {
        loadPosts.execute(true);
    }

    public class LoadPosts implements Callback<PostsResultModel>{
        int count = 20;
        int page = 1;
        ArrayList<PostModel> items = new ArrayList<>();
        boolean isLoading = false;
        boolean mayLoading = true;

        public void execute(boolean isRefresh){
            if (isRefresh){
                page = 1;
                mayLoading = true;
                items.clear();

                mSwipeRefreshLayout.post(new Runnable() {
                    @Override public void run() {
                        mSwipeRefreshLayout.setRefreshing(true);
                    }
                });
            }

            isLoading = true;

            Api.Posts().posts(count, page, this);
        }

        @Override
        public void success(PostsResultModel postsModel, Response response) {
            if (getView()!=null){
                items.addAll(postsModel.posts);
                if (mAdapter==null) {
                    mAdapter = new PostsAdapter(getActivity(), items);
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            if (!loadPosts.isLoading && loadPosts.mayLoading && mLayoutManager.findLastVisibleItemPosition()+5>mLayoutManager.getItemCount()){
                                if (DataLoadingHelper.isOnline(getActivity())) loadPosts.execute(false);
                            }
                        }
                    });
                } else {
                    mAdapter.updateItems(items);
                }
            }

            if (page+1<=postsModel.pages) {
                page++;
                mayLoading = true;
            } else mayLoading = false;

            isLoading = false;

            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        }

        @Override
        public void failure(RetrofitError error) {
            mayLoading = false;
            isLoading = false;

            mSwipeRefreshLayout.post(new Runnable() {
                @Override public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    }
}
