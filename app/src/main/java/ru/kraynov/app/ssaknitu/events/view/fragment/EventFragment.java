package ru.kraynov.app.ssaknitu.events.view.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.manuelpeinado.fadingactionbar.view.ObservableScrollView;
import com.manuelpeinado.fadingactionbar.view.OnScrollChangedCallback;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.kraynov.app.ssaknitu.events.R;
import ru.kraynov.app.ssaknitu.events.sdk.api.Api;
import ru.kraynov.app.ssaknitu.events.sdk.api.model.EventModel;

public class EventFragment extends EvFragment implements OnScrollChangedCallback {
    public static final String ARG_EVENT_DATA = "ARG_EVENT_DATA";
    public static final String ARG_EVENT_ID = "ARG_EVENT_ID";

    private EventModel eventData;
    private int id;

    @InjectView(R.id.header) View mHeader;
    @InjectView(R.id.title) TextView tv_title;
    @InjectView(R.id.date) TextView tv_date;
    @InjectView(R.id.organisation) TextView tv_organisation;
    @InjectView(R.id.time) TextView tv_time;
    @InjectView(R.id.address) TextView tv_address;
    @InjectView(R.id.description) TextView tv_description;
    @InjectView(R.id.scrollview) ObservableScrollView osv_container;
    @InjectView(R.id.progressBarCenter) ProgressBar pb_progress;
    private Drawable mActionBarBackgroundDrawable;
    private int mLastDampedScroll;
    private int mInitialStatusBarColor;
    private int mFinalStatusBarColor;
    private SystemBarTintManager mStatusBarManager;

    /* FRAGMENT LIFECYCLE */

    public static Fragment newInstance(EventModel event, int id) {
        EventFragment mFragment = new EventFragment();
        Bundle bundle = new Bundle();
        if (event!=null) bundle.putSerializable(ARG_EVENT_DATA, event);
        if (id!=-1) bundle.putInt(ARG_EVENT_ID, id);
        return mFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        if (getArguments().containsKey(ARG_EVENT_DATA)) {
            eventData = (EventModel) getArguments().getSerializable(ARG_EVENT_DATA);
        } else if (getArguments().containsKey(ARG_EVENT_ID)){
            id = getArguments().getInt(ARG_EVENT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, null);
        ButterKnife.inject(this, view);
        actionBar().setTitle(getString(R.string.event));
        actionBar().setHomeButtonEnabled(true);
        actionBar().setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);

        if (eventData!=null){
            start();
        } else {
            if (id!=-1){
                Api.Events().events(id, new Callback<EventModel>() {
                    @Override
                    public void success(EventModel eventModel, Response response) {
                        eventData = eventModel;

                        if (eventData!=null) start();
                        else Toast.makeText(getActivity(), getString(R.string.error_load_event), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getActivity(), getString(R.string.error_load_event), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        return view;
    }

    private void start() {
        osv_container.setVisibility(View.VISIBLE);
        pb_progress.setVisibility(View.GONE);

        mActionBarBackgroundDrawable = new ColorDrawable(getResources().getColor(R.color.material_red_500));
        mStatusBarManager = new SystemBarTintManager(getActivity());
        mStatusBarManager.setStatusBarTintEnabled(true);
        mInitialStatusBarColor = Color.BLACK;
        mFinalStatusBarColor = getResources().getColor(R.color.material_red_500);
        osv_container.setOnScrollChangedCallback(this);
        onScroll(-1, 0);

        String date = eventData.date;
        try {
            date = new SimpleDateFormat("dd.MM.yyyy").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(eventData.date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tv_title.setText(eventData.title);
        tv_date.setText(date);
        tv_organisation.setText(eventData.organisation_name);
        tv_time.setText(eventData.time_start + " - " + eventData.time_end);
        tv_address.setText(eventData.address);
        tv_description.setText(Html.fromHtml(eventData.description));
        Picasso.with(getActivity()).load("http://" + eventData.event_cover).into(((ImageView) mHeader));
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
    public void onDestroyView() {
        super.onDestroyView();

        updateActionBarTransparency(1);
        updateStatusBarColor(1);
    }

    /* FRAGMENT LIFECYCLE END */

    @Override
    public void onScroll(int l, int scrollPosition) {
        int headerHeight = mHeader.getHeight() - actionBar().getHeight();
        float ratio = 0;
        if (scrollPosition > 0 && headerHeight > 0)
            ratio = (float) Math.min(Math.max(scrollPosition, 0), headerHeight) / headerHeight;

        updateActionBarTransparency(ratio);
        updateStatusBarColor(ratio);
        updateParallaxEffect(scrollPosition);
    }

    private void updateActionBarTransparency(float scrollRatio) {
        if (mActionBarBackgroundDrawable!=null && actionBar()!=null){
            int newAlpha = (int) (scrollRatio * 255);
            mActionBarBackgroundDrawable.setAlpha(newAlpha);
            actionBar().setBackgroundDrawable(mActionBarBackgroundDrawable);
        }
    }

    private void updateStatusBarColor(float scrollRatio) {
        if (mStatusBarManager!=null){
            int r = interpolate(Color.red(mInitialStatusBarColor), Color.red(mFinalStatusBarColor), 1 - scrollRatio);
            int g = interpolate(Color.green(mInitialStatusBarColor), Color.green(mFinalStatusBarColor), 1 - scrollRatio);
            int b = interpolate(Color.blue(mInitialStatusBarColor), Color.blue(mFinalStatusBarColor), 1 - scrollRatio);
            mStatusBarManager.setTintColor(Color.rgb(r, g, b));
        }
    }

    private void updateParallaxEffect(int scrollPosition) {
        float damping = 0.5f;
        int dampedScroll = (int) (scrollPosition * damping);
        int offset = mLastDampedScroll - dampedScroll;
        mHeader.offsetTopAndBottom(-offset);

        mLastDampedScroll = dampedScroll;
    }

    private int interpolate(int from, int to, float param) {
        return (int) (from * param + to * (1 - param));
    }
}
