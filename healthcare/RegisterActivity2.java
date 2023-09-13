package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity2 extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://healthcare-c3b0a-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        String username = getIntent().getStringExtra("keyUsername");
        final EditText sex = findViewById(R.id.sex);
        final EditText measuredHeight = findViewById(R.id.height);
        final EditText measuredWeight = findViewById(R.id.weight);
        final EditText currentIssues = findViewById(R.id.issues);
        final EditText currentMedication = findViewById(R.id.medication);
        final EditText diseaseHistory = findViewById(R.id.medicalhistory);

        final Button registerBtn = findViewById(R.id.regBtn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String registerSex = sex.getText().toString();
                final Double registerHeight = Double.valueOf(measuredHeight.getText().toString());
                final Double registerWeight = Double.valueOf(measuredWeight.getText().toString());
                final String registerIssues = currentIssues.getText().toString();
                final String registerMedication = currentMedication.getText().toString();
                final String registerHistory = diseaseHistory.getText().toString();

                if (registerSex.isEmpty())
                {
                    Toast.makeText(RegisterActivity2.this, "Please fill in all the required fields.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(username))
                            {
                                databaseReference.child("Users").child(username).child("Sex").setValue(registerSex);
                                databaseReference.child("Users").child(username).child("Height").setValue(registerHeight);
                                databaseReference.child("Users").child(username).child("Weight").setValue(registerWeight);
                                databaseReference.child("Users").child(username).child("Model").setValue("001");
                                databaseReference.child("Users").child(username).child("Last Temp").setValue(0.1);
                                databaseReference.child("Users").child(username).child("Last Heart").setValue(-1);
                                databaseReference.child("Users").child(username).child("Last O2").setValue(-1);
                                databaseReference.child("Users").child(username).child("Last Glucose").setValue(0.1);
                                databaseReference.child("Users").child(username).child("Last Diagnosis").setValue("");
                                databaseReference.child("Users").child(username).child("Last Recommendation").setValue("");

                                if (!registerIssues.isEmpty())
                                {
                                    databaseReference.child("Users").child(username).child("Current Issues").setValue(registerIssues);
                                }
                                else
                                {
                                    databaseReference.child("Users").child(username).child("Current Issues").setValue("");
                                }

                                if (!registerMedication.isEmpty())
                                {
                                    databaseReference.child("Users").child(username).child("Current Medication").setValue(registerMedication);
                                }
                                else
                                {
                                    databaseReference.child("Users").child(username).child("Current Medication").setValue("");
                                }

                                if (!registerHistory.isEmpty())
                                {
                                    databaseReference.child("Users").child(username).child("Medical History").setValue(registerHistory);
                                }
                                else
                                {
                                    databaseReference.child("Users").child(username).child("Medical History").setValue("");
                                }

                                Toast.makeText(RegisterActivity2.this, "User Successfully Registered!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity2.this, LoginActivity.class));
                                finish();

                            }
                            else
                            {
                                Toast.makeText(RegisterActivity2.this, "Could not Update Medical Information.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });

    }
}