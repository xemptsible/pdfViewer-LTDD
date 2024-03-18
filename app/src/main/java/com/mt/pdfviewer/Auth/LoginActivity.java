package com.mt.pdfviewer.Auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.mt.pdfviewer.MainActivity;
import com.mt.pdfviewer.R;
import com.mt.pdfviewer.Utils;

public class LoginActivity extends AppCompatActivity {
    EditText edUsername, edMatKhau;
    Button btnDN;
    TextView btnDK;
    SharedPreferences preferences;
    Gson gson = new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = getSharedPreferences(Utils.PREF_APP, Context.MODE_PRIVATE);

        getSupportActionBar().setTitle("");

        edUsername = findViewById(R.id.edTenDN);
        edMatKhau = findViewById(R.id.edMatKhauDN);
        btnDN = findViewById(R.id.btnDN);
        btnDK = findViewById(R.id.btnChuyenSangDK);

        btnDK.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        btnDN.setOnClickListener(view -> {
            String thongTinNgDungSP = preferences.getString(Utils.KEY_USER, null);
            User userJson = gson.fromJson(thongTinNgDungSP, User.class);

            String username = edUsername.getText().toString();
            String password = edMatKhau.getText().toString();

            if (userJson == null) {
                Toast.makeText(this, "Tài khoản chưa tạo", Toast.LENGTH_SHORT).show();
            }
            if (edUsername.length() > 6 && edMatKhau.length() > 6 && coDungThongTin(username, password, userJson)){
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            else {
//                Toast.makeText(this, "Vui lòng nhập thông tin", Toast.LENGTH_LONG).show();
                edUsername.setError("Vui lòng nhập tên đăng nhập");
                edMatKhau.setError("Vui lòng nhập mật khẩu");
            };

            Log.d("LoginActivity", "Button is clicked. User: " + username + " Pass: " + password);
        });
    }

    private boolean coDungThongTin(String username, String password, User userJson) {
        return username.equals(userJson.getUsername()) || password.equals(userJson.getPassword());
    }
}