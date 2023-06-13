package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Chatbot extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        String username = getIntent().getStringExtra("user");
        String fullName = getIntent().getStringExtra("name");
        String birthdate = getIntent().getStringExtra("dob");
        String mail = getIntent().getStringExtra("email");
        String password = getIntent().getStringExtra("pwd");
        String gender = getIntent().getStringExtra("sex");
        String bodyHeight = getIntent().getStringExtra("height");
        String bodyWeight = getIntent().getStringExtra("weight");
        String healthIssues = getIntent().getStringExtra("issues");
        String healthMedication = getIntent().getStringExtra("medication");

        Intent homeIntent = new Intent(Chatbot.this, Homepage.class);
        homeIntent.putExtra("user", username);
        homeIntent.putExtra("name", fullName);
        homeIntent.putExtra("dob", birthdate);
        homeIntent.putExtra("email", mail);
        homeIntent.putExtra("pwd", password);
        homeIntent.putExtra("sex", gender);
        homeIntent.putExtra("height", bodyHeight);
        homeIntent.putExtra("weight", bodyWeight);
        homeIntent.putExtra("issues", healthIssues);
        homeIntent.putExtra("medication", healthMedication);

        ImageView home;
        home = findViewById(R.id.homeBtn);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(homeIntent);
            }
        });
    }
}