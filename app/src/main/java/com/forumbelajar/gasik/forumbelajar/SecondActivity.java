package com.forumbelajar.gasik.forumbelajar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by Gasik on 6/8/2016.
 */
public class SecondActivity extends ActionBarActivity implements android.support.v7.app.ActionBar.TabListener,Communicator {

    private ViewPager tabsviewPager;
    private Tabsadapter mTabsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        setTitle("Forum Belajar");

        tabsviewPager = (ViewPager) findViewById(R.id.pager);

        mTabsAdapter = new Tabsadapter(getSupportFragmentManager());

        tabsviewPager.setAdapter(mTabsAdapter);

        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab tab_home = getSupportActionBar().newTab().setText("Home").setTabListener(this);
        ActionBar.Tab tab_profile = getSupportActionBar().newTab().setText("Profile").setTabListener(this);

        getSupportActionBar().addTab(tab_home);
        getSupportActionBar().addTab(tab_profile);

        tabsviewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                getSupportActionBar().setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
        tabsviewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void respond(String data) {

    }

    @Override
    public void goTo(String data) {
        switch (data) {
            case "AddquestionActivity":
                Intent intent = new Intent(SecondActivity.this, AddquestionActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void createSession(String key, String Value) {

    }
}
