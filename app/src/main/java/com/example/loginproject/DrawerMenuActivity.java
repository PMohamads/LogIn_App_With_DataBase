package com.example.loginproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

public class DrawerMenuActivity extends AppCompatActivity {

    Toolbar ToolBar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FrameLayout frameLayout;
    TextView Adi,SoyAdi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_menu);
        drawerLayout = findViewById(R.id.drawerlayout);
        ToolBar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigation_layout);
        frameLayout = findViewById(R.id.frame_layout);
        Adi = findViewById(R.id.kayitlikisiadi);
        SoyAdi = findViewById(R.id.kayitlikisiSoyadi);
        setSupportActionBar(ToolBar);

        Intent i = getIntent();
        String Kayitliadi = i.getStringExtra("Name");
        String KayitliSoyadi = i.getStringExtra("SurName");
        Adi.setText(Kayitliadi);
        SoyAdi.setText(KayitliSoyadi);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,ToolBar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.addlabel){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    FragmetGetir(new AddLabelFragment());
                }
                if (item.getItemId() == R.id.addphoto){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    FragmetGetir(new AddPhotoFragment());
                }
                if (item.getItemId() == R.id.gallery){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    FragmetGetir(new GalleryFragment());
                }
                if (item.getItemId() == R.id.about){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    FragmetGetir(new AboutFragment());
                }
                if (item.getItemId() == R.id.signout){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(DrawerMenuActivity.this,MainActivity.class));
                }
                return false;
            }
        });
    }

    public void FragmetGetir(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout , fragment);
        fragmentTransaction.commit();
    }

}