package com.dev.toxa.integrate.MainActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import com.dev.toxa.integrate.FragmentConnetctToServer.MVPfragmentConnectToServer;
import com.dev.toxa.integrate.FragmentConnetctToServer.PresenterFragmentConnectToServer;
import com.dev.toxa.integrate.FragmentListServers.MVPfragmentListServers;
import com.dev.toxa.integrate.FragmentListServers.PresenterFragmentListServers;
import com.dev.toxa.integrate.FragmentSettings.MVPFragmentSettings;
import com.dev.toxa.integrate.FragmentSettings.PresenterFragmentSettings;
import com.dev.toxa.integrate.LoggingNameClass;
import com.dev.toxa.integrate.R;
import com.dev.toxa.integrate.FragmentConnetctToServer.FragmentConnectToServer;
import com.dev.toxa.integrate.FragmentListServers.FragmentListServers;
import com.dev.toxa.integrate.FragmentSettings.FragmentSettings;

public class MainActivity extends AppCompatActivity implements MVPmain.view, FragmentListServers.CallbackToActivity {

    private String LOG_TAG = (new LoggingNameClass().parseName(getClass().getName().toString())) + " ";

    private Toolbar toolbar;
    private TabLayout tabLayout;

    private ViewPager viewPager;
    ViewPagerAdapter adapter;

    PresenterMain presenterMain;
    PresenterFragmentListServers presenterFragmentListServers;
    PresenterFragmentConnectToServer presenterFragmentConnectToServer;
    PresenterFragmentSettings presenterFragmentSettings;

    private static final int MY_PERMISSION_ACCESS_COURSE_NOTIFICATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName().toString()));
        super.onCreate(savedInstanceState);

        presenterMain = new PresenterMain(this);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        presenterMain.activityLoaded();

        Log.d(LOG_TAG, "action: " + getIntent().getAction() + " gategory: " + getIntent().getCategories());
    }

    private void setupViewPager(ViewPager viewPager) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentListServers(), "Поиск серверов");
        adapter.addFragment(new FragmentConnectToServer(), "Управление ПК");
        adapter.addFragment(new FragmentSettings(), "Параметры");
        viewPager.setAdapter(adapter);
    }

    @Override
    public Context getContext() {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        return this;
    }

    //====================Создание презертеров фрагментов, прикрепление view к презентерам==============================

    @Override
    public PresenterFragmentListServers setFragmentListServers(MVPfragmentListServers.view view) {
        Log.i(LOG_TAG, "method name: " + Thread.currentThread().getStackTrace()[2].getMethodName());
        if (presenterFragmentListServers != null) {
            presenterFragmentListServers.setView(view);
            Log.d(LOG_TAG, "presenter list servers set view");
        } else {
            presenterFragmentListServers = new PresenterFragmentListServers(view);
            Log.d(LOG_TAG, "presenter list servers create");
        }
        return presenterFragmentListServers;
    }

    @Override
    public PresenterFragmentSettings setFragmentSettings(MVPFragmentSettings.view view) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        if (presenterFragmentSettings != null) {
            presenterFragmentSettings.setView(view);
        } else {
            presenterFragmentSettings = new PresenterFragmentSettings(view);
        }
        return presenterFragmentSettings;
    }

    @Override
    public PresenterFragmentConnectToServer setFragmentConnectToServer(MVPfragmentConnectToServer.view view) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        if (presenterFragmentConnectToServer != null) {
            presenterFragmentConnectToServer.setView(view);
        } else {
            presenterFragmentConnectToServer = new PresenterFragmentConnectToServer(view);
        }
        return presenterFragmentConnectToServer;
    }

    //=============================Отправка IP и servername во fragmentConnectToServer==========+++=====================
    @Override
    public void sendServerIDToFragmentConnect(final int serverID) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        Log.d(LOG_TAG, "ID сервера: " + serverID);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                presenterFragmentConnectToServer.clickedOnFoundServer(serverID);
            }
        });
    }

    @Override
    public void passShareToPresenterConnect(String shared) {
        presenterFragmentConnectToServer.sendSharedLink(shared);
    }

    //=============================================Проверка разрешений==================================================

    @Override
    public boolean checkPermissions() {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));

        if ( ContextCompat.checkSelfPermission(this, Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE ) != PackageManager.PERMISSION_GRANTED ) {
            Log.d(LOG_TAG, "Еще нет доступа к уведомлениям");

        }
        return false;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));

        Log.d(LOG_TAG, "Grant result: " + grantResults[0]);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_ACCESS_COURSE_NOTIFICATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(LOG_TAG, "Разрешение дано");
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Log.d(LOG_TAG, "В доступе отказано");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    //=============================================Жизненный цикл=======================================================

    @Override
    public void onResume(){
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        super.onRestoreInstanceState(savedInstanceState);
    }
    //==================================================================================================================

    @Override
    public void minimazeActivity() {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        moveTaskToBack(true);
    }

    @Override
    public void onBackPressed() {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        presenterFragmentListServers.onDestroy();
        super.onBackPressed();
    }

    //==================================================================================================================
}
