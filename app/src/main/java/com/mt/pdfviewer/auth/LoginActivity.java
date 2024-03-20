package com.mt.pdfviewer.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mt.pdfviewer.databinding.ActivityLoginBinding;
import com.mt.pdfviewer.main.AdminDashboardActivity;
import com.mt.pdfviewer.main.UserDashboardActivity;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private FirebaseAuth firebaseAuth;
    private String email, matKhau;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View registerView = binding.getRoot();
        setContentView(registerView);

        firebaseAuth = FirebaseAuth.getInstance();

        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        binding.btnChuyenSangDK.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
        });

        binding.btnDN.setOnClickListener(v -> {
            xacThucData();
        });

    }

    private void xacThucData() {
        EditText edEmailDN = binding.edEmailDN,
                edMatKhau = binding.edMatKhauDN;

        email = edEmailDN.getText().toString().trim();
        matKhau = edMatKhau.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edEmailDN.setError("Vui lòng nhập email");
        }
        if (matKhau.isEmpty()) {
            edMatKhau.setError("Vui lòng nhập mật khẩu");
        }
        else {
            firebaseAuth.signInWithEmailAndPassword(email, matKhau)
                    .addOnSuccessListener(authResult -> {
                        kiemTraNguoiDung();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Đăng nhập không thành công. Lý do: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        }
    }

    private void kiemTraNguoiDung() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("NguoiDung");

        userRef.child(firebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String phanQuyen = snapshot.child("phanQuyen").getValue(String.class);
                            if (Objects.equals(phanQuyen, "user")) {
                                startActivity(new Intent(LoginActivity.this, UserDashboardActivity.class));
                                finish();
                            } else if (Objects.equals(phanQuyen, "admin")) {
                                startActivity(new Intent(LoginActivity.this, AdminDashboardActivity.class));
                                finish();
                            }
                        } else {
                            startActivity(new Intent(LoginActivity.this, LoginActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
