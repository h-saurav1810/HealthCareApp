package com.example.healthcare;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ChatbotFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Homepage home = (Homepage) getActivity();
        String username = home.getUsername();
        String password = home.getAccPassword();
        String name = home.getName();
        String birthdate = home.getBDate();
        String sex = home.getSex();
        String height = home.getHeight();
        String weight = home.getWeight();
        String currentIssues = home.getCurrentIssues();
        String currentMedication = home.getCurrentMedication();


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chatbot, container, false);
    }

}