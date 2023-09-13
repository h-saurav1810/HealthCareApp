package com.example.healthcare;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {
/*
    String lastHeartVal;
    String lastO2Val;
    String lastTempVal;
    String lastGlucoseVal;
    String diagnosis;

*/
    String user;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://healthcare-c3b0a-default-rtdb.firebaseio.com/");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Homepage home = (Homepage) getActivity();
        String username = home.getUsername();
        String name = home.getName();
        String bDate = home.getBDate();
        String accEmail = home.getAccEmail();
        String accPassword = home.getAccPassword();
        String sex = home.getSex();
        String height = home.getHeight();
        String weight = home.getWeight();
        String currentIssues = home.getCurrentIssues();
        String currentMedication = home.getCurrentMedication();
        String healthModel = home.getHealthModel();
        String heart_last = home.getHeart_last();
        String o2_last = home.getO2_last();
        String temp_last = home.getTemp_last();
        String glucose_last = home.getGlucose_last();
        String diagnosis_last = home.getDiagnosis_last();
        String recommendation_last = home.getRecommendation_last();

        // Inflate the layout for this fragment
        View myInflatedView = inflater.inflate(R.layout.fragment_home, container, false);
        user = username;

        TextView welcome, lastHeart, lastO2, lastTemp, lastGlucose, lastIllness;
        AppCompatButton chatbotButton, heartStatsButton, o2StatsButton, tempStatsButton, glucoseStatsButton, lastDiagnosisButton;

        welcome = (TextView) myInflatedView.findViewById(R.id.welcomeText);
        welcome.setText("Hello, " + name + "!");

        lastHeart = (TextView) myInflatedView.findViewById(R.id.lastRecordedHeart);
        lastO2 = (TextView) myInflatedView.findViewById(R.id.lastRecordedO2);
        lastTemp = (TextView) myInflatedView.findViewById(R.id.lastRecordedTemp);
        lastGlucose = (TextView) myInflatedView.findViewById(R.id.lastRecordedGlucose);
        lastIllness = (TextView) myInflatedView.findViewById(R.id.lastRecordedIllness);

        chatbotButton = (AppCompatButton) myInflatedView.findViewById(R.id.checkUpBtn);
        heartStatsButton = (AppCompatButton) myInflatedView.findViewById(R.id.heartStatsBtn);
        o2StatsButton = (AppCompatButton) myInflatedView.findViewById(R.id.o2StatsBtn);
        tempStatsButton = (AppCompatButton) myInflatedView.findViewById(R.id.tempStatsBtn);
        glucoseStatsButton = (AppCompatButton) myInflatedView.findViewById(R.id.glucoseStatsBtn);
        lastDiagnosisButton = (AppCompatButton) myInflatedView.findViewById(R.id.diagnosisBtn);

        Log.d(TAG, user);

        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(user))
                {
                    final String lastHeartVal = String.valueOf(snapshot.child(user).child("Last Heart").getValue(Integer.class));
                    if (lastHeartVal.equals("-1"))
                    {
                        final String replaceHeartVal = "----";
                        lastHeart.setText(replaceHeartVal);
                    }
                    else
                    {
                        lastHeart.setText(lastHeartVal);
                    }

                    final String lastO2Val = String.valueOf(snapshot.child(user).child("Last O2").getValue(Integer.class));
                    if (lastO2Val.equals("-1"))
                    {
                        final String replaceO2Val = "----";
                        lastO2.setText(replaceO2Val);
                    }
                    else
                    {
                        lastO2.setText(lastO2Val);
                    }

                    final String lastTempVal = String.valueOf(snapshot.child(user).child("Last Temp").getValue(Double.class));
                    if (lastTempVal.equals("0.1"))
                    {
                        final String replaceTempVal = "------";
                        lastTemp.setText(replaceTempVal);
                    }
                    else
                    {
                        lastTemp.setText(lastTempVal);
                    }

                    final String lastGlucoseVal = String.valueOf(snapshot.child(user).child("Last Glucose").getValue(Double.class));
                    if (lastGlucoseVal.equals("0.1"))
                    {
                        final String replaceGlucoseVal = "------";
                        lastGlucose.setText(replaceGlucoseVal);
                    }
                    else
                    {
                        lastGlucose.setText(lastGlucoseVal);
                    }

                    final String diagnosis = snapshot.child(user).child("Last Diagnosis").getValue(String.class);
                    lastIllness.setText(diagnosis);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        chatbotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent botIntent = new Intent(getActivity(), Chatbot.class);
                botIntent.putExtra("user", username);
                botIntent.putExtra("name", name);
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
                startActivity(botIntent);
            }
        });

        lastDiagnosisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent botIntent = new Intent(getActivity(), Diagnosis.class);
                botIntent.putExtra("user", username);
                botIntent.putExtra("name", name);
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
                startActivity(botIntent);
            }
        });

        heartStatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.replaceFragment(new StatsFragment());
            }
        });

        o2StatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.replaceFragment(new O2StatsFragment());
            }
        });

        tempStatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.replaceFragment(new TempStatsFragment());
            }
        });

        glucoseStatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.replaceFragment(new GlucoseStatsFragment());
            }
        });


        return myInflatedView;
    }
}