package com.mt.pdfviewer.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mt.pdfviewer.databinding.ActivityRegisterBinding;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private final static String REGISTER_TAG = "RegisterActivity";
    private ActivityRegisterBinding binding;
    private FirebaseAuth firebaseAuth;
    String ten, email, matKhau, xacNhanMK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View registerView = binding.getRoot();
        setContentView(registerView);

        // Khởi tạo Firebase
        firebaseAuth = FirebaseAuth.getInstance();

        binding.btnXacNhanDK.setOnClickListener(v -> xacNhanThongTin());
        binding.backImageBtn.setOnClickListener(v -> finish());
    }

    private void xacNhanThongTin() {
        // Ánh xạ dữ liệu
        EditText edTenDK = binding.edTenDK,
                edEmailDK = binding.edEmailDK,
                edMatKhauDK = binding.edMatKhauDK,
                edXacNhanMKDK = binding.edXacNhanMKDK;

        ten = edTenDK.getText().toString().trim();
        email = edEmailDK.getText().toString().trim();
        matKhau = edMatKhauDK.getText().toString().trim();
        xacNhanMK = edXacNhanMKDK.getText().toString().trim();

        // Xác thực thông tin
        if (ten.isEmpty()) {
            edTenDK.setError("Vui lòng nhập tên");
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.isEmpty()) {
            edEmailDK.setError("Vui lòng nhập email");
        }
        if (matKhau.isEmpty() || matKhau.length() < 6) {
            edMatKhauDK.setError("Vui lòng nhập mật khẩu từ 6 ký tự trở lên");
        }
        if (xacNhanMK.isEmpty()) {
            edXacNhanMKDK.setError("Vui lòng xác nhận mật khẩu");
        }
        else if (!matKhau.equals(xacNhanMK)) {
            edXacNhanMKDK.setError("Mật khẩu không trùng");
        }
        else {
            taoTaiKhoan();
        }
    }

    private void taoTaiKhoan() {
        Toast.makeText(this, "Đang tạo tài khoản", Toast.LENGTH_LONG).show();

        firebaseAuth
                .createUserWithEmailAndPassword(email, matKhau)
                .addOnSuccessListener(authResult -> {
                    taoTaiKhoanTrongFirebase();
                    Log.d(REGISTER_TAG, "Email added in Auth!");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Không thành công. Lý do: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void taoTaiKhoanTrongFirebase() {


        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("NguoiDung");
        String uid =firebaseAuth.getUid();
        HashMap<String, Object> userHashMap = new HashMap<>();
        userHashMap.put("uid", uid);
        userHashMap.put("email", email);
        userHashMap.put("matKhau", matKhau);
        userHashMap.put("tenNguoiDung", ten);
        userHashMap.put("hinhNguoiDung", "");

        if (ten.contains("admin") && email.contains("admin"))
        {
            userHashMap.put("phanQuyen", "admin");
        }
        else
        {
            userHashMap.put("phanQuyen", "user");
        }

        userRef
                .child(Objects.requireNonNull(uid))
                .setValue(userHashMap)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Tạo thể loại thành công!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(this, LoginActivity.class));
                    firebaseAuth.signOut();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Thất bại. Lý do: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}
