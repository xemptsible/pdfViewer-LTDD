package com.mt.pdfviewer.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mt.pdfviewer.R;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("Register");
    }
}