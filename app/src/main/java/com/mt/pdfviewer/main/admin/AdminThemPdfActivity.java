package com.mt.pdfviewer.main.admin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mt.pdfviewer.databinding.ActivityAdminThemPdfBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class AdminThemPdfActivity extends AppCompatActivity {
    private final static String TAG = "AdminThemPdfActivity";
    private final static int PDF_PICK_CODE = 1;
    private ActivityAdminThemPdfBinding binding;
    private ArrayList<String> categoryModelIds, categoryModelTheLoai;
    private Uri pdfUri = null;
    private String tenTruyen, moTa, idTheLoaiChon, tenTheLoaiChon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminThemPdfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Thêm truyện mới");

        taiTheLoaiVaoArray();

        binding.btnGanPdf.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("application/pdf");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Chọn PDF"), PDF_PICK_CODE);
        });

        binding.tvCategoryPicker.setOnClickListener(v -> taoAlertDialogChoPicker());
        binding.btnDangPdfMoi.setOnClickListener(v -> xacThucTruyen());
    }

    private void xacThucTruyen() {
        tenTruyen = binding.edTenPdf.getText().toString().trim();
        moTa = binding.edMoTaPdf.getText().toString().trim();

        if (tenTruyen.isEmpty()) {
            Toast.makeText(this, "Nhập tên truyện", Toast.LENGTH_LONG).show();
        }
        if (moTa.isEmpty()) {
            Toast.makeText(this, "Nhập mô tả", Toast.LENGTH_LONG).show();
        }
        if (tenTheLoaiChon == null) {
            Toast.makeText(this, "Chọn thể loại", Toast.LENGTH_LONG).show();
        }
        if (pdfUri == null) {
            Toast.makeText(this, "Chọn truyện để đăng", Toast.LENGTH_LONG).show();
        }
        else if (!tenTruyen.isEmpty() && !moTa.isEmpty() && !(tenTheLoaiChon == null)) {
            dangTruyenMoiLenStorage();
        }
    }

    private void dangTruyenMoiLenStorage() {
        Toast.makeText(this, "Đang thêm truyện mới...", Toast.LENGTH_LONG).show();
        String duongDanTen = "Truyen/" + tenTruyen;

        StorageReference reference = FirebaseStorage.getInstance().getReference(duongDanTen);
        reference.putFile(pdfUri)
                .addOnSuccessListener(taskSnapshot -> {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    uriTask.addOnSuccessListener(uri -> {
                        String duongUrlPdf = String.valueOf(uriTask.getResult());
                        dangTruyenLenDatabase(duongUrlPdf);
                        Log.d(TAG, "Đăng thành công vào storage");
                    });
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Thất bại. Lý do: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void taiTheLoaiVaoArray() {
        categoryModelIds = new ArrayList<>();
        categoryModelTheLoai = new ArrayList<>();
        DatabaseReference theLoaiArrayRef = FirebaseDatabase.getInstance().getReference("TheLoai");

        theLoaiArrayRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryModelIds.clear();
                categoryModelTheLoai.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String theLoaiId = Objects.requireNonNull(ds.child("uid").getValue()).toString();
                    String theLoaiTen = Objects.requireNonNull(ds.child("theLoai").getValue()).toString();

                    categoryModelIds.add(theLoaiId);
                    categoryModelTheLoai.add(theLoaiTen);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void taoAlertDialogChoPicker() {
        // Mảng có kích cỡ của thể loại được lấy từ FIREBASE
        String[] mangTheLoai = new String[categoryModelTheLoai.size()];
        for (int i = 0; i < categoryModelTheLoai.size(); i++) {
            mangTheLoai[i] = categoryModelTheLoai.get(i);
        }

        Log.d(TAG, "Test" + tenTheLoaiChon);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn thể loại")
                .setItems(mangTheLoai, (dialog, which) -> {
                    tenTheLoaiChon = categoryModelTheLoai.get(which);
                    idTheLoaiChon = categoryModelIds.get(which);

                    binding.tvCategoryPicker.setText(tenTheLoaiChon);
                    Log.d(TAG, "Thể loại được chọn: " + tenTheLoaiChon + " và id: " + idTheLoaiChon);
                })
                .show();
    }

    private void dangTruyenLenDatabase(String duongUrlPdf) {
        Log.d(TAG, "Đang đăng truyện vào realtime database");
        HashMap<String, Object> truyenHashMap = new HashMap<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Truyen");
        String uid = ref.push().getKey();
        truyenHashMap.put("uid", uid);
        truyenHashMap.put("tenTruyen", tenTruyen);
        truyenHashMap.put("moTa", moTa);
        truyenHashMap.put("theLoai_uid", idTheLoaiChon);
        truyenHashMap.put("duongUrlTruyen", duongUrlPdf);
        truyenHashMap.put("dauThoiGianCapNhat", System.currentTimeMillis());
        truyenHashMap.put("soLanXem", 0);

        ref.child(Objects.requireNonNull(uid))
                .setValue(truyenHashMap)
                .addOnSuccessListener(unused -> Toast.makeText(AdminThemPdfActivity.this, "Thêm truyện thành công!", Toast.LENGTH_LONG).show())
                .addOnFailureListener(e -> Toast.makeText(AdminThemPdfActivity.this, "Thất bại. Lý do: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PDF_PICK_CODE) {
            Log.d(TAG, "onActivityResult: PDF được chọn");

            if (data != null)
                pdfUri = data.getData();

            Log.d(TAG, "onActivityResult: Uri của PDF: " + pdfUri);
        }
        else {
            Log.d(TAG, "onActivityResult: Hủy chọn PDF");
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}