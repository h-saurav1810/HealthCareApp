package com.example.healthcare;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

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

public class MediProfileFragment extends Fragment {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://healthcare-c3b0a-default-rtdb.firebaseio.com/");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Homepage home = (Homepage) getActivity();
        String username = home.getUsername();
        String name = home.getName();
        String sex = home.getSex();
        String height = home.getHeight();
        String weight = home.getWeight();
        String currentIssues = home.getCurrentIssues();
        String currentMedication = home.getCurrentMedication();

        // Inflate the layout for this fragment
        View myInflatedView = inflater.inflate(R.layout.fragment_medi_profile, container, false);

        ImageView logout;
        TextView regName, regUsername;
        EditText sexField, heightField, weightField, issuesField, medicationField;
        Button update;
        AppCompatButton personal, medical;

        regName = (TextView) myInflatedView.findViewById(R.id.profileName);
        regUsername = (TextView) myInflatedView.findViewById(R.id.profileUsername);
        sexField = (EditText) myInflatedView.findViewById(R.id.editSex);
        heightField = (EditText) myInflatedView.findViewById(R.id.editHeight);
        weightField = (EditText) myInflatedView.findViewById(R.id.editWeight);
        issuesField = (EditText) myInflatedView.findViewById(R.id.editIssues);
        medicationField = (EditText) myInflatedView.findViewById(R.id.editMedication);

        logout = (ImageView) myInflatedView.findViewById(R.id.logoutBtn);
        update = (Button) myInflatedView.findViewById(R.id.updateMedicalBtn);
        personal = (AppCompatButton) myInflatedView.findViewById(R.id.personalBtn);
        medical = (AppCompatButton) myInflatedView.findViewById(R.id.medicalBtn);

        regName.setText(name);
        regUsername.setText(username);
        sexField.setText(sex);
        heightField.setText(height);
        weightField.setText(weight);
        issuesField.setText(currentIssues);
        medicationField.setText(currentMedication);

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

                final String registerSex = sexField.getText().toString();
                final Double registerHeight = Double.valueOf(heightField.getText().toString());
                final Double registerWeight = Double.valueOf(weightField.getText().toString());
                final String registerIssues = issuesField.getText().toString();
                final String registerMedication = medicationField.getText().toString();

                if (registerSex.isEmpty())
                {
                    Toast.makeText(getActivity(), "Please fill in all the required fields.", Toast.LENGTH_SHORT).show();
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

                                Toast.makeText(getActivity(), "Medical Information Successfully Updated. Information will be refreshed once you Log Back In.", Toast.LENGTH_SHORT).show();
                                home.replaceFragment(new MediProfileFragment());

                            }
                            else
                            {
                                Toast.makeText(getActivity(), "Could not Update Medical Information.", Toast.LENGTH_SHORT).show();
                            }
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