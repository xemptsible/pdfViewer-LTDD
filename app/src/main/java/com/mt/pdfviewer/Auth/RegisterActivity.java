package com.mt.pdfviewer.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mt.pdfviewer.R;
import com.mt.pdfviewer.Utils;

public class RegisterActivity extends AppCompatActivity {
    EditText edTenDK, edMK_DK, edXacNhanMK;
    Button btnDKMoi;
    SharedPreferences preferences;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("Register");

        edTenDK = findViewById(R.id.edTenDK);
        edMK_DK = findViewById(R.id.edMatKhauDK);
        edXacNhanMK = findViewById(R.id.edXacNhanMK_DK);

        btnDKMoi = findViewById(R.id.btnXacNhanDK);

        btnDKMoi.setOnClickListener(view -> {
            preferences = getSharedPreferences(Utils.PREF_APP, Context.MODE_PRIVATE);
            User user = new User();
            String usernameMoi = edTenDK.getText().toString();
            String matKhauMoi = edMK_DK.getText().toString();
            String xacNhanMK = edXacNhanMK.getText().toString();

            if (edTenDK.length() <=6) {
                edTenDK.setError("Tên đăng nhập phải dài hơn 6 ký tự");
            }
            if (edMK_DK.length() <=6 || edXacNhanMK.length() <=6) {
                edMK_DK.setError("Mật khẩu phải dài hơn 6 ký tự");
                edXacNhanMK.setError("Mật khẩu phải dài hơn 6 ký tự");
            }
            if (!matKhauMoi.equals(xacNhanMK)
                    && !matKhauMoi.isEmpty()
                    && !xacNhanMK.isEmpty()) {
                edMK_DK.setError("Mật khẩu không trùng");
                edXacNhanMK.setError("Mật khẩu không trùng");
            }
            else if (edTenDK.length() > 6
                    && matKhauMoi.equals(xacNhanMK)
                    && edMK_DK.length() > 6
                    && edXacNhanMK.length() > 6) {
                user.setUsername(usernameMoi);
                user.setPassword(xacNhanMK);
                luuGiuThongTinTK(user);
                quayVeLogin();
                finish();
            }
        });
    }
    private void luuGiuThongTinTK(User user) {
        SharedPreferences.Editor editor = preferences.edit();
        String userJson = gson.toJson(user);
        editor.putString(Utils.KEY_USER, userJson);
        editor.commit();
    }
    private void quayVeLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}