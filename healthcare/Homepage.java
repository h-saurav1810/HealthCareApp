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
    String user, fName, bDate, accEmail, accPassword, sex, height, weight, currentIssues, currentMedication;

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

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}