package com.example.healthcare;

import static java.lang.Math.pow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Diagnosis extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://healthcare-c3b0a-default-rtdb.firebaseio.com/");

    String username;
    String checkupModel;

    Double bmi = 0.0;
    Double bmiPoints = 0.0, otherPoints = 0.0, vitalPoints = 0.0, totalPoints = 0.0, symptomPoints = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis);

        username = getIntent().getStringExtra("user");
        String fullName = getIntent().getStringExtra("name");
        String birthdate = getIntent().getStringExtra("dob");
        String mail = getIntent().getStringExtra("email");
        String password = getIntent().getStringExtra("pwd");
        String gender = getIntent().getStringExtra("sex");
        String bodyHeight = getIntent().getStringExtra("height");
        String bodyWeight = getIntent().getStringExtra("weight");
        String healthIssues = getIntent().getStringExtra("issues");
        String healthMedication = getIntent().getStringExtra("medication");
        checkupModel = getIntent().getStringExtra("model");
        String lastHeart = getIntent().getStringExtra("last_heart");
        String lastO2 = getIntent().getStringExtra("last_o2");
        String lastTemp = getIntent().getStringExtra("last_temp");
        String lastGlucose = getIntent().getStringExtra("last_glucose");
        String lastDiagnosis = getIntent().getStringExtra("last_diagnosis");
        String lastRecommendation = getIntent().getStringExtra("last_recommendation");

        Double height = Double.valueOf(bodyHeight);
        Double weight = Double.valueOf(bodyWeight);

        Intent homeIntent = new Intent(Diagnosis.this, Homepage.class);
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
        homeIntent.putExtra("model", checkupModel);
        homeIntent.putExtra("last_heart", lastHeart);
        homeIntent.putExtra("last_o2", lastO2);
        homeIntent.putExtra("last_temp", lastTemp);
        homeIntent.putExtra("last_glucose", lastGlucose);
        homeIntent.putExtra("last_diagnosis", lastDiagnosis);
        homeIntent.putExtra("last_recommendation", lastRecommendation);

        TextView illness;
        TextView heartMonitored, tempMonitored, glucoseMonitored, o2Monitored;
        TextView diagnosisHeart, diagnosisTemp, diagnosisGlucose, diagnosisO2, diagnosisSymptom, diagnosisBMI;
        TextView scoreVitals, scoreSymptom, scoreBMI, scoreOther, scoreTotal;

        illness = findViewById(R.id.RecordedIllness);
        heartMonitored = findViewById(R.id.lastMonitoredHeart);
        tempMonitored = findViewById(R.id.lastMonitoredTemp);
        glucoseMonitored = findViewById(R.id.lastMonitoredGlucose);
        o2Monitored = findViewById(R.id.lastMonitoredO2);

        diagnosisHeart = findViewById(R.id.heartDiagnosis);
        diagnosisTemp = findViewById(R.id.tempDiagnosis);
        diagnosisGlucose = findViewById(R.id.glucoseDiagnosis);
        diagnosisO2 = findViewById(R.id.o2Diagnosis);
        diagnosisSymptom = findViewById(R.id.symptomsDiagnosis);
        diagnosisBMI = findViewById(R.id.bmiDiagnosis);

        scoreVitals = findViewById(R.id.vitalsScore);
        scoreSymptom = findViewById(R.id.symptomsScore);
        scoreBMI = findViewById(R.id.bmiScore);
        scoreOther = findViewById(R.id.otherScore);
        scoreTotal = findViewById(R.id.recommendScore);

        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(username))
                {
                    final String lastHeartVal = String.valueOf(snapshot.child(username).child("Last Heart").getValue(Integer.class));
                    if (lastHeartVal.equals("-1"))
                    {
                        final String replaceHeartVal = "----";
                        heartMonitored.setText(replaceHeartVal);
                    }
                    else
                    {
                        heartMonitored.setText(lastHeartVal);
                    }

                    final String lastO2Val = String.valueOf(snapshot.child(username).child("Last O2").getValue(Integer.class));
                    if (lastO2Val.equals("-1"))
                    {
                        final String replaceO2Val = "----";
                        o2Monitored.setText(replaceO2Val);
                    }
                    else
                    {
                        o2Monitored.setText(lastO2Val);
                    }

                    final String lastTempVal = String.valueOf(snapshot.child(username).child("Last Temp").getValue(Double.class));
                    if (lastTempVal.equals("0.1"))
                    {
                        final String replaceTempVal = "------";
                        tempMonitored.setText(replaceTempVal);
                    }
                    else
                    {
                        tempMonitored.setText(lastTempVal);
                    }

                    final String lastGlucoseVal = String.valueOf(snapshot.child(username).child("Last Glucose").getValue(Double.class));
                    if (lastGlucoseVal.equals("0.1"))
                    {
                        final String replaceGlucoseVal = "------";
                        glucoseMonitored.setText(replaceGlucoseVal);
                    }
                    else
                    {
                        glucoseMonitored.setText(lastGlucoseVal);
                    }

                    final String diagnosis = snapshot.child(username).child("Last Diagnosis").getValue(String.class);
                    illness.setText(diagnosis);


                    if (diagnosis.equals("Healthy"))
                    {
                        final Integer heart = snapshot.child(username).child("Last Heart").getValue(Integer.class);
                        final Integer o2 = snapshot.child(username).child("Last O2").getValue(Integer.class);
                        final Double glucose = snapshot.child(username).child("Last Glucose").getValue(Double.class);
                        final Double temp = snapshot.child(username).child("Last Temp").getValue(Double.class);

                        if (temp >= 36.4 && temp <= 37.5)
                        {
                            diagnosisTemp.setText("Body Temp. falls within 36.4 and 37.5 °C");
                            vitalPoints += 12.5;
                        }
                        else if (temp > 37.5)
                        {
                            diagnosisTemp.setText("Body Temp. is greater than 37.5 °C");
                        }
                        else
                        {
                            diagnosisTemp.setText("Body Temp. is less than 36.4 °C");
                        }

                        if (heart >= 60 && heart <= 100)
                        {
                            diagnosisHeart.setText("Heart Rate falls within 60 BPM and 100 BPM");
                            vitalPoints += 12.5;
                        }
                        else if (heart > 100)
                        {
                            diagnosisHeart.setText("Heart Rate is greater than 100 BPM");
                        }
                        else
                        {
                            diagnosisHeart.setText("Heart Rate is less than 60 BPM");
                        }

                        if (o2 >= 95)
                        {
                            diagnosisO2.setText("O2 Level is greater than 95%");
                            vitalPoints += 12.5;
                        }
                        else
                        {
                            diagnosisO2.setText("O2 Level is less than 95%");
                        }

                        if (glucose >= 80 && glucose <= 120)
                        {
                            diagnosisGlucose.setText("Blood Glucose Level falls within 80 mg/dl and 120 mg/dl");
                            vitalPoints += 12.5;
                        }
                        else if (glucose > 120)
                        {
                            diagnosisGlucose.setText("Blood Glucose Level is greater than 120 mg/dl");
                        }
                        else
                        {
                            diagnosisGlucose.setText("Blood Glucose level is less than 80 mg/dl");
                        }

                        bmi = weight / pow((height/100), 2);
                        if (bmi > 25 || bmi < 19)
                        {
                            diagnosisBMI.setText("BMI: " + String.valueOf(bmi) + "% (Unhealthy BMI)");
                        }
                        else
                        {
                            diagnosisBMI.setText("BMI: " + String.valueOf(bmi) + "% (Healthy BMI)");
                            bmiPoints = 10.0;
                        }

                        otherPoints = 5.0;
                        if (healthIssues.isEmpty())
                        {
                            otherPoints += 5.0;
                        }

                        symptomPoints = 30.0;

                        scoreBMI.setText(String.valueOf(bmiPoints));
                        scoreSymptom.setText(String.valueOf(symptomPoints));
                        scoreOther.setText(String.valueOf(otherPoints));
                        scoreVitals.setText(String.valueOf(vitalPoints));
                        totalPoints = vitalPoints + symptomPoints + bmiPoints + otherPoints;
                        scoreTotal.setText(String.valueOf(totalPoints));

                    }
                    else if (diagnosis.equals("Fever"))
                    {
                        final Integer heart = snapshot.child(username).child("Last Heart").getValue(Integer.class);
                        final Integer o2 = snapshot.child(username).child("Last O2").getValue(Integer.class);
                        final Double glucose = snapshot.child(username).child("Last Glucose").getValue(Double.class);
                        final Double temp = snapshot.child(username).child("Last Temp").getValue(Double.class);

                        if (temp >= 36.4 && temp <= 37.5)
                        {
                            diagnosisTemp.setText("Body Temp. falls within 36.4 and 37.5 °C");
                            vitalPoints += 12.5;
                        }
                        else if (temp > 37.5)
                        {
                            diagnosisTemp.setText("Body Temp. is greater than 37.5 °C");
                        }
                        else
                        {
                            diagnosisTemp.setText("Body Temp. is less than 36.4 °C");
                        }

                        if (heart >= 60 && heart <= 100)
                        {
                            diagnosisHeart.setText("Heart Rate falls within 60 BPM and 100 BPM");
                            vitalPoints += 12.5;
                        }
                        else if (heart > 100)
                        {
                            diagnosisHeart.setText("Heart Rate is greater than 100 BPM");
                        }
                        else
                        {
                            diagnosisHeart.setText("Heart Rate is less than 60 BPM");
                        }

                        if (o2 >= 95)
                        {
                            diagnosisO2.setText("O2 Level is greater than 95%");
                            vitalPoints += 12.5;
                        }
                        else
                        {
                            diagnosisO2.setText("O2 Level is less than 95%");
                        }

                        if (glucose >= 80 && glucose <= 120)
                        {
                            diagnosisGlucose.setText("Blood Glucose Level falls within 80 mg/dl and 120 mg/dl");
                            vitalPoints += 12.5;
                        }
                        else if (glucose > 120)
                        {
                            diagnosisGlucose.setText("Blood Glucose Level is greater than 120 mg/dl");
                        }
                        else
                        {
                            diagnosisGlucose.setText("Blood Glucose level is less than 80 mg/dl");
                        }

                        bmi = weight / pow((height/100), 2);
                        if (bmi > 25 || bmi < 19)
                        {
                            diagnosisBMI.setText("BMI: " + String.valueOf(bmi) + "% (Unhealthy BMI)");
                        }
                        else
                        {
                            diagnosisBMI.setText("BMI: " + String.valueOf(bmi) + "% (Healthy BMI)");
                            bmiPoints = 10.0;
                        }

                        otherPoints = 5.0;
                        if (healthIssues.isEmpty())
                        {
                            otherPoints += 5.0;
                        }

                        symptomPoints = 30.0;

                        scoreBMI.setText(String.valueOf(bmiPoints));
                        scoreSymptom.setText(String.valueOf(symptomPoints));
                        scoreOther.setText(String.valueOf(otherPoints));
                        scoreVitals.setText(String.valueOf(vitalPoints));
                        totalPoints = vitalPoints + symptomPoints + bmiPoints + otherPoints;
                        scoreTotal.setText(String.valueOf(totalPoints));
                    }
                    else if (diagnosis.equals("Flu"))
                    {
                        final Integer heart = snapshot.child(username).child("Last Heart").getValue(Integer.class);
                        final Integer o2 = snapshot.child(username).child("Last O2").getValue(Integer.class);
                        final Double glucose = snapshot.child(username).child("Last Glucose").getValue(Double.class);
                        final Double temp = snapshot.child(username).child("Last Temp").getValue(Double.class);

                        if (temp >= 36.4 && temp <= 37.5)
                        {
                            diagnosisTemp.setText("Body Temp. falls within 36.4 and 37.5 °C");
                            vitalPoints += 12.5;
                        }
                        else if (temp > 37.5)
                        {
                            diagnosisTemp.setText("Body Temp. is greater than 37.5 °C");
                        }
                        else
                        {
                            diagnosisTemp.setText("Body Temp. is less than 36.4 °C");
                        }

                        if (heart >= 60 && heart <= 100)
                        {
                            diagnosisHeart.setText("Heart Rate falls within 60 BPM and 100 BPM");
                            vitalPoints += 12.5;
                        }
                        else if (heart > 100)
                        {
                            diagnosisHeart.setText("Heart Rate is greater than 100 BPM");
                        }
                        else
                        {
                            diagnosisHeart.setText("Heart Rate is less than 60 BPM");
                        }

                        if (o2 >= 95)
                        {
                            diagnosisO2.setText("O2 Level is greater than 95%");
                            vitalPoints += 12.5;
                        }
                        else
                        {
                            diagnosisO2.setText("O2 Level is less than 95%");
                        }

                        if (glucose >= 80 && glucose <= 120)
                        {
                            diagnosisGlucose.setText("Blood Glucose Level falls within 80 mg/dl and 120 mg/dl");
                            vitalPoints += 12.5;
                        }
                        else if (glucose > 120)
                        {
                            diagnosisGlucose.setText("Blood Glucose Level is greater than 120 mg/dl");
                        }
                        else
                        {
                            diagnosisGlucose.setText("Blood Glucose level is less than 80 mg/dl");
                        }

                        bmi = weight / pow((height/100), 2);
                        if (bmi > 25 || bmi < 19)
                        {
                            diagnosisBMI.setText("BMI: " + String.valueOf(bmi) + "% (Unhealthy BMI)");
                        }
                        else
                        {
                            diagnosisBMI.setText("BMI: " + String.valueOf(bmi) + "% (Healthy BMI)");
                            bmiPoints = 10.0;
                        }

                        otherPoints = 5.0;
                        if (healthIssues.isEmpty())
                        {
                            otherPoints += 5.0;
                        }

                        symptomPoints = 30.0;

                        scoreBMI.setText(String.valueOf(bmiPoints));
                        scoreSymptom.setText(String.valueOf(symptomPoints));
                        scoreOther.setText(String.valueOf(otherPoints));
                        scoreVitals.setText(String.valueOf(vitalPoints));
                        totalPoints = vitalPoints + symptomPoints + bmiPoints + otherPoints;
                        scoreTotal.setText(String.valueOf(totalPoints));
                    }
                    else if (diagnosis.equals("Covid-19"))
                    {
                        final Integer heart = snapshot.child(username).child("Last Heart").getValue(Integer.class);
                        final Integer o2 = snapshot.child(username).child("Last O2").getValue(Integer.class);
                        final Double glucose = snapshot.child(username).child("Last Glucose").getValue(Double.class);
                        final Double temp = snapshot.child(username).child("Last Temp").getValue(Double.class);

                        if (temp >= 36.4 && temp <= 37.5)
                        {
                            diagnosisTemp.setText("Body Temp. falls within 36.4 and 37.5 °C");
                            vitalPoints += 12.5;
                        }
                        else if (temp > 37.5)
                        {
                            diagnosisTemp.setText("Body Temp. is greater than 37.5 °C");
                        }
                        else
                        {
                            diagnosisTemp.setText("Body Temp. is less than 36.4 °C");
                        }

                        if (heart >= 60 && heart <= 100)
                        {
                            diagnosisHeart.setText("Heart Rate falls within 60 BPM and 100 BPM");
                            vitalPoints += 12.5;
                        }
                        else if (heart > 100)
                        {
                            diagnosisHeart.setText("Heart Rate is greater than 100 BPM");
                        }
                        else
                        {
                            diagnosisHeart.setText("Heart Rate is less than 60 BPM");
                        }

                        if (o2 >= 95)
                        {
                            diagnosisO2.setText("O2 Level is greater than 95%");
                            vitalPoints += 12.5;
                        }
                        else
                        {
                            diagnosisO2.setText("O2 Level is less than 95%");
                        }

                        if (glucose >= 80 && glucose <= 120)
                        {
                            diagnosisGlucose.setText("Blood Glucose Level falls within 80 mg/dl and 120 mg/dl");
                            vitalPoints += 12.5;
                        }
                        else if (glucose > 120)
                        {
                            diagnosisGlucose.setText("Blood Glucose Level is greater than 120 mg/dl");
                        }
                        else
                        {
                            diagnosisGlucose.setText("Blood Glucose level is less than 80 mg/dl");
                        }

                        bmi = weight / pow((height/100), 2);
                        if (bmi > 25 || bmi < 19)
                        {
                            diagnosisBMI.setText("BMI: " + String.valueOf(bmi) + "% (Unhealthy BMI)");
                        }
                        else
                        {
                            diagnosisBMI.setText("BMI: " + String.valueOf(bmi) + "% (Healthy BMI)");
                            bmiPoints = 10.0;
                        }

                        otherPoints = 5.0;
                        if (healthIssues.isEmpty())
                        {
                            otherPoints += 5.0;
                        }

                        symptomPoints = 30.0;

                        scoreBMI.setText(String.valueOf(bmiPoints));
                        scoreSymptom.setText(String.valueOf(symptomPoints));
                        scoreOther.setText(String.valueOf(otherPoints));
                        scoreVitals.setText(String.valueOf(vitalPoints));
                        totalPoints = vitalPoints + symptomPoints + bmiPoints + otherPoints;
                        scoreTotal.setText(String.valueOf(totalPoints));
                    }
                    else if (diagnosis.equals("Asthma"))
                    {
                        final Integer heart = snapshot.child(username).child("Last Heart").getValue(Integer.class);
                        final Integer o2 = snapshot.child(username).child("Last O2").getValue(Integer.class);
                        final Double glucose = snapshot.child(username).child("Last Glucose").getValue(Double.class);
                        final Double temp = snapshot.child(username).child("Last Temp").getValue(Double.class);

                        if (temp >= 36.4 && temp <= 37.5)
                        {
                            diagnosisTemp.setText("Body Temp. falls within 36.4 and 37.5 °C");
                            vitalPoints += 12.5;
                        }
                        else if (temp > 37.5)
                        {
                            diagnosisTemp.setText("Body Temp. is greater than 37.5 °C");
                        }
                        else
                        {
                            diagnosisTemp.setText("Body Temp. is less than 36.4 °C");
                        }

                        if (heart >= 60 && heart <= 100)
                        {
                            diagnosisHeart.setText("Heart Rate falls within 60 BPM and 100 BPM");
                            vitalPoints += 12.5;
                        }
                        else if (heart > 100)
                        {
                            diagnosisHeart.setText("Heart Rate is greater than 100 BPM");
                        }
                        else
                        {
                            diagnosisHeart.setText("Heart Rate is less than 60 BPM");
                        }

                        if (o2 >= 95)
                        {
                            diagnosisO2.setText("O2 Level is greater than 95%");
                            vitalPoints += 12.5;
                        }
                        else
                        {
                            diagnosisO2.setText("O2 Level is less than 95%");
                        }

                        if (glucose >= 80 && glucose <= 120)
                        {
                            diagnosisGlucose.setText("Blood Glucose Level falls within 80 mg/dl and 120 mg/dl");
                            vitalPoints += 12.5;
                        }
                        else if (glucose > 120)
                        {
                            diagnosisGlucose.setText("Blood Glucose Level is greater than 120 mg/dl");
                        }
                        else
                        {
                            diagnosisGlucose.setText("Blood Glucose level is less than 80 mg/dl");
                        }

                        bmi = weight / pow((height/100), 2);
                        if (bmi > 25 || bmi < 19)
                        {
                            diagnosisBMI.setText("BMI: " + String.valueOf(bmi) + "% (Unhealthy BMI)");
                        }
                        else
                        {
                            diagnosisBMI.setText("BMI: " + String.valueOf(bmi) + "% (Healthy BMI)");
                            bmiPoints = 10.0;
                        }

                        otherPoints = 5.0;
                        if (healthIssues.isEmpty())
                        {
                            otherPoints += 5.0;
                        }

                        symptomPoints = 30.0;

                        scoreBMI.setText(String.valueOf(bmiPoints));
                        scoreSymptom.setText(String.valueOf(symptomPoints));
                        scoreOther.setText(String.valueOf(otherPoints));
                        scoreVitals.setText(String.valueOf(vitalPoints));
                        totalPoints = vitalPoints + symptomPoints + bmiPoints + otherPoints;
                        scoreTotal.setText(String.valueOf(totalPoints));
                    }
                    else if (lastRecommendation.equals("Hyperglycemia"))
                    {
                        final Integer heart = snapshot.child(username).child("Last Heart").getValue(Integer.class);
                        final Integer o2 = snapshot.child(username).child("Last O2").getValue(Integer.class);
                        final Double glucose = snapshot.child(username).child("Last Glucose").getValue(Double.class);
                        final Double temp = snapshot.child(username).child("Last Temp").getValue(Double.class);

                        if (temp >= 36.4 && temp <= 37.5)
                        {
                            diagnosisTemp.setText("Body Temp. falls within 36.4 and 37.5 °C");
                            vitalPoints += 12.5;
                        }
                        else if (temp > 37.5)
                        {
                            diagnosisTemp.setText("Body Temp. is greater than 37.5 °C");
                        }
                        else
                        {
                            diagnosisTemp.setText("Body Temp. is less than 36.4 °C");
                        }

                        if (heart >= 60 && heart <= 100)
                        {
                            diagnosisHeart.setText("Heart Rate falls within 60 BPM and 100 BPM");
                            vitalPoints += 12.5;
                        }
                        else if (heart > 100)
                        {
                            diagnosisHeart.setText("Heart Rate is greater than 100 BPM");
                        }
                        else
                        {
                            diagnosisHeart.setText("Heart Rate is less than 60 BPM");
                        }

                        if (o2 >= 95)
                        {
                            diagnosisO2.setText("O2 Level is greater than 95%");
                            vitalPoints += 12.5;
                        }
                        else
                        {
                            diagnosisO2.setText("O2 Level is less than 95%");
                        }

                        if (glucose >= 80 && glucose <= 120)
                        {
                            diagnosisGlucose.setText("Blood Glucose Level falls within 80 mg/dl and 120 mg/dl");
                            vitalPoints += 12.5;
                        }
                        else if (glucose > 120)
                        {
                            diagnosisGlucose.setText("Blood Glucose Level is greater than 120 mg/dl");
                        }
                        else
                        {
                            diagnosisGlucose.setText("Blood Glucose level is less than 80 mg/dl");
                        }

                        bmi = weight / pow((height/100), 2);
                        if (bmi > 25 || bmi < 19)
                        {
                            diagnosisBMI.setText("BMI: " + String.valueOf(bmi) + "% (Unhealthy BMI)");
                        }
                        else
                        {
                            diagnosisBMI.setText("BMI: " + String.valueOf(bmi) + "% (Healthy BMI)");
                            bmiPoints = 10.0;
                        }

                        otherPoints = 5.0;
                        if (healthIssues.isEmpty())
                        {
                            otherPoints += 5.0;
                        }

                        symptomPoints = 30.0;

                        scoreBMI.setText(String.valueOf(bmiPoints));
                        scoreSymptom.setText(String.valueOf(symptomPoints));
                        scoreOther.setText(String.valueOf(otherPoints));
                        scoreVitals.setText(String.valueOf(vitalPoints));
                        totalPoints = vitalPoints + symptomPoints + bmiPoints + otherPoints;
                        scoreTotal.setText(String.valueOf(totalPoints));
                    }
                    else if (lastRecommendation.equals("Pre-Diabetes"))
                    {
                        final Integer heart = snapshot.child(username).child("Last Heart").getValue(Integer.class);
                        final Integer o2 = snapshot.child(username).child("Last O2").getValue(Integer.class);
                        final Double glucose = snapshot.child(username).child("Last Glucose").getValue(Double.class);
                        final Double temp = snapshot.child(username).child("Last Temp").getValue(Double.class);

                        if (temp >= 36.4 && temp <= 37.5)
                        {
                            diagnosisTemp.setText("Body Temp. falls within 36.4 and 37.5 °C");
                            vitalPoints += 12.5;
                        }
                        else if (temp > 37.5)
                        {
                            diagnosisTemp.setText("Body Temp. is greater than 37.5 °C");
                        }
                        else
                        {
                            diagnosisTemp.setText("Body Temp. is less than 36.4 °C");
                        }

                        if (heart >= 60 && heart <= 100)
                        {
                            diagnosisHeart.setText("Heart Rate falls within 60 BPM and 100 BPM");
                            vitalPoints += 12.5;
                        }
                        else if (heart > 100)
                        {
                            diagnosisHeart.setText("Heart Rate is greater than 100 BPM");
                        }
                        else
                        {
                            diagnosisHeart.setText("Heart Rate is less than 60 BPM");
                        }

                        if (o2 >= 95)
                        {
                            diagnosisO2.setText("O2 Level is greater than 95%");
                            vitalPoints += 12.5;
                        }
                        else
                        {
                            diagnosisO2.setText("O2 Level is less than 95%");
                        }

                        if (glucose >= 80 && glucose <= 120)
                        {
                            diagnosisGlucose.setText("Blood Glucose Level falls within 80 mg/dl and 120 mg/dl");
                            vitalPoints += 12.5;
                        }
                        else if (glucose > 120)
                        {
                            diagnosisGlucose.setText("Blood Glucose Level is greater than 120 mg/dl");
                        }
                        else
                        {
                            diagnosisGlucose.setText("Blood Glucose level is less than 80 mg/dl");
                        }

                        bmi = weight / pow((height/100), 2);
                        if (bmi > 25 || bmi < 19)
                        {
                            diagnosisBMI.setText("BMI: " + String.valueOf(bmi) + "% (Unhealthy BMI)");
                        }
                        else
                        {
                            diagnosisBMI.setText("BMI: " + String.valueOf(bmi) + "% (Healthy BMI)");
                            bmiPoints = 10.0;
                        }

                        otherPoints = 5.0;
                        if (healthIssues.isEmpty())
                        {
                            otherPoints += 5.0;
                        }

                        symptomPoints = 30.0;

                        scoreBMI.setText(String.valueOf(bmiPoints));
                        scoreSymptom.setText(String.valueOf(symptomPoints));
                        scoreOther.setText(String.valueOf(otherPoints));
                        scoreVitals.setText(String.valueOf(vitalPoints));
                        totalPoints = vitalPoints + symptomPoints + bmiPoints + otherPoints;
                        scoreTotal.setText(String.valueOf(totalPoints));
                    }
                    else if (lastRecommendation.equals("Hypoglycemia"))
                    {
                        final Integer heart = snapshot.child(username).child("Last Heart").getValue(Integer.class);
                        final Integer o2 = snapshot.child(username).child("Last O2").getValue(Integer.class);
                        final Double glucose = snapshot.child(username).child("Last Glucose").getValue(Double.class);
                        final Double temp = snapshot.child(username).child("Last Temp").getValue(Double.class);

                        if (temp >= 36.4 && temp <= 37.5)
                        {
                            diagnosisTemp.setText("Body Temp. falls within 36.4 and 37.5 °C");
                            vitalPoints += 12.5;
                        }
                        else if (temp > 37.5)
                        {
                            diagnosisTemp.setText("Body Temp. is greater than 37.5 °C");
                        }
                        else
                        {
                            diagnosisTemp.setText("Body Temp. is less than 36.4 °C");
                        }

                        if (heart >= 60 && heart <= 100)
                        {
                            diagnosisHeart.setText("Heart Rate falls within 60 BPM and 100 BPM");
                            vitalPoints += 12.5;
                        }
                        else if (heart > 100)
                        {
                            diagnosisHeart.setText("Heart Rate is greater than 100 BPM");
                        }
                        else
                        {
                            diagnosisHeart.setText("Heart Rate is less than 60 BPM");
                        }

                        if (o2 >= 95)
                        {
                            diagnosisO2.setText("O2 Level is greater than 95%");
                            vitalPoints += 12.5;
                        }
                        else
                        {
                            diagnosisO2.setText("O2 Level is less than 95%");
                        }

                        if (glucose >= 80 && glucose <= 120)
                        {
                            diagnosisGlucose.setText("Blood Glucose Level falls within 80 mg/dl and 120 mg/dl");
                            vitalPoints += 12.5;
                        }
                        else if (glucose > 120)
                        {
                            diagnosisGlucose.setText("Blood Glucose Level is greater than 120 mg/dl");
                        }
                        else
                        {
                            diagnosisGlucose.setText("Blood Glucose level is less than 80 mg/dl");
                        }

                        bmi = weight / pow((height/100), 2);
                        if (bmi > 25 || bmi < 19)
                        {
                            diagnosisBMI.setText("BMI: " + String.valueOf(bmi) + "% (Unhealthy BMI)");
                        }
                        else
                        {
                            diagnosisBMI.setText("BMI: " + String.valueOf(bmi) + "% (Healthy BMI)");
                            bmiPoints = 10.0;
                        }

                        otherPoints = 5.0;
                        if (healthIssues.isEmpty())
                        {
                            otherPoints += 5.0;
                        }

                        symptomPoints = 30.0;

                        scoreBMI.setText(String.valueOf(bmiPoints));
                        scoreSymptom.setText(String.valueOf(symptomPoints));
                        scoreOther.setText(String.valueOf(otherPoints));
                        scoreVitals.setText(String.valueOf(vitalPoints));
                        totalPoints = vitalPoints + symptomPoints + bmiPoints + otherPoints;
                        scoreTotal.setText(String.valueOf(totalPoints));
                    }
                    else if (lastRecommendation.equals("Pneumonia"))
                    {
                        final Integer heart = snapshot.child(username).child("Last Heart").getValue(Integer.class);
                        final Integer o2 = snapshot.child(username).child("Last O2").getValue(Integer.class);
                        final Double glucose = snapshot.child(username).child("Last Glucose").getValue(Double.class);
                        final Double temp = snapshot.child(username).child("Last Temp").getValue(Double.class);

                        if (temp >= 36.4 && temp <= 37.5)
                        {
                            diagnosisTemp.setText("Body Temp. falls within 36.4 and 37.5 °C");
                            vitalPoints += 12.5;
                        }
                        else if (temp > 37.5)
                        {
                            diagnosisTemp.setText("Body Temp. is greater than 37.5 °C");
                        }
                        else
                        {
                            diagnosisTemp.setText("Body Temp. is less than 36.4 °C");
                        }

                        if (heart >= 60 && heart <= 100)
                        {
                            diagnosisHeart.setText("Heart Rate falls within 60 BPM and 100 BPM");
                            vitalPoints += 12.5;
                        }
                        else if (heart > 100)
                        {
                            diagnosisHeart.setText("Heart Rate is greater than 100 BPM");
                        }
                        else
                        {
                            diagnosisHeart.setText("Heart Rate is less than 60 BPM");
                        }

                        if (o2 >= 95)
                        {
                            diagnosisO2.setText("O2 Level is greater than 95%");
                            vitalPoints += 12.5;
                        }
                        else
                        {
                            diagnosisO2.setText("O2 Level is less than 95%");
                        }

                        if (glucose >= 80 && glucose <= 120)
                        {
                            diagnosisGlucose.setText("Blood Glucose Level falls within 80 mg/dl and 120 mg/dl");
                            vitalPoints += 12.5;
                        }
                        else if (glucose > 120)
                        {
                            diagnosisGlucose.setText("Blood Glucose Level is greater than 120 mg/dl");
                        }
                        else
                        {
                            diagnosisGlucose.setText("Blood Glucose level is less than 80 mg/dl");
                        }

                        bmi = weight / pow((height/100), 2);
                        if (bmi > 25 || bmi < 19)
                        {
                            diagnosisBMI.setText("BMI: " + String.valueOf(bmi) + "% (Unhealthy BMI)");
                        }
                        else
                        {
                            diagnosisBMI.setText("BMI: " + String.valueOf(bmi) + "% (Healthy BMI)");
                            bmiPoints = 10.0;
                        }

                        otherPoints = 5.0;
                        if (healthIssues.isEmpty())
                        {
                            otherPoints += 5.0;
                        }

                        symptomPoints = 30.0;

                        scoreBMI.setText(String.valueOf(bmiPoints));
                        scoreSymptom.setText(String.valueOf(symptomPoints));
                        scoreOther.setText(String.valueOf(otherPoints));
                        scoreVitals.setText(String.valueOf(vitalPoints));
                        totalPoints = vitalPoints + symptomPoints + bmiPoints + otherPoints;
                        scoreTotal.setText(String.valueOf(totalPoints));
                    }
                    else if (lastRecommendation.equals("Stress"))
                    {
                        final Integer heart = snapshot.child(username).child("Last Heart").getValue(Integer.class);
                        final Integer o2 = snapshot.child(username).child("Last O2").getValue(Integer.class);
                        final Double glucose = snapshot.child(username).child("Last Glucose").getValue(Double.class);
                        final Double temp = snapshot.child(username).child("Last Temp").getValue(Double.class);

                        if (temp >= 36.4 && temp <= 37.5)
                        {
                            diagnosisTemp.setText("Body Temp. falls within 36.4 and 37.5 °C");
                            vitalPoints += 12.5;
                        }
                        else if (temp > 37.5)
                        {
                            diagnosisTemp.setText("Body Temp. is greater than 37.5 °C");
                        }
                        else
                        {
                            diagnosisTemp.setText("Body Temp. is less than 36.4 °C");
                        }

                        if (heart >= 60 && heart <= 100)
                        {
                            diagnosisHeart.setText("Heart Rate falls within 60 BPM and 100 BPM");
                            vitalPoints += 12.5;
                        }
                        else if (heart > 100)
                        {
                            diagnosisHeart.setText("Heart Rate is greater than 100 BPM");
                        }
                        else
                        {
                            diagnosisHeart.setText("Heart Rate is less than 60 BPM");
                        }

                        if (o2 >= 95)
                        {
                            diagnosisO2.setText("O2 Level is greater than 95%");
                            vitalPoints += 12.5;
                        }
                        else
                        {
                            diagnosisO2.setText("O2 Level is less than 95%");
                        }

                        if (glucose >= 80 && glucose <= 120)
                        {
                            diagnosisGlucose.setText("Blood Glucose Level falls within 80 mg/dl and 120 mg/dl");
                            vitalPoints += 12.5;
                        }
                        else if (glucose > 120)
                        {
                            diagnosisGlucose.setText("Blood Glucose Level is greater than 120 mg/dl");
                        }
                        else
                        {
                            diagnosisGlucose.setText("Blood Glucose level is less than 80 mg/dl");
                        }

                        bmi = weight / pow((height/100), 2);
                        if (bmi > 25 || bmi < 19)
                        {
                            diagnosisBMI.setText("BMI: " + String.valueOf(bmi) + "% (Unhealthy BMI)");
                        }
                        else
                        {
                            diagnosisBMI.setText("BMI: " + String.valueOf(bmi) + "% (Healthy BMI)");
                            bmiPoints = 10.0;
                        }

                        otherPoints = 5.0;
                        if (healthIssues.isEmpty())
                        {
                            otherPoints += 5.0;
                        }

                        symptomPoints = 30.0;

                        scoreBMI.setText(String.valueOf(bmiPoints));
                        scoreSymptom.setText(String.valueOf(symptomPoints));
                        scoreOther.setText(String.valueOf(otherPoints));
                        scoreVitals.setText(String.valueOf(vitalPoints));
                        totalPoints = vitalPoints + symptomPoints + bmiPoints + otherPoints;
                        scoreTotal.setText(String.valueOf(totalPoints));
                    }
                    else if (lastRecommendation.equals("Arrhythmia"))
                    {
                        final Integer heart = snapshot.child(username).child("Last Heart").getValue(Integer.class);
                        final Integer o2 = snapshot.child(username).child("Last O2").getValue(Integer.class);
                        final Double glucose = snapshot.child(username).child("Last Glucose").getValue(Double.class);
                        final Double temp = snapshot.child(username).child("Last Temp").getValue(Double.class);

                        if (temp >= 36.4 && temp <= 37.5)
                        {
                            diagnosisTemp.setText("Body Temp. falls within 36.4 and 37.5 °C");
                            vitalPoints += 12.5;
                        }
                        else if (temp > 37.5)
                        {
                            diagnosisTemp.setText("Body Temp. is greater than 37.5 °C");
                        }
                        else
                        {
                            diagnosisTemp.setText("Body Temp. is less than 36.4 °C");
                        }

                        if (heart >= 60 && heart <= 100)
                        {
                            diagnosisHeart.setText("Heart Rate falls within 60 BPM and 100 BPM");
                            vitalPoints += 12.5;
                        }
                        else if (heart > 100)
                        {
                            diagnosisHeart.setText("Heart Rate is greater than 100 BPM");
                        }
                        else
                        {
                            diagnosisHeart.setText("Heart Rate is less than 60 BPM");
                        }

                        if (o2 >= 95)
                        {
                            diagnosisO2.setText("O2 Level is greater than 95%");
                            vitalPoints += 12.5;
                        }
                        else
                        {
                            diagnosisO2.setText("O2 Level is less than 95%");
                        }

                        if (glucose >= 80 && glucose <= 120)
                        {
                            diagnosisGlucose.setText("Blood Glucose Level falls within 80 mg/dl and 120 mg/dl");
                            vitalPoints += 12.5;
                        }
                        else if (glucose > 120)
                        {
                            diagnosisGlucose.setText("Blood Glucose Level is greater than 120 mg/dl");
                        }
                        else
                        {
                            diagnosisGlucose.setText("Blood Glucose level is less than 80 mg/dl");
                        }

                        bmi = weight / pow((height/100), 2);
                        if (bmi > 25 || bmi < 19)
                        {
                            diagnosisBMI.setText("BMI: " + String.valueOf(bmi) + "% (Unhealthy BMI)");
                        }
                        else
                        {
                            diagnosisBMI.setText("BMI: " + String.valueOf(bmi) + "% (Healthy BMI)");
                            bmiPoints = 10.0;
                        }

                        otherPoints = 5.0;
                        if (healthIssues.isEmpty())
                        {
                            otherPoints += 5.0;
                        }

                        symptomPoints = 30.0;

                        scoreBMI.setText(String.valueOf(bmiPoints));
                        scoreSymptom.setText(String.valueOf(symptomPoints));
                        scoreOther.setText(String.valueOf(otherPoints));
                        scoreVitals.setText(String.valueOf(vitalPoints));
                        totalPoints = vitalPoints + symptomPoints + bmiPoints + otherPoints;
                        scoreTotal.setText(String.valueOf(totalPoints));
                    }
                    else if (lastRecommendation.equals("Coronary Artery Disease"))
                    {
                        final Integer heart = snapshot.child(username).child("Last Heart").getValue(Integer.class);
                        final Integer o2 = snapshot.child(username).child("Last O2").getValue(Integer.class);
                        final Double glucose = snapshot.child(username).child("Last Glucose").getValue(Double.class);
                        final Double temp = snapshot.child(username).child("Last Temp").getValue(Double.class);

                        if (temp >= 36.4 && temp <= 37.5)
                        {
                            diagnosisTemp.setText("Body Temp. falls within 36.4 and 37.5 °C");
                            vitalPoints += 12.5;
                        }
                        else if (temp > 37.5)
                        {
                            diagnosisTemp.setText("Body Temp. is greater than 37.5 °C");
                        }
                        else
                        {
                            diagnosisTemp.setText("Body Temp. is less than 36.4 °C");
                        }

                        if (heart >= 60 && heart <= 100)
                        {
                            diagnosisHeart.setText("Heart Rate falls within 60 BPM and 100 BPM");
                            vitalPoints += 12.5;
                        }
                        else if (heart > 100)
                        {
                            diagnosisHeart.setText("Heart Rate is greater than 100 BPM");
                        }
                        else
                        {
                            diagnosisHeart.setText("Heart Rate is less than 60 BPM");
                        }

                        if (o2 >= 95)
                        {
                            diagnosisO2.setText("O2 Level is greater than 95%");
                            vitalPoints += 12.5;
                        }
                        else
                        {
                            diagnosisO2.setText("O2 Level is less than 95%");
                        }

                        if (glucose >= 80 && glucose <= 120)
                        {
                            diagnosisGlucose.setText("Blood Glucose Level falls within 80 mg/dl and 120 mg/dl");
                            vitalPoints += 12.5;
                        }
                        else if (glucose > 120)
                        {
                            diagnosisGlucose.setText("Blood Glucose Level is greater than 120 mg/dl");
                        }
                        else
                        {
                            diagnosisGlucose.setText("Blood Glucose level is less than 80 mg/dl");
                        }

                        bmi = weight / pow((height/100), 2);
                        if (bmi > 25 || bmi < 19)
                        {
                            diagnosisBMI.setText("BMI: " + String.valueOf(bmi) + "% (Unhealthy BMI)");
                        }
                        else
                        {
                            diagnosisBMI.setText("BMI: " + String.valueOf(bmi) + "% (Healthy BMI)");
                            bmiPoints = 10.0;
                        }

                        otherPoints = 5.0;
                        if (healthIssues.isEmpty())
                        {
                            otherPoints += 5.0;
                        }

                        symptomPoints = 30.0;

                        scoreBMI.setText(String.valueOf(bmiPoints));
                        scoreSymptom.setText(String.valueOf(symptomPoints));
                        scoreOther.setText(String.valueOf(otherPoints));
                        scoreVitals.setText(String.valueOf(vitalPoints));
                        totalPoints = vitalPoints + symptomPoints + bmiPoints + otherPoints;
                        scoreTotal.setText(String.valueOf(totalPoints));
                    }
                    else if (lastRecommendation.equals("Lung Cancer"))
                    {
                        final Integer heart = snapshot.child(username).child("Last Heart").getValue(Integer.class);
                        final Integer o2 = snapshot.child(username).child("Last O2").getValue(Integer.class);
                        final Double glucose = snapshot.child(username).child("Last Glucose").getValue(Double.class);
                        final Double temp = snapshot.child(username).child("Last Temp").getValue(Double.class);

                        if (temp >= 36.4 && temp <= 37.5)
                        {
                            diagnosisTemp.setText("Body Temp. falls within 36.4 and 37.5 °C");
                            vitalPoints += 12.5;
                        }
                        else if (temp > 37.5)
                        {
                            diagnosisTemp.setText("Body Temp. is greater than 37.5 °C");
                        }
                        else
                        {
                            diagnosisTemp.setText("Body Temp. is less than 36.4 °C");
                        }

                        if (heart >= 60 && heart <= 100)
                        {
                            diagnosisHeart.setText("Heart Rate falls within 60 BPM and 100 BPM");
                            vitalPoints += 12.5;
                        }
                        else if (heart > 100)
                        {
                            diagnosisHeart.setText("Heart Rate is greater than 100 BPM");
                        }
                        else
                        {
                            diagnosisHeart.setText("Heart Rate is less than 60 BPM");
                        }

                        if (o2 >= 95)
                        {
                            diagnosisO2.setText("O2 Level is greater than 95%");
                            vitalPoints += 12.5;
                        }
                        else
                        {
                            diagnosisO2.setText("O2 Level is less than 95%");
                        }

                        if (glucose >= 80 && glucose <= 120)
                        {
                            diagnosisGlucose.setText("Blood Glucose Level falls within 80 mg/dl and 120 mg/dl");
                            vitalPoints += 12.5;
                        }
                        else if (glucose > 120)
                        {
                            diagnosisGlucose.setText("Blood Glucose Level is greater than 120 mg/dl");
                        }
                        else
                        {
                            diagnosisGlucose.setText("Blood Glucose level is less than 80 mg/dl");
                        }

                        bmi = weight / pow((height/100), 2);
                        if (bmi > 25 || bmi < 19)
                        {
                            diagnosisBMI.setText("BMI: " + String.valueOf(bmi) + "% (Unhealthy BMI)");
                        }
                        else
                        {
                            diagnosisBMI.setText("BMI: " + String.valueOf(bmi) + "% (Healthy BMI)");
                            bmiPoints = 10.0;
                        }

                        otherPoints = 5.0;
                        if (healthIssues.isEmpty())
                        {
                            otherPoints += 5.0;
                        }

                        symptomPoints = 30.0;

                        scoreBMI.setText(String.valueOf(bmiPoints));
                        scoreSymptom.setText(String.valueOf(symptomPoints));
                        scoreOther.setText(String.valueOf(otherPoints));
                        scoreVitals.setText(String.valueOf(vitalPoints));
                        totalPoints = vitalPoints + symptomPoints + bmiPoints + otherPoints;
                        scoreTotal.setText(String.valueOf(totalPoints));
                    }
                    else if(lastRecommendation.equals("Breast Cancer"))
                    {
                        final Integer heart = snapshot.child(username).child("Last Heart").getValue(Integer.class);
                        final Integer o2 = snapshot.child(username).child("Last O2").getValue(Integer.class);
                        final Double glucose = snapshot.child(username).child("Last Glucose").getValue(Double.class);
                        final Double temp = snapshot.child(username).child("Last Temp").getValue(Double.class);

                        if (temp >= 36.4 && temp <= 37.5)
                        {
                            diagnosisTemp.setText("Body Temp. falls within 36.4 and 37.5 °C");
                            vitalPoints += 12.5;
                        }
                        else if (temp > 37.5)
                        {
                            diagnosisTemp.setText("Body Temp. is greater than 37.5 °C");
                        }
                        else
                        {
                            diagnosisTemp.setText("Body Temp. is less than 36.4 °C");
                        }

                        if (heart >= 60 && heart <= 100)
                        {
                            diagnosisHeart.setText("Heart Rate falls within 60 BPM and 100 BPM");
                            vitalPoints += 12.5;
                        }
                        else if (heart > 100)
                        {
                            diagnosisHeart.setText("Heart Rate is greater than 100 BPM");
                        }
                        else
                        {
                            diagnosisHeart.setText("Heart Rate is less than 60 BPM");
                        }

                        if (o2 >= 95)
                        {
                            diagnosisO2.setText("O2 Level is greater than 95%");
                            vitalPoints += 12.5;
                        }
                        else
                        {
                            diagnosisO2.setText("O2 Level is less than 95%");
                        }

                        if (glucose >= 80 && glucose <= 120)
                        {
                            diagnosisGlucose.setText("Blood Glucose Level falls within 80 mg/dl and 120 mg/dl");
                            vitalPoints += 12.5;
                        }
                        else if (glucose > 120)
                        {
                            diagnosisGlucose.setText("Blood Glucose Level is greater than 120 mg/dl");
                        }
                        else
                        {
                            diagnosisGlucose.setText("Blood Glucose level is less than 80 mg/dl");
                        }

                        bmi = weight / pow((height/100), 2);
                        if (bmi > 25 || bmi < 19)
                        {
                            diagnosisBMI.setText("BMI: " + String.valueOf(bmi) + "% (Unhealthy BMI)");
                        }
                        else
                        {
                            diagnosisBMI.setText("BMI: " + String.valueOf(bmi) + "% (Healthy BMI)");
                            bmiPoints = 10.0;
                        }

                        otherPoints = 5.0;
                        if (healthIssues.isEmpty())
                        {
                            otherPoints += 5.0;
                        }

                        symptomPoints = 30.0;

                        scoreBMI.setText(String.valueOf(bmiPoints));
                        scoreSymptom.setText(String.valueOf(symptomPoints));
                        scoreOther.setText(String.valueOf(otherPoints));
                        scoreVitals.setText(String.valueOf(vitalPoints));
                        totalPoints = vitalPoints + symptomPoints + bmiPoints + otherPoints;
                        scoreTotal.setText(String.valueOf(totalPoints));
                    }
                    else if(lastRecommendation.equals("Leukemia"))
                    {
                        final Integer heart = snapshot.child(username).child("Last Heart").getValue(Integer.class);
                        final Integer o2 = snapshot.child(username).child("Last O2").getValue(Integer.class);
                        final Double glucose = snapshot.child(username).child("Last Glucose").getValue(Double.class);
                        final Double temp = snapshot.child(username).child("Last Temp").getValue(Double.class);

                        if (temp >= 36.4 && temp <= 37.5)
                        {
                            diagnosisTemp.setText("Body Temp. falls within 36.4 and 37.5 °C");
                            vitalPoints += 12.5;
                        }
                        else if (temp > 37.5)
                        {
                            diagnosisTemp.setText("Body Temp. is greater than 37.5 °C");
                        }
                        else
                        {
                            diagnosisTemp.setText("Body Temp. is less than 36.4 °C");
                        }

                        if (heart >= 60 && heart <= 100)
                        {
                            diagnosisHeart.setText("Heart Rate falls within 60 BPM and 100 BPM");
                            vitalPoints += 12.5;
                        }
                        else if (heart > 100)
                        {
                            diagnosisHeart.setText("Heart Rate is greater than 100 BPM");
                        }
                        else
                        {
                            diagnosisHeart.setText("Heart Rate is less than 60 BPM");
                        }

                        if (o2 >= 95)
                        {
                            diagnosisO2.setText("O2 Level is greater than 95%");
                            vitalPoints += 12.5;
                        }
                        else
                        {
                            diagnosisO2.setText("O2 Level is less than 95%");
                        }

                        if (glucose >= 80 && glucose <= 120)
                        {
                            diagnosisGlucose.setText("Blood Glucose Level falls within 80 mg/dl and 120 mg/dl");
                            vitalPoints += 12.5;
                        }
                        else if (glucose > 120)
                        {
                            diagnosisGlucose.setText("Blood Glucose Level is greater than 120 mg/dl");
                        }
                        else
                        {
                            diagnosisGlucose.setText("Blood Glucose level is less than 80 mg/dl");
                        }

                        bmi = weight / pow((height/100), 2);
                        if (bmi > 25 || bmi < 19)
                        {
                            diagnosisBMI.setText("BMI: " + String.valueOf(bmi) + "% (Unhealthy BMI)");
                        }
                        else
                        {
                            diagnosisBMI.setText("BMI: " + String.valueOf(bmi) + "% (Healthy BMI)");
                            bmiPoints = 10.0;
                        }

                        otherPoints = 5.0;
                        if (healthIssues.isEmpty())
                        {
                            otherPoints += 5.0;
                        }

                        symptomPoints = 30.0;

                        scoreBMI.setText(String.valueOf(bmiPoints));
                        scoreSymptom.setText(String.valueOf(symptomPoints));
                        scoreOther.setText(String.valueOf(otherPoints));
                        scoreVitals.setText(String.valueOf(vitalPoints));
                        totalPoints = vitalPoints + symptomPoints + bmiPoints + otherPoints;
                        scoreTotal.setText(String.valueOf(totalPoints));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}