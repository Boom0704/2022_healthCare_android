package org.techtown.healthycare;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;

import org.techtown.healthycare.bottomFragment.homeFragment;
import org.techtown.healthycare.bottomFragment.sportFragment;
import org.techtown.healthycare.bottomFragment.rankFragment;
import org.techtown.healthycare.bottomFragment.mypageFragment;

public class MainActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout frameLayout = findViewById(R.id.frameLayout);
        Button btn_logout = findViewById(R.id.btn_logout);
        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, new homeFragment()).commit();



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                int itemId = item.getItemId();

                switch (itemId) {
                    // 아이템
                    case R.id.bottom1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new homeFragment()).commit();
                        return  true;

                    case R.id.bottom2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new sportFragment()).commit();
                        return  true;

                    case R.id.bottom3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new rankFragment()).commit();
                        return  true;
                    case R.id.bottom4:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new mypageFragment()).commit();
                        return  true;


                    default: return  true;
                }

            }

        });

    }
}