package ru.kraynov.app.ssaknitu.events.view.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.kraynov.app.ssaknitu.events.R;
import ru.kraynov.app.ssaknitu.events.sdk.api.Api;
import ru.kraynov.app.ssaknitu.events.sdk.api.model.PostModel;
import ru.kraynov.app.ssaknitu.events.sdk.api.model.PostResultModel;
import ru.kraynov.app.ssaknitu.events.util.helper.ShareHelper;
import ru.kraynov.app.ssaknitu.events.view.widget.ScrollDirectionListener;
import ru.kraynov.app.ssaknitu.events.view.widget.TouchyWebView;

public class PostWebFragment extends EvFragment implements View.OnClickListener {

    public static final String ARG_POST_DATA = "POST_DATA";
    public static final String ARG_POST_ID = "POST_ID";

    private PostModel postData;
    private int id = -1;

    @InjectView(R.id.webView)
    TouchyWebView webView;
    @InjectView(R.id.progressBar)
    ProgressBar progressBar;
    @InjectView(R.id.progressBarCenter)
    ProgressBar progressBarCenter;
    @InjectView(R.id.share)
    FloatingActionButton fab_share;
    private WebViewSettings mWebViewSettings;


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.share:
                if (postData!=null && postData.url!=null) ShareHelper.shareText(getActivity(), String.valueOf(Html.fromHtml(postData.title + "<br /><br />" + postData.url)));
                break;
        }
    }

    ScrollDirectionListener scrollDirectionListener = new ScrollDirectionListener() {
        @Override
        public void onScrollUp() {
            fab_share.hide(true);
        }

        @Override
        protected void onScrollDown() {
            fab_share.show(true);
        }
    };

    public static PostWebFragment newInstance(PostModel postData, int id){
        PostWebFragment fragment = new PostWebFragment();
        Bundle bundle = new Bundle();
        if (postData!=null) bundle.putSerializable(ARG_POST_DATA, postData);
        if (id!=-1) bundle.putInt(ARG_POST_ID, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if (getArguments().containsKey(ARG_POST_DATA)) {
            postData = (PostModel) getArguments().getSerializable(ARG_POST_DATA);
        } else if (getArguments().containsKey(ARG_POST_ID)){
            id = getArguments().getInt(ARG_POST_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_web, null);

        ButterKnife.inject(this, view);

        mWebViewSettings = new WebViewSettings(webView);

        actionBar().setDisplayShowTitleEnabled(true);
        actionBar().setHomeButtonEnabled(true);
        actionBar().setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);
        actionBar().setTitle(R.string.post);

        if (postData!=null){
            start();
        } else {
            if (id!=-1){
                Api.Posts().post(id, new Callback<PostResultModel>() {
                    @Override
                    public void success(PostResultModel postResultModel, Response response) {
                        postData = postResultModel.post;

                        if (postData.url!=null) start();
                        else Toast.makeText(getActivity(), getString(R.string.error_load_post),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getActivity(), getString(R.string.error_load_post), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        return view;
    }

    public void start(){
        progressBar.setVisibility(View.VISIBLE);
        progressBarCenter.setVisibility(View.GONE);
        fab_share.setVisibility(View.VISIBLE);
        webView.setVisibility(View.VISIBLE);
        webView.loadUrl(prepareUrl(postData.url));

        scrollDirectionListener.setScrollThreshold(getResources().getDimensionPixelOffset(R.dimen.fab_scroll_threshold));
        webView.setOnScrollChangedCallback(new TouchyWebView.OnScrollChangedCallback() {
            @Override
            public void onScroll(int l, int t, int oldl, int oldt) {
                scrollDirectionListener.onScrollChanged(webView, l, t, oldl, oldt);
            }
        });
        fab_share.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                getActivity().finish();
                break;
        }
        return false;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        webView.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        webView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        webView.destroy();
        webView = null;
    }

    public String prepareUrl(String url){
        return url.contains("?") ?  url + "&device=android" : url + "?device=android";
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        webView.saveState(outState);

        super.onSaveInstanceState(outState);
    }

    class WebViewSettings extends WebChromeClient {
        WebView mWebView;
        WebSettings webSettings;

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        public WebViewSettings(WebView wv){
            mWebView = wv;

            mWebView.setWebViewClient(new ShowImage());
            mWebView.setWebChromeClient(this);
            webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setUserAgentString("Android");
            android.webkit.CookieManager.getInstance().removeAllCookie();
        }
    }

    class ShowImage extends WebViewClient {
        String[] img = {"png","PNG","jpg","JPG","jpeg","JPEG","bmp","BMP"};

        boolean isImage(String url){
            for(String image: img) {
                if (image.equals(url.substring(url.length()-3,url.length()))) return true;
            }

            return false;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.GONE);
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressBar.setVisibility(View.VISIBLE);
        }

    }
}