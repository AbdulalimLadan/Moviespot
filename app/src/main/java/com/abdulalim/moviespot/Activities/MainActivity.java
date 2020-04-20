package com.abdulalim.moviespot.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.abdulalim.moviespot.Fragments.FavFragment;
import com.abdulalim.moviespot.Fragments.HomeFragment;
import com.abdulalim.moviespot.Fragments.ProfileFragment;
import com.abdulalim.moviespot.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView nav_view;
    String currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        initViews();
    }

    private void initViews() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        currentFragment = "Home";
        nav_view = findViewById(R.id.nav_view);
        nav_view.setOnNavigationItemSelectedListener(navListener);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            Fragment selectedFragment = null;
            switch (menuItem.getItemId())
            {
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    currentFragment = "Home";
                    break;
                case R.id.nav_fav:
                    selectedFragment = new FavFragment();
                    currentFragment = "Favourite";
                    break;
                case R.id.nav_profile:
                    selectedFragment = new ProfileFragment();
                    currentFragment = "Profile";
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };
}
