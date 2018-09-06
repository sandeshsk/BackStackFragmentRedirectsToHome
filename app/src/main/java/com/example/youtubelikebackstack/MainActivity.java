package com.example.youtubelikebackstack;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class MainActivity extends AppCompatActivity {

    enum Page {
        HOME(HomeFragment.class),
        DASHBOARD(DashboardFragment.class),
        NOTIFICATION(NotificationFragment.class),
        SETTINGS(SettingFragment.class);

        final public Class clazz;

        Page(Class clazz) {
            this.clazz = clazz;
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setPage(Page.HOME);
                    break;
                case R.id.navigation_dashboard:
                    setPage(Page.DASHBOARD);
                    break;
                case R.id.navigation_notifications:
                    setPage(Page.NOTIFICATION);
                    break;
                case R.id.navigation_setting:
                    setPage(Page.SETTINGS);
                    break;
                default:
                    return false;
            }
            return true;
        }
    };
    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigation = findViewById(R.id.navigation);
        disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);
    }

    private void setPage(Page page) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        String tag = page.name();

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // hide everything
        for (Fragment fragment : fragmentManager.getFragments()) {
            transaction.detach(fragment);
        }

        // Retrieve fragment instance, if it was already created
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == null) { // If not, crate new instance and add it
            try {
                fragment = (Fragment) page.clazz.newInstance();
                transaction.add(R.id.frame, fragment, tag);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } else { // otherwise just show it
            transaction.attach(fragment);
        }
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (!getSupportFragmentManager().findFragmentByTag(Page.HOME.name()).isDetached()) {
            super.onBackPressed();
        } else {
            navigation.setSelectedItemId(R.id.navigation_home);
        }
    }

    @SuppressLint("RestrictedApi")
    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //noinspection RestrictedApi
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }
    }
}
