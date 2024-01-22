package com.example.loginproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void SignUpFunction(View view) {
        startActivity(new Intent(MainActivity.this,SignUpActivity.class));
    }

    public void LogInFunction(View view) {
        startActivity(new Intent(MainActivity.this,LogInActivity.class));

    }
}