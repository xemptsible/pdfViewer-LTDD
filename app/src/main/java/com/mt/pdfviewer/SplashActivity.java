package com.mt.pdfviewer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mt.pdfviewer.Auth.AdminDashboardActivity;
import com.mt.pdfviewer.Auth.LoginActivity;
import com.mt.pdfviewer.Auth.UserDashboardActivity;

import java.util.Objects;
import java.util.concurrent.Executor;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        firebaseAuth = FirebaseAuth.getInstance();

        new Handler(Looper.getMainLooper()).postDelayed(this::kiemTraUser, 1000);
    }

    private void kiemTraUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }
        else {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("NguoiDung");
            userRef.child(firebaseUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String phanQuyen = snapshot.child("phanQuyen").getValue(String.class);
                                if (Objects.equals(phanQuyen, "user")) {
                                    startActivity(new Intent(SplashActivity.this, UserDashboardActivity.class));
                                    finish();
                                } else if (Objects.equals(phanQuyen, "admin")) {
                                    startActivity(new Intent(SplashActivity.this, AdminDashboardActivity.class));
                                    finish();
                                }
                            } else {
                                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }
}