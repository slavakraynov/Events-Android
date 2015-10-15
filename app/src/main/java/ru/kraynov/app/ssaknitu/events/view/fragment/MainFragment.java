package ru.kraynov.app.ssaknitu.events.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

import ru.kraynov.app.ssaknitu.events.R;
import ru.kraynov.app.ssaknitu.events.view.activity.EvFragmentContainerActivity;

public class MainFragment extends EvFragment{
    //Classes
    //Adapters
    PagerAdapter mPagerAdapter;
    //Views
    ViewPager mViewPager;
    PagerSlidingTabStrip tabs;

    public static final String TAG_TAB_POSITION = "tab_position";
    private OnPageChangeListener mPageChangeListener;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (int i=0;i<mPagerAdapter.getRegisteredFragments().size();i++){
            if (mPagerAdapter.getRegisteredFragment(i)!=null) mPagerAdapter.getRegisteredFragment(i).onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null);
        mPageChangeListener = new OnPageChangeListener();

        actionBar().setTitle(getString(R.string.app_name));

        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);

        mPagerAdapter = new PagerAdapter(getChildFragmentManager());

        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOnPageChangeListener(mPageChangeListener);
        mViewPager.setOffscreenPageLimit(mPagerAdapter.getCount());

        tabs.setViewPager(mViewPager);
        tabs.setOnPageChangeListener(mPageChangeListener);

        if (savedInstanceState!=null){
            mViewPager.setCurrentItem(savedInstanceState.getInt(TAG_TAB_POSITION), true);
            mViewPager.post(new Runnable() {
                @Override
                public void run() {
                    mPageChangeListener.onPageSelected(savedInstanceState.getInt(TAG_TAB_POSITION));
                }
            });
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0,0,0, R.string.settings).setIcon(R.drawable.ic_settings_white_48dp).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case 0:
                getActivity().startActivity(
                        new Intent(getActivity(), EvFragmentContainerActivity.class)
                                .putExtra(EvFragmentContainerActivity.ARG_FRAGMENT_ID, 3)
                );

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(TAG_TAB_POSITION, mViewPager.getCurrentItem());
        super.onSaveInstanceState(outState);
    }

    public class PagerAdapter extends FragmentPagerAdapter {
        SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //registeredFragments.remove(position);
            //super.destroyItem(container, position, object);
        }

        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }

        public SparseArray<Fragment> getRegisteredFragments() {
            return registeredFragments;
        }

        private final String[] TITLES = getResources().getStringArray(R.array.main_tab_titles);

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new EventsFragment();
                case 1:
                    return new PostsFragment();
            }

            return null;
        }
    }

    class OnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int i, float v, int i2) {}

        @Override
        public void onPageSelected(int i){

        }

        @Override
        public void onPageScrollStateChanged(int i) {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
