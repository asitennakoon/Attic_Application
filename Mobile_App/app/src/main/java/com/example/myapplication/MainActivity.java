package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    public static final String ROOM_KEY = "com.example.myapplication.MainActivity";
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    //load category screen
    public void loadCategory(View view) {
        Intent intent = new Intent(view.getContext(), Camera.class);
        startActivity(intent);
    }

    //load manual view screen
    public void ManualView(View view) {
        // setting animations for the button
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.image_anim);
        view.startAnimation(animation);

        /*
         * Each category button has a tag which determines which button actually is clicked.
         * It will be passed to the next activity, where relevant room type will be picked.*/
        Intent intent = new Intent(this, Manual_AR_View.class);
        Log.d(LOG_TAG, String.valueOf(view.getTag()));
        intent.putExtra(ROOM_KEY, String.valueOf(view.getTag()));
        startActivity(intent);
    }
}
