package com.example.healthcare;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://healthcare-c3b0a-default-rtdb.firebaseio.com/");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Homepage home = (Homepage) getActivity();
        String username = home.getUsername();
        String email = home.getAccEmail();
        String password = home.getAccPassword();
        String name = home.getName();
        String birthdate = home.getBDate();

        // Inflate the layout for this fragment
        View myInflatedView = inflater.inflate(R.layout.fragment_profile, container, false);

        ImageView logout;
        TextView regName, regUsername;
        EditText nameField, dobField, emailField, usernameField, passwordField;
        Button update;
        AppCompatButton personal, medical;

        regName = (TextView) myInflatedView.findViewById(R.id.profileName);
        regUsername = (TextView) myInflatedView.findViewById(R.id.profileUsername);
        nameField = (EditText) myInflatedView.findViewById(R.id.editName);
        dobField = (EditText) myInflatedView.findViewById(R.id.editDob);
        emailField = (EditText) myInflatedView.findViewById(R.id.editEmail);
        usernameField = (EditText) myInflatedView.findViewById(R.id.editUsername);
        passwordField = (EditText) myInflatedView.findViewById(R.id.editPassword);

        logout = (ImageView) myInflatedView.findViewById(R.id.logoutBtn);
        update = (Button) myInflatedView.findViewById(R.id.updatePersonalBtn);
        personal = (AppCompatButton) myInflatedView.findViewById(R.id.personalBtn);
        medical = (AppCompatButton) myInflatedView.findViewById(R.id.medicalBtn);

        regName.setText(name);
        regUsername.setText(username);
        nameField.setText(name);
        dobField.setText(birthdate);
        emailField.setText(email);
        usernameField.setText(username);
        passwordField.setText(password);
        usernameField.setEnabled(false);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.replaceFragment(new ProfileFragment());
            }
        });

        medical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.replaceFragment(new MediProfileFragment());
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String registerName = nameField.getText().toString();
                final String registerDOB = dobField.getText().toString();
                final String registerEmail = emailField.getText().toString();
                final String registerUsername = usernameField.getText().toString();
                final String registerPassword = passwordField.getText().toString();

                if (registerName.isEmpty() || registerDOB.isEmpty() || registerEmail.isEmpty() || registerUsername.isEmpty() || registerPassword.isEmpty())
                {
                    Toast.makeText(getActivity(), "Please fill in all the fields.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            databaseReference.child("Users").child(registerUsername).child("Name").setValue(registerName);
                            databaseReference.child("Users").child(registerUsername).child("DOB").setValue(registerDOB);
                            databaseReference.child("Users").child(registerUsername).child("Email").setValue(registerEmail);
                            databaseReference.child("Users").child(registerUsername).child("Password").setValue(registerPassword);

                            Toast.makeText(getActivity(), "Personal Information Successfully Updated. Information will be refreshed once you Log Back In.", Toast.LENGTH_SHORT).show();
                            home.replaceFragment(new ProfileFragment());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }
        });

        return myInflatedView;
    }

}