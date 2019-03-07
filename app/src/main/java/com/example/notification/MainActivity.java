package com.example.notification;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private TextView profile;
    private TextView user;
    private TextView notification;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profile = findViewById(R.id.Profile);
        user = findViewById(R.id.AllUsers);
        notification = findViewById(R.id.Notification);
        viewPager = findViewById(R.id.viewpager);
        auth = FirebaseAuth.getInstance();

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);

        // INITIALLY SELECTED
        profile.setTextColor(getColor(R.color.tab_bright));
        profile.setTextSize(20);
        user.setTextColor(getColor(R.color.tab_dark));
        user.setTextSize(16);
        notification.setTextColor(getColor(R.color.tab_dark));
        notification.setTextSize(16);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(0);
            }
        });

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);

            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(2);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                changeTab(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    private void changeTab(int i) {

        if(i == 0){

        } else if(i == 1){
            user.setTextColor(getColor(R.color.tab_bright));
            user.setTextSize(20);

            profile.setTextColor(getColor(R.color.tab_dark));
            profile.setTextSize(16);
            notification.setTextColor(getColor(R.color.tab_dark));
            notification.setTextSize(16);
        }else{
            notification.setTextColor(getColor(R.color.tab_bright));
            notification.setTextSize(20);

            profile.setTextColor(getColor(R.color.tab_dark));
            profile.setTextSize(16);
            user.setTextColor(getColor(R.color.tab_dark));
            user.setTextSize(16);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currUser = auth.getCurrentUser();
        if(currUser == null)
            sendToLogin();
    }

    private void sendToLogin() {
    startActivity(new Intent(MainActivity.this,LoginActivity.class));
    finish();
    }
}
