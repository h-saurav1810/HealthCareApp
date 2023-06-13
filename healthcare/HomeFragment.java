package com.example.healthcare;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Homepage home = (Homepage) getActivity();
        String username = home.getUsername();
        String name = home.getName();

        // Inflate the layout for this fragment
        View myInflatedView = inflater.inflate(R.layout.fragment_home, container, false);

        TextView welcome;

        welcome = (TextView) myInflatedView.findViewById(R.id.welcomeText);
        welcome.setText("Hello, " + name + "!");

        return myInflatedView;
    }
}