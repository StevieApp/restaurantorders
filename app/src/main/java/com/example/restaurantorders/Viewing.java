package com.example.restaurantorders;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Viewing extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener newListener = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.allorders:
                            selectedFragment = new Orders();
                            break;
                        case R.id.dispatchedorders:
                            selectedFragment = new DispatchedOrders();
                            break;

                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_area,
                            selectedFragment).commit();
                    return true;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewing);
        BottomNavigationView bottom_nav = findViewById(R.id.bottom_navigator);
        bottom_nav.setOnNavigationItemSelectedListener(newListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_area,
                new Orders()).commit();


    }


}
