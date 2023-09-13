package com.example.healthcare;

import static android.content.ContentValues.TAG;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GlucoseStatsFragment extends Fragment {

    TextView lastRecorded, minRecorded, avgRecorded, maxRecorded;
    List<String> xValues;
    LineChart lineChart;

    String formattedDate = "", formattedSevenDate = "", formattedSixDate = "", formattedFiveDate = "", formattedFourDate = "";
    String formattedThreeDate = "", formattedTwoDate = "";

    Double min = 0.0;
    Double max = 0.0;
    Double avgTotal = 0.0;
    Double avg = 0.0;

    Double readDay1;
    Double readDay2;
    Double readDay3;
    Double readDay4;
    Double readDay5;
    Double readDay6;
    Double readDay7;

    Float day1Val;
    Float day2Val;
    Float day3Val;
    Float day4Val;
    Float day5Val;
    Float day6Val;
    Float day7Val;

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
        View myInflatedView = inflater.inflate(R.layout.fragment_glucose_stats, container, false);
        user = username;

        AppCompatButton heartStatsButton, o2StatsButton, tempStatsButton, glucoseStatsButton, weeklyButton, monthlyButton;

        lastRecorded = (TextView) myInflatedView.findViewById(R.id.lastGlucose);
        minRecorded = (TextView) myInflatedView.findViewById(R.id.minGlucose);
        avgRecorded = (TextView) myInflatedView.findViewById(R.id.avgGlucose);
        maxRecorded = (TextView) myInflatedView.findViewById(R.id.maxGlucose);

        heartStatsButton = (AppCompatButton) myInflatedView.findViewById(R.id.heartbutton);
        o2StatsButton = (AppCompatButton) myInflatedView.findViewById(R.id.o2button);
        tempStatsButton = (AppCompatButton) myInflatedView.findViewById(R.id.tempbutton);
        glucoseStatsButton = (AppCompatButton) myInflatedView.findViewById(R.id.glucosebutton);

        LocalDateTime recordedDate, sevenDate, sixDate, fiveDate, fourDate, threeDate, twoDate;
        DateTimeFormatter dateFormat;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            recordedDate = LocalDateTime.now();
            sevenDate = LocalDateTime.now().minusDays(6);
            sixDate = LocalDateTime.now().minusDays(5);
            fiveDate = LocalDateTime.now().minusDays(4);
            fourDate = LocalDateTime.now().minusDays(3);
            threeDate = LocalDateTime.now().minusDays(2);
            twoDate = LocalDateTime.now().minusDays(1);
            dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            formattedDate = recordedDate.format(dateFormat);
            formattedSevenDate = sevenDate.format(dateFormat);
            formattedSixDate = sixDate.format(dateFormat);
            formattedFiveDate = fiveDate.format(dateFormat);
            formattedFourDate = fourDate.format(dateFormat);
            formattedThreeDate = threeDate.format(dateFormat);
            formattedTwoDate = twoDate.format(dateFormat);
        }

        lineChart = (LineChart) myInflatedView.findViewById(R.id.glucosePlot);

        getDay7();
        getDay6();
        getDay5();
        getDay4();
        getDay3();
        getDay2();
        getDay1();
        report();

        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(user))
                {
                    final String lastGlucoseVal = String.valueOf(snapshot.child(user).child("Last Glucose").getValue(Double.class));
                    if (lastGlucoseVal.equals("0.1"))
                    {
                        final String replaceTempVal = "------";
                        lastRecorded.setText(replaceTempVal);
                    }
                    else
                    {
                        lastRecorded.setText(lastGlucoseVal);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

    public void getDay7()
    {
        readDay7 = 96.5;
        databaseReference.child("Monitored Data").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(user)) {
                    Log.d(TAG, "I DO LOOK THROUGH DAY 7");

                    databaseReference.child("Monitored Data").child(user).child(formattedSevenDate).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                            if (datasnapshot.hasChildren()) {
                                Log.d(TAG, "I COME TO DAY7");
                                String time = datasnapshot.getValue().toString();
                                final String timestamp = time.substring(1, 9);
                                databaseReference.child("Monitored Data").child(user).child(formattedSevenDate).child(timestamp).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot datasnap) {
                                        if (datasnap.hasChild("Glucose"))
                                        {
                                            Log.d(TAG, "I COME TO DAY7 FOR SURE");
                                            readDay7 = datasnap.child("Glucose").getValue(Double.class);
                                            Log.d(TAG, String.valueOf(readDay7));
                                            createGraph();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getDay6()
    {
        readDay6 = 96.5;
        databaseReference.child("Monitored Data").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(user)) {
                    Log.d(TAG, "I DO LOOK THROUGH DAY 6");

                    databaseReference.child("Monitored Data").child(user).child(formattedSixDate).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                            if (datasnapshot.hasChildren()) {
                                Log.d(TAG, "I COME TO DAY6");
                                String time = datasnapshot.getValue().toString();
                                final String timestamp = time.substring(1, 9);
                                databaseReference.child("Monitored Data").child(user).child(formattedSixDate).child(timestamp).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot datasnap) {
                                        if (datasnap.hasChild("Glucose"))
                                        {
                                            Log.d(TAG, "I COME TO DAY6 FOR SURE");
                                            readDay6 = datasnap.child("Glucose").getValue(Double.class);
                                            Log.d(TAG, String.valueOf(readDay6));
                                            createGraph();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getDay5()
    {
        readDay5 = 96.5;
        databaseReference.child("Monitored Data").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(user)) {
                    Log.d(TAG, "I DO LOOK THROUGH DAY 5");

                    databaseReference.child("Monitored Data").child(user).child(formattedFiveDate).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                            if (datasnapshot.hasChildren()) {
                                Log.d(TAG, "I COME TO DAY5");
                                String time = datasnapshot.getValue().toString();
                                final String timestamp = time.substring(1, 9);
                                databaseReference.child("Monitored Data").child(user).child(formattedFiveDate).child(timestamp).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot datasnap) {
                                        if (datasnap.hasChild("Glucose"))
                                        {
                                            Log.d(TAG, "I COME TO DAY5 FOR SURE");
                                            readDay5 = datasnap.child("Glucose").getValue(Double.class);
                                            Log.d(TAG, String.valueOf(readDay5));
                                            createGraph();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getDay4()
    {
        readDay4 = 96.5;
        databaseReference.child("Monitored Data").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(user)) {
                    Log.d(TAG, "I DO LOOK THROUGH DAY 4");

                    databaseReference.child("Monitored Data").child(user).child(formattedFourDate).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                            if (datasnapshot.hasChildren()) {
                                Log.d(TAG, "I COME TO DAY4");
                                String time = datasnapshot.getValue().toString();
                                final String timestamp = time.substring(1, 9);
                                databaseReference.child("Monitored Data").child(user).child(formattedFourDate).child(timestamp).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot datasnap) {
                                        if (datasnap.hasChild("Glucose"))
                                        {
                                            Log.d(TAG, "I COME TO DAY4 FOR SURE");
                                            readDay4 = datasnap.child("Glucose").getValue(Double.class);
                                            Log.d(TAG, String.valueOf(readDay4));
                                            createGraph();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getDay3()
    {
        readDay3 = 96.5;
        databaseReference.child("Monitored Data").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(user)) {
                    Log.d(TAG, "I DO LOOK THROUGH DAY 3");

                    databaseReference.child("Monitored Data").child(user).child(formattedThreeDate).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                            if (datasnapshot.hasChildren()) {
                                Log.d(TAG, "I COME TO DAY3");
                                String time = datasnapshot.getValue().toString();
                                final String timestamp = time.substring(1, 9);
                                databaseReference.child("Monitored Data").child(user).child(formattedThreeDate).child(timestamp).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot datasnap) {
                                        if (datasnap.hasChild("Glucose"))
                                        {
                                            Log.d(TAG, "I COME TO DAY3 FOR SURE");
                                            readDay3 = datasnap.child("Glucose").getValue(Double.class);
                                            Log.d(TAG, String.valueOf(readDay3));
                                            createGraph();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getDay2()
    {
        readDay2 = 96.5;
        databaseReference.child("Monitored Data").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(user)) {
                    Log.d(TAG, "I DO LOOK THROUGH DAY 2");

                    databaseReference.child("Monitored Data").child(user).child(formattedTwoDate).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                            if (datasnapshot.hasChildren()) {
                                Log.d(TAG, "I COME TO DAY2");
                                String time = datasnapshot.getValue().toString();
                                final String timestamp = time.substring(1, 9);
                                databaseReference.child("Monitored Data").child(user).child(formattedTwoDate).child(timestamp).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot datasnap) {
                                        if (datasnap.hasChild("Glucose"))
                                        {
                                            Log.d(TAG, "I COME TO DAY2 FOR SURE");
                                            readDay2 = datasnap.child("Glucose").getValue(Double.class);
                                            Log.d(TAG, String.valueOf(readDay2));
                                            createGraph();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getDay1()
    {
        readDay1 = 96.5;
        databaseReference.child("Monitored Data").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(user)) {
                    Log.d(TAG, "I DO LOOK THROUGH DAY 1");

                    databaseReference.child("Monitored Data").child(user).child(formattedDate).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                            if (datasnapshot.hasChildren()) {
                                Log.d(TAG, "I COME TO DAY1");
                                String time = datasnapshot.getValue().toString();
                                final String timestamp = time.substring(1, 9);
                                databaseReference.child("Monitored Data").child(user).child(formattedDate).child(timestamp).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot datasnap) {
                                        if (datasnap.hasChild("Glucose"))
                                        {
                                            Log.d(TAG, "I COME TO DAY1 FOR SURE");
                                            readDay1 = datasnap.child("Glucose").getValue(Double.class);
                                            Log.d(TAG, readDay1+" Baby Shark");
                                            createGraph();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void createGraph()
    {
        Description description = new Description();
        description.setText("Blood Glucose");
        description.setPosition(150f, 15f);

        lineChart.setDescription(description);
        lineChart.getAxisRight().setDrawLabels(false);

        xValues = Arrays.asList(formattedSevenDate, formattedSixDate, formattedFiveDate, formattedFourDate, formattedThreeDate, formattedTwoDate, formattedDate);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xValues));
        xAxis.setLabelCount(7);
        xAxis.setGranularity(1f);

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setAxisMaximum(120f);
        yAxis.setAxisMinimum(80f);
        yAxis.setAxisLineWidth(2f);
        yAxis.setAxisLineColor(Color.BLACK);
        yAxis.setLabelCount(5);

        if (readDay7 == null)
        {
            readDay7 = 97.2;
        }

        if (readDay6 == null)
        {
            readDay6 = 96.8;
        }

        if (readDay5 == null)
        {
            readDay5 = 95.82;
        }

        if (readDay4 == null)
        {
            readDay4 = 104.95;
        }

        if (readDay3 == null)
        {
            readDay3 = 98.3;
        }

        if (readDay2 == null)
        {
            readDay2 = 88.49;
        }

        if (readDay1 == null)
        {
            readDay1 = 91.12;
        }

        day7Val = readDay7.floatValue();
        day6Val = readDay6.floatValue();
        day5Val = readDay5.floatValue();
        day4Val = readDay4.floatValue();
        day3Val = readDay3.floatValue();
        day2Val = readDay2.floatValue();
        day1Val = readDay1.floatValue();

        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, day7Val));
        entries.add(new Entry(1, day6Val));
        entries.add(new Entry(2, day5Val));
        entries.add(new Entry(3, day4Val));
        entries.add(new Entry(4, day3Val));
        entries.add(new Entry(5, day2Val));
        entries.add(new Entry(6, day1Val));

        LineDataSet dataSet = new LineDataSet(entries, user);
        dataSet.setColor(Color.BLUE);

        LineData lineData = new LineData(dataSet);

        lineChart.setData(lineData);
        lineChart.invalidate();

    }

    public void report()
    {
        final DecimalFormat df = new DecimalFormat("0.00");
        databaseReference.child("Monitored Data").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(user)) {

                    databaseReference.child("Monitored Data").child(user).child(formattedSevenDate).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                            if (datasnapshot.hasChildren()) {

                                String time = datasnapshot.getValue().toString();
                                final String timestamp = time.substring(1, 9);
                                databaseReference.child("Monitored Data").child(user).child(formattedSevenDate).child(timestamp).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot datasnap) {
                                        if (datasnap.hasChild("Glucose"))
                                        {
                                            Log.d(TAG, "I COME TO DAY7 FOR SURE");
                                            final Double day7 = datasnap.child("Glucose").getValue(Double.class);
                                            min = day7;
                                            max = day7;
                                            minRecorded.setText(String.valueOf(min));
                                            avg = Double.valueOf(df.format(avgTotal / 7));
                                            avgRecorded.setText(String.valueOf(avg));
                                            maxRecorded.setText(String.valueOf(max));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                            else
                            {
                                Log.d(TAG, "Day 7 Default");
                                min = 96.55;
                                max = 96.55;
                                avgTotal += 96.55;
                                Log.d(TAG, String.valueOf(avgTotal));
                                minRecorded.setText(String.valueOf(min));
                                avg = Double.valueOf(df.format(avgTotal / 7));;
                                avgRecorded.setText(String.valueOf(avg));
                                maxRecorded.setText(String.valueOf(max));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    databaseReference.child("Monitored Data").child(user).child(formattedSixDate).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                            if (datasnapshot.hasChildren()) {

                                String time = datasnapshot.getValue().toString();
                                final String timestamp = time.substring(1, 9);
                                databaseReference.child("Monitored Data").child(user).child(formattedSixDate).child(timestamp).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot datasnap) {
                                        if (datasnap.hasChild("Glucose"))
                                        {
                                            final Double day6 = datasnap.child("Glucose").getValue(Double.class);
                                            avgTotal += day6;
                                            if (day6 < min)
                                            {
                                                min = day6;
                                            }
                                            if (day6 > max)
                                            {
                                                max = day6;
                                            }
                                            minRecorded.setText(String.valueOf(min));
                                            avg = Double.valueOf(df.format(avgTotal / 7));;
                                            avgRecorded.setText(String.valueOf(avg));
                                            maxRecorded.setText(String.valueOf(max));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                            else
                            {
                                Log.d(TAG, "Day 6 Default");
                                avgTotal += 96.55;
                                if (min > 96.55)
                                {
                                    min = 96.55;
                                }
                                if (max < 96.55)
                                {
                                    max = 96.55;
                                }
                                Log.d(TAG, String.valueOf(avgTotal));
                                minRecorded.setText(String.valueOf(min));
                                avg = Double.valueOf(df.format(avgTotal / 7));;
                                avgRecorded.setText(String.valueOf(avg));
                                maxRecorded.setText(String.valueOf(max));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    databaseReference.child("Monitored Data").child(user).child(formattedFiveDate).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                            if (datasnapshot.hasChildren()) {

                                String time = datasnapshot.getValue().toString();
                                final String timestamp = time.substring(1, 9);
                                databaseReference.child("Monitored Data").child(user).child(formattedFiveDate).child(timestamp).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot datasnap) {
                                        if (datasnap.hasChild("Glucose"))
                                        {
                                            final Double day5 = datasnap.child("Glucose").getValue(Double.class);
                                            avgTotal += day5;
                                            if (day5 < min)
                                            {
                                                min = day5;
                                            }
                                            if (day5 > max)
                                            {
                                                max = day5;
                                            }
                                            minRecorded.setText(String.valueOf(min));
                                            avg = Double.valueOf(df.format(avgTotal / 7));;
                                            avgRecorded.setText(String.valueOf(avg));
                                            maxRecorded.setText(String.valueOf(max));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                            else
                            {
                                Log.d(TAG, "Day 5 Default");
                                avgTotal += 96.55;
                                if (min > 96.55)
                                {
                                    min = 96.55;
                                }
                                if (max < 96.55)
                                {
                                    max = 96.55;
                                }
                                Log.d(TAG, String.valueOf(avgTotal));
                                minRecorded.setText(String.valueOf(min));
                                avg = Double.valueOf(df.format(avgTotal / 7));;
                                avgRecorded.setText(String.valueOf(avg));
                                maxRecorded.setText(String.valueOf(max));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    databaseReference.child("Monitored Data").child(user).child(formattedFourDate).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                            if (datasnapshot.hasChildren()) {

                                String time = datasnapshot.getValue().toString();
                                final String timestamp = time.substring(1, 9);
                                databaseReference.child("Monitored Data").child(user).child(formattedFourDate).child(timestamp).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot datasnap) {
                                        if (datasnap.hasChild("Glucose"))
                                        {
                                            final Double day4 = datasnap.child("Glucose").getValue(Double.class);
                                            avgTotal += day4;
                                            if (day4 < min)
                                            {
                                                min = day4;
                                            }
                                            if (day4 > max)
                                            {
                                                max = day4;
                                            }
                                            minRecorded.setText(String.valueOf(min));
                                            avg = Double.valueOf(df.format(avgTotal / 7));;
                                            avgRecorded.setText(String.valueOf(avg));
                                            maxRecorded.setText(String.valueOf(max));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                            else
                            {
                                Log.d(TAG, "Day 4 Default");
                                avgTotal += 96.55;
                                if (min > 96.55)
                                {
                                    min = 96.55;
                                }
                                if (max < 96.55)
                                {
                                    max = 96.55;
                                }
                                Log.d(TAG, String.valueOf(avgTotal));
                                minRecorded.setText(String.valueOf(min));
                                avg = Double.valueOf(df.format(avgTotal / 7));;
                                avgRecorded.setText(String.valueOf(avg));
                                maxRecorded.setText(String.valueOf(max));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    databaseReference.child("Monitored Data").child(user).child(formattedThreeDate).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                            if (datasnapshot.hasChildren()) {

                                String time = datasnapshot.getValue().toString();
                                final String timestamp = time.substring(1, 9);
                                databaseReference.child("Monitored Data").child(user).child(formattedThreeDate).child(timestamp).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot datasnap) {
                                        if (datasnap.hasChild("Glucose"))
                                        {
                                            final Double day3 = datasnap.child("Glucose").getValue(Double.class);
                                            avgTotal += day3;
                                            if (day3 < min)
                                            {
                                                min = day3;
                                            }
                                            if (day3 > max)
                                            {
                                                max = day3;
                                            }
                                            minRecorded.setText(String.valueOf(min));
                                            avg = Double.valueOf(df.format(avgTotal / 7));;
                                            avgRecorded.setText(String.valueOf(avg));
                                            maxRecorded.setText(String.valueOf(max));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                            else
                            {
                                Log.d(TAG, "Day 3 Default");
                                avgTotal += 96.55;
                                if (min > 96.55)
                                {
                                    min = 96.55;
                                }
                                if (max < 96.55)
                                {
                                    max = 96.55;
                                }
                                Log.d(TAG, String.valueOf(avgTotal));
                                minRecorded.setText(String.valueOf(min));
                                avg = Double.valueOf(df.format(avgTotal / 7));;
                                avgRecorded.setText(String.valueOf(avg));
                                maxRecorded.setText(String.valueOf(max));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    databaseReference.child("Monitored Data").child(user).child(formattedTwoDate).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                            if (datasnapshot.hasChildren()) {

                                String time = datasnapshot.getValue().toString();
                                final String timestamp = time.substring(1, 9);
                                databaseReference.child("Monitored Data").child(user).child(formattedTwoDate).child(timestamp).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot datasnap) {
                                        if (datasnap.hasChild("Glucose"))
                                        {
                                            final Double day2 = datasnap.child("Glucose").getValue(Double.class);
                                            avgTotal += day2;
                                            if (day2 < min)
                                            {
                                                min = day2;
                                            }
                                            if (day2 > max)
                                            {
                                                max = day2;
                                            }
                                            minRecorded.setText(String.valueOf(min));
                                            avg = Double.valueOf(df.format(avgTotal / 7));;
                                            avgRecorded.setText(String.valueOf(avg));
                                            maxRecorded.setText(String.valueOf(max));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                            else
                            {
                                Log.d(TAG, "Day 2 Default");
                                avgTotal += 96.55;
                                if (min > 96.55)
                                {
                                    min = 96.55;
                                }
                                if (max < 96.55)
                                {
                                    max = 96.55;
                                }
                                Log.d(TAG, String.valueOf(avgTotal));
                                minRecorded.setText(String.valueOf(min));
                                avg = Double.valueOf(df.format(avgTotal / 7));;
                                avgRecorded.setText(String.valueOf(avg));
                                maxRecorded.setText(String.valueOf(max));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    databaseReference.child("Monitored Data").child(user).child(formattedDate).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                            if (datasnapshot.hasChildren()) {

                                String time = datasnapshot.getValue().toString();
                                final String timestamp = time.substring(1, 9);
                                databaseReference.child("Monitored Data").child(user).child(formattedDate).child(timestamp).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot datasnap) {
                                        if (datasnap.hasChild("Glucose"))
                                        {
                                            final Double day = datasnap.child("Glucose").getValue(Double.class);
                                            avgTotal += day;
                                            if (day < min)
                                            {
                                                min = day;
                                            }
                                            if (day > max)
                                            {
                                                max = day;
                                            }
                                            minRecorded.setText(String.valueOf(min));
                                            avg = Double.valueOf(df.format(avgTotal / 7));;
                                            avgRecorded.setText(String.valueOf(avg));
                                            maxRecorded.setText(String.valueOf(max));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                            else
                            {
                                Log.d(TAG, "Current Day Default");
                                avgTotal += 96.55;
                                if (min > 96.55)
                                {
                                    min = 96.55;
                                }
                                if (max < 96.55)
                                {
                                    max = 96.55;
                                }
                                Log.d(TAG, String.valueOf(avgTotal));
                                minRecorded.setText(String.valueOf(min));
                                avg = Double.valueOf(df.format(avgTotal / 7));;
                                avgRecorded.setText(String.valueOf(avg));
                                maxRecorded.setText(String.valueOf(max));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}