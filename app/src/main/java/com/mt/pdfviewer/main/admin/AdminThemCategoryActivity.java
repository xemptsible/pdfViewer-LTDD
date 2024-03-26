package com.mt.pdfviewer.main.admin;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mt.pdfviewer.R;
import com.mt.pdfviewer.databinding.ActivityAdminDashboardBinding;
import com.mt.pdfviewer.databinding.ActivityAdminThemCategoryBinding;

import java.util.HashMap;
import java.util.Objects;

public class AdminThemCategoryActivity extends AppCompatActivity {
    private ActivityAdminThemCategoryBinding binding;
    private FirebaseAuth firebaseAuth;
    private String theLoai;
    private EditText edTheLoaiMoi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminThemCategoryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ActionBar actionBar =getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Thêm thể loại mới");

        if (firebaseAuth == null) {
            firebaseAuth = FirebaseAuth.getInstance();
        }

        binding.btnDangCategoryMoi.setOnClickListener(v -> {
            dangTheLoaiMoi();
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void dangTheLoaiMoi() {

        edTheLoaiMoi = binding.edThemCategory;

        theLoai = edTheLoaiMoi.getText().toString().trim();
        if (theLoai.isEmpty()) {
            edTheLoaiMoi.setError("Vui lòng nhập tên thể loại");
        }
        else {
            HashMap<String, Object> theLoaiHashMap = new HashMap<>();

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("TheLoai");
            String uid = ref.push().getKey();
            theLoaiHashMap.put("uid", uid);
            theLoaiHashMap.put("theLoai", theLoai);
            theLoaiHashMap.put("dauThoiGianCapNhat", System.currentTimeMillis());

            ref.child(uid)
                    .setValue(theLoaiHashMap)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(AdminThemCategoryActivity.this, "Thêm thể loại thành công!", Toast.LENGTH_LONG).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AdminThemCategoryActivity.this, "Thất bại. Lý do: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        }
    }
}