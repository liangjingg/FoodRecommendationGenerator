package com.example.cz2006_hungryspoons;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.cz2006_hungryspoons.caloriesdiary.CaloriesFragment;
import com.example.cz2006_hungryspoons.user.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navLister);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new EateriesFragment()).commit();

    }


    private BottomNavigationView.OnNavigationItemSelectedListener navLister = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            Fragment selectedFragment = null;

            switch(menuItem.getItemId()){

                case R.id.nav_park:
                    selectedFragment = new ParkFragment();
                    break;
                case R.id.nav_eateries:
                    selectedFragment = new EateriesFragment();
                    break;
                case R.id.nav_calories:
                    selectedFragment = new CaloriesFragment();
                    break;
                case R.id.nav_me:
                    selectedFragment = new UserFragment();
                    break;


            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

            return true; //to select the item at the bottom.
        }
    };

}
