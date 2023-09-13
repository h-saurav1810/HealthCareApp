package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.healthcare.databinding.ActivityHomepageBinding;
import com.example.healthcare.databinding.ActivityMainBinding;

public class Homepage extends AppCompatActivity {

    ActivityHomepageBinding binding;
    String user, fName, bDate, accEmail, accPassword, sex, height, weight, currentIssues, currentMedication, healthModel, heart_last, o2_last, temp_last, glucose_last, diagnosis_last, recommendation_last;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
        String monitorModel = getIntent().getStringExtra("model");
        String lastHeart = getIntent().getStringExtra("last_heart");
        String lastO2 = getIntent().getStringExtra("last_o2");
        String lastTemp = getIntent().getStringExtra("last_temp");
        String lastGlucose = getIntent().getStringExtra("last_glucose");
        String lastDiagnosis = getIntent().getStringExtra("last_diagnosis");
        String lastRecommendation = getIntent().getStringExtra("last_recommendation");

        user = username;
        fName = fullName;
        bDate = birthdate;
        accEmail = mail;
        accPassword = password;
        sex = gender;
        height = bodyHeight;
        weight = bodyWeight;
        currentIssues = healthIssues;
        currentMedication = healthMedication;
        healthModel = monitorModel;
        heart_last = lastHeart;
        o2_last = lastO2;
        temp_last = lastTemp;
        glucose_last = lastGlucose;
        diagnosis_last = lastDiagnosis;
        recommendation_last = lastRecommendation;

        Intent botIntent = new Intent(Homepage.this, Chatbot.class);
        botIntent.putExtra("user", user);
        botIntent.putExtra("name", fName);
        botIntent.putExtra("dob", bDate);
        botIntent.putExtra("email", accEmail);
        botIntent.putExtra("pwd", accPassword);
        botIntent.putExtra("sex", sex);
        botIntent.putExtra("height", height);
        botIntent.putExtra("weight", weight);
        botIntent.putExtra("issues", currentIssues);
        botIntent.putExtra("medication", currentMedication);
        botIntent.putExtra("model", healthModel);
        botIntent.putExtra("last_heart", heart_last);
        botIntent.putExtra("last_o2", o2_last);
        botIntent.putExtra("last_temp", temp_last);
        botIntent.putExtra("last_glucose", glucose_last);
        botIntent.putExtra("last_diagnosis", diagnosis_last);
        botIntent.putExtra("last_recommendation", recommendation_last);

        replaceFragment(new HomeFragment());
        binding.bottomNavView.setBackground(null);

        binding.bottomNavView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.home)
            {
                replaceFragment(new HomeFragment());
            }
            else if (item.getItemId() == R.id.stats)
            {
                replaceFragment(new StatsFragment());
            }
            else if (item.getItemId() == R.id.bot)
            {
                startActivity(botIntent);
            }
            else if (item.getItemId() == R.id.profile)
            {
                replaceFragment(new ProfileFragment());
            }

            return true;

        });

    }

    public String getUsername()
    {
        return user;
    }

    public String getName()
    {
        return fName;
    }

    public String getBDate()
    {
        return bDate;
    }

    public String getAccEmail()
    {
        return accEmail;
    }

    public String getAccPassword()
    {
        return accPassword;
    }

    public String getSex()
    {
        return sex;
    }

    public String getHeight()
    {
        return height;
    }

    public String getWeight()
    {
        return weight;
    }

    public String getCurrentIssues()
    {
        return currentIssues;
    }

    public String getCurrentMedication()
    {
        return currentMedication;
    }

    public String getHealthModel() { return healthModel; }

    public String getHeart_last() { return heart_last; }

    public String getO2_last() { return o2_last; }

    public String getTemp_last() { return temp_last; }

    public String getGlucose_last() { return glucose_last; }

    public String getDiagnosis_last() { return diagnosis_last; }

    public String getRecommendation_last() { return recommendation_last; }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}