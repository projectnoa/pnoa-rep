package com.screwattack.sgcapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;
import com.screwattack.sgcapp.fragments.LoginFragment;

import java.util.Locale;

public class SplashActivity extends ParentActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    public static final String FIRST_RUN_PARAM = "SGC_FIRST_RUN_PARAM";

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        getScheduleData(false, true);
        getAutorsData(false, true);
        getInfoData(false, true);

        //if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
            setActionBar();
        //}

        if (getSharedPreferences("SGC_FIRST_START", MODE_PRIVATE).getBoolean(FIRST_RUN_PARAM, true)) {
            getSharedPreferences("SGC_FIRST_START", MODE_PRIVATE).edit().putBoolean(FIRST_RUN_PARAM, false).commit();
            // Create the adapter that will return a fragment for each of the three
            // primary sections of the activity.
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) findViewById(R.id.pager);

            mViewPager.setAdapter(mSectionsPagerAdapter);

            getScheduleData(false, false);
            getAutorsData(false, false);
            getInfoData(false, false);
        } else {
            Intent intent = new Intent(this, BaseActivity.class);

            finish();

            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_skip) {
            Intent intent = new Intent(this, BaseActivity.class);

            finish();

            startActivity(intent);

            return true;
        }

        return true;//super.onOptionsItemSelected(item);
    }

    public void setActionBar() {
        getSupportActionBar().setTitle("");
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return PlaceholderFragment.newInstance(R.drawable.garden_logo_hover, R.string.splash_1_text, R.drawable.sgc_icon);
                case 1:
                    return PlaceholderFragment.newInstance(0, R.string.splash_2_text, R.drawable.safety_icon);
                case 2:
                    return PlaceholderFragment.newInstance(0, R.string.splash_3_text, R.drawable.safety_icon);
                case 3:
                    return PlaceholderFragment.newInstance(0, R.string.splash_4_text, R.drawable.safety_icon);
                case 4:
                    return PlaceholderFragment.newInstance(0, R.string.splash_5_text, R.drawable.safety_icon);
                case 5:
                    return PlaceholderFragment.newInstance(0, R.string.splash_6_text, R.drawable.safety_icon);
                case 6:
                    return PlaceholderFragment.newInstance(0, R.string.splash_7_text, R.drawable.safety_icon);
                case 7:
                    return PlaceholderFragment.newInstance(0, R.string.splash_8_text, R.drawable.safety_icon);
                case 8:
                    return PlaceholderFragment.newInstance(0, R.string.splash_9_text, R.drawable.safety_icon);
                case 9:
                    return LoginFragment.newInstance(false);
                case 10:
                    return PlaceholderFragment.newInstance(0, R.string.splash_10_text, R.drawable.sgc_havefun);
            }

            return null;
        }

        @Override
        public int getCount() {
            // Show 11 total pages.
            return 11;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();

            switch (position) {
                case 0:
                    return getString(R.string.title_1_splash).toUpperCase(l);
                case 1:
                    return getString(R.string.title_2_splash).toUpperCase(l);
                case 2:
                    return getString(R.string.title_3_splash).toUpperCase(l);
                case 3:
                    return getString(R.string.title_4_splash).toUpperCase(l);
                case 4:
                    return getString(R.string.title_5_splash).toUpperCase(l);
                case 5:
                    return getString(R.string.title_6_splash).toUpperCase(l);
                case 6:
                    return getString(R.string.title_7_splash).toUpperCase(l);
                case 7:
                    return getString(R.string.title_8_splash).toUpperCase(l);
                case 8:
                    return getString(R.string.title_9_splash).toUpperCase(l);
                case 9:
                    return getResources().getString(R.string.title_10_splash).toUpperCase();
                case 10:
                    return getString(R.string.title_11_splash).toUpperCase(l);
            }

            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_HEADER_RESOURCE = "ARG_HEADER_RESOURCE";
        private static final String ARG_TEXT = "ARG_TEXT";
        private static final String ARG_FOOTER_RESOURCE = "ARG_FOOTER_RESOURCE";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int headerResourceID, int textResource, int footerResourceID) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();

            args.putInt(ARG_HEADER_RESOURCE, headerResourceID);
            args.putInt(ARG_TEXT, textResource);
            args.putInt(ARG_FOOTER_RESOURCE, footerResourceID);

            fragment.setArguments(args);

            return fragment;
        }

        public PlaceholderFragment() { }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_splash, container, false);

            if (rootView != null) {
                TextView textView = (TextView) rootView.findViewById(R.id.splash_text);
                ImageView headerImage = (ImageView) rootView.findViewById(R.id.header_imageView);
                ImageView footerImage = (ImageView) rootView.findViewById(R.id.footer_imageView);

                textView.setText(getResources().getString(getArguments().getInt(ARG_TEXT)));

                int resource = getArguments().getInt(ARG_HEADER_RESOURCE);

                if (resource == 0)
                    headerImage.setVisibility(ImageView.GONE);
                else
                    headerImage.setImageDrawable(getResources().getDrawable(resource));

                resource = getArguments().getInt(ARG_FOOTER_RESOURCE);

                if (resource == 0)
                    footerImage.setVisibility(ImageView.GONE);
                else
                    footerImage.setImageDrawable(getResources().getDrawable(resource));
            }

            return rootView;
        }
    }
}
