package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class RegisterActivity1 extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://healthcare-c3b0a-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);

        final EditText name = findViewById(R.id.registerName);
        final EditText dob = findViewById(R.id.registerDOB);
        final EditText email = findViewById(R.id.registerEmail);
        final EditText username = findViewById(R.id.registerUsername);
        final EditText password = findViewById(R.id.registerPassword);
        final EditText conPassword = findViewById(R.id.confirmpassword);

        final Button continueBtn = findViewById(R.id.contButton);
        final TextView loginLink = findViewById(R.id.loginLink);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String registerName = name.getText().toString();
                final String registerDOB = dob.getText().toString();
                final String registerEmail = email.getText().toString();
                final String registerUsername = username.getText().toString();
                final String registerPassword = password.getText().toString();
                final String confirmedPassword = conPassword.getText().toString();

                if (registerName.isEmpty() || registerDOB.isEmpty() || registerEmail.isEmpty() || registerUsername.isEmpty() || registerPassword.isEmpty() || confirmedPassword.isEmpty())
                {
                    Toast.makeText(RegisterActivity1.this, "Please fill in all the fields.", Toast.LENGTH_SHORT).show();
                }
                else if (!registerPassword.equals(confirmedPassword))
                {
                    Toast.makeText(RegisterActivity1.this, "Passwords do not match. Enter the same password.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(registerUsername))
                            {
                                Toast.makeText(RegisterActivity1.this, "Username has already been registered", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                databaseReference.child("Users").child(registerUsername).child("Name").setValue(registerName);
                                databaseReference.child("Users").child(registerUsername).child("DOB").setValue(registerDOB);
                                databaseReference.child("Users").child(registerUsername).child("Email").setValue(registerEmail);
                                databaseReference.child("Users").child(registerUsername).child("Password").setValue(registerPassword);

                                Intent intent = new Intent(RegisterActivity1.this, RegisterActivity2.class);
                                intent.putExtra("keyUsername", registerUsername);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity1.this, LoginActivity.class));
            }
        });
    }
}