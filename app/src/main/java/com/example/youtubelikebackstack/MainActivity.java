package com.example.youtubelikebackstack;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    addFragment(getSupportFragmentManager(),new HomeFragment(),R.id.frame,true);
                    return true;
                case R.id.navigation_dashboard:
                    addFragment(getSupportFragmentManager(),new DashboardFragment(),R.id.frame,false);
                    return true;
                case R.id.navigation_notifications:
                    addFragment(getSupportFragmentManager(),new NotificationFragment(),R.id.frame,false);
                    return true;
                case R.id.navigation_setting:
                    addFragment(getSupportFragmentManager(),new SettingFragment(),R.id.frame,false);
                    return true;
            }
            return false;
        }
    };
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);
    }

    public void addFragment(FragmentManager fragmentManager,
                                   Fragment fragment,
                                   int containerId,boolean isFromHome){

        fragmentManager.popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);

        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        if(isFromHome){
            fragmentTransaction.replace(containerId,fragment);
        }else{
            fragmentTransaction.add(new HomeFragment(),"Home");
            fragmentTransaction.addToBackStack("Home");
        }
        fragmentTransaction.replace(containerId,fragment).commit();

    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount()>0){
            navigation.setSelectedItemId(R.id.navigation_home);
        }else {
            super.onBackPressed();
        }
    }
}
