package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://healthcare-c3b0a-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText id = findViewById(R.id.email_username);
        final EditText password = findViewById(R.id.password);

        final Button loginBtn = findViewById(R.id.signInBtn);
        final TextView signUpLink = findViewById(R.id.registerLink);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String loginId = id.getText().toString();
                final String loginPassword = password.getText().toString();

                if (loginId.isEmpty() || loginPassword.isEmpty())
                {
                    Toast.makeText(LoginActivity.this, "Please fill in all the fields.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(loginId))
                            {
                                final String getPassword = snapshot.child(loginId).child("Password").getValue(String.class);
                                final String getName = snapshot.child(loginId).child("Name").getValue(String.class);
                                final String getDOB = snapshot.child(loginId).child("DOB").getValue(String.class);
                                final String getEmail = snapshot.child(loginId).child("Email").getValue(String.class);
                                final String getSex = snapshot.child(loginId).child("Sex").getValue(String.class);
                                final String getHeight = String.valueOf(snapshot.child(loginId).child("Height").getValue(Double.class));
                                final String getWeight = String.valueOf(snapshot.child(loginId).child("Weight").getValue(Double.class));
                                final String getCurrentIssues = snapshot.child(loginId).child("Current Issues").getValue(String.class);
                                final String getCurrentMedication = snapshot.child(loginId).child("Current Medication").getValue(String.class);

                                if (getPassword.equals(loginPassword))
                                {

                                    Intent intent = new Intent(LoginActivity.this, Homepage.class);
                                    intent.putExtra("user", loginId);
                                    intent.putExtra("name", getName);
                                    intent.putExtra("dob", getDOB);
                                    intent.putExtra("email", getEmail);
                                    intent.putExtra("pwd", getPassword);
                                    intent.putExtra("sex", getSex);
                                    intent.putExtra("height", getHeight);
                                    intent.putExtra("weight", getWeight);
                                    intent.putExtra("issues", getCurrentIssues);
                                    intent.putExtra("medication", getCurrentMedication);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(LoginActivity.this, "Incorrect Password Entered.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this, "Please enter a Valid Username.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });

        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity1.class));
            }
        });

    }
}