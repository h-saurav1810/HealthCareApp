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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class O2StatsFragment extends Fragment {

    TextView lastRecorded, minRecorded, avgRecorded, maxRecorded;
    List<String> xValues;
    LineChart lineChart;

    String formattedDate = "", formattedSevenDate = "", formattedSixDate = "", formattedFiveDate = "", formattedFourDate = "";
    String formattedThreeDate = "", formattedTwoDate = "";

    Integer min = 0;
    Integer max = 0;
    Integer avgTotal = 0;
    Integer avg = 0;


    Integer readDay1;
    Integer readDay2;
    Integer readDay3;
    Integer readDay4;
    Integer readDay5;
    Integer readDay6;
    Integer readDay7;


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
        View myInflatedView = inflater.inflate(R.layout.fragment_o2_stats, container, false);
        user = username;

        AppCompatButton heartStatsButton, o2StatsButton, tempStatsButton, glucoseStatsButton, weeklyButton, monthlyButton;

        lastRecorded = (TextView) myInflatedView.findViewById(R.id.lastO2);
        minRecorded = (TextView) myInflatedView.findViewById(R.id.minO2);
        avgRecorded = (TextView) myInflatedView.findViewById(R.id.avgO2);
        maxRecorded = (TextView) myInflatedView.findViewById(R.id.maxO2);

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

        lineChart = (LineChart) myInflatedView.findViewById(R.id.o2Plot);

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
                    final String lastO2Val = String.valueOf(snapshot.child(user).child("Last O2").getValue(Integer.class));
                    if (lastO2Val.equals("-1"))
                    {
                        final String replaceHeartVal = "----";
                        lastRecorded.setText(replaceHeartVal);
                    }
                    else
                    {
                        lastRecorded.setText(lastO2Val);
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
        readDay7 = 95;
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
                                        if (datasnap.hasChild("O2"))
                                        {
                                            Log.d(TAG, "I COME TO DAY7 FOR SURE");
                                            readDay7 = datasnap.child("O2").getValue(Integer.class);
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
        readDay6 = 95;
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
                                        if (datasnap.hasChild("O2"))
                                        {
                                            Log.d(TAG, "I COME TO DAY6 FOR SURE");
                                            readDay6 = datasnap.child("O2").getValue(Integer.class);
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
        readDay5 = 95;
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
                                        if (datasnap.hasChild("O2"))
                                        {
                                            Log.d(TAG, "I COME TO DAY5 FOR SURE");
                                            readDay5 = datasnap.child("O2").getValue(Integer.class);
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
        readDay4 = 95;
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
                                        if (datasnap.hasChild("O2"))
                                        {
                                            Log.d(TAG, "I COME TO DAY4 FOR SURE");
                                            readDay4 = datasnap.child("O2").getValue(Integer.class);
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
        readDay3 = 95;
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
                                        if (datasnap.hasChild("O2"))
                                        {
                                            Log.d(TAG, "I COME TO DAY3 FOR SURE");
                                            readDay3 = datasnap.child("O2").getValue(Integer.class);
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
        readDay2 = 95;
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
                                        if (datasnap.hasChild("O2"))
                                        {
                                            Log.d(TAG, "I COME TO DAY2 FOR SURE");
                                            readDay2 = datasnap.child("O2").getValue(Integer.class);
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
        readDay1 = 95;
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
                                        if (datasnap.hasChild("O2"))
                                        {
                                            Log.d(TAG, "I COME TO DAY1 FOR SURE");
                                            readDay1 = datasnap.child("O2").getValue(Integer.class);
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
        description.setText("O2 Saturation");
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
        yAxis.setAxisMaximum(100f);
        yAxis.setAxisMinimum(75f);
        yAxis.setAxisLineWidth(2f);
        yAxis.setAxisLineColor(Color.BLACK);
        yAxis.setLabelCount(5);

        if (readDay7 == null)
        {
            readDay7 = 88;
        }

        if (readDay6 == null)
        {
            readDay6 = 91;
        }

        if (readDay5 == null)
        {
            readDay5 = 79;
        }

        if (readDay4 == null)
        {
            readDay4 = 98;
        }

        if (readDay3 == null)
        {
            readDay3 = 92;
        }

        if (readDay2 == null)
        {
            readDay2 = 82;
        }

        if (readDay1 == null)
        {
            readDay1 = 86;
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
                                        if (datasnap.hasChild("O2"))
                                        {
                                            Log.d(TAG, "I COME TO DAY7 FOR SURE");
                                            final Integer day7 = datasnap.child("O2").getValue(Integer.class);
                                            avgTotal += day7;
                                            min = day7;
                                            max = day7;
                                            minRecorded.setText(String.valueOf(min));
                                            avg = avgTotal / 7;
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
                                min = 95;
                                max = 95;
                                avgTotal += 95;
                                Log.d(TAG, String.valueOf(avgTotal));
                                minRecorded.setText(String.valueOf(min));
                                avg = avgTotal / 7;
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
                                        if (datasnap.hasChild("O2"))
                                        {
                                            final Integer day6 = datasnap.child("O2").getValue(Integer.class);
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
                                            avg = avgTotal / 7;
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
                                avgTotal += 95;
                                if (min > 95)
                                {
                                    min = 95;
                                }
                                if (max < 95)
                                {
                                    max = 95;
                                }
                                Log.d(TAG, String.valueOf(avgTotal));
                                minRecorded.setText(String.valueOf(min));
                                avg = avgTotal / 7;
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
                                        if (datasnap.hasChild("O2"))
                                        {
                                            final Integer day5 = datasnap.child("O2").getValue(Integer.class);
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
                                            avg = avgTotal / 7;
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
                                avgTotal += 95;
                                if (min > 95)
                                {
                                    min = 95;
                                }
                                if (max < 95)
                                {
                                    max = 95;
                                }
                                Log.d(TAG, String.valueOf(avgTotal));
                                minRecorded.setText(String.valueOf(min));
                                avg = avgTotal / 7;
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
                                        if (datasnap.hasChild("O2"))
                                        {
                                            final Integer day4 = datasnap.child("O2").getValue(Integer.class);
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
                                            avg = avgTotal / 7;
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
                                avgTotal += 95;
                                if (min > 95)
                                {
                                    min = 95;
                                }
                                if (max < 95)
                                {
                                    max = 95;
                                }
                                Log.d(TAG, String.valueOf(avgTotal));
                                minRecorded.setText(String.valueOf(min));
                                avg = avgTotal / 7;
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
                                        if (datasnap.hasChild("O2"))
                                        {
                                            final Integer day3 = datasnap.child("O2").getValue(Integer.class);
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
                                            avg = avgTotal / 7;
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
                                avgTotal += 95;
                                if (min > 95)
                                {
                                    min = 95;
                                }
                                if (max < 95)
                                {
                                    max = 95;
                                }
                                Log.d(TAG, String.valueOf(avgTotal));
                                minRecorded.setText(String.valueOf(min));
                                avg = avgTotal / 7;
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
                                        if (datasnap.hasChild("Heart Rate"))
                                        {
                                            final Integer day2 = datasnap.child("O2").getValue(Integer.class);
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
                                            avg = avgTotal / 7;
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
                                avgTotal += 95;
                                if (min > 95)
                                {
                                    min = 95;
                                }
                                if (max < 95)
                                {
                                    max = 95;
                                }
                                Log.d(TAG, String.valueOf(avgTotal));
                                minRecorded.setText(String.valueOf(min));
                                avg = avgTotal / 7;
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
                                        if (datasnap.hasChild("O2"))
                                        {
                                            final Integer day = datasnap.child("O2").getValue(Integer.class);
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
                                            avg = avgTotal / 7;
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
                                avgTotal += 95;
                                if (min > 95)
                                {
                                    min = 95;
                                }
                                if (max < 95)
                                {
                                    max = 95;
                                }
                                Log.d(TAG, String.valueOf(avgTotal));
                                minRecorded.setText(String.valueOf(min));
                                avg = avgTotal / 7;
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