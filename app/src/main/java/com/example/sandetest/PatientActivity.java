package com.example.sandetest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SearchView;

public class PatientActivity extends Activity {

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        searchView = findViewById(R.id.searchView);

        searchView.setSubmitButtonEnabled(true);


    }
}
