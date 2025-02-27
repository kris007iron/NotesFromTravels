package com.example.travel_notes_mobile.view;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.travel_notes_mobile.databinding.ActivityAuthBinding;

public class AuthActivity extends AppCompatActivity {
    private ActivityAuthBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(Color.argb(255, 255, 255, 255));


        binding.btnLogin.setOnClickListener(v -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://192.168.0.171:8080/login"));
            startActivity(browserIntent);
        });
//        binding.btnRegister.setOnClickListener(v -> {
//            Intent intent = new Intent(this, RegisterActivity.class);
//            startActivity(intent);
//        });

    }
}