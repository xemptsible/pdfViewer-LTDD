package com.mt.pdfviewer.main.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mt.pdfviewer.databinding.ActivityAdminChinhSuaPdfBinding;

import java.util.ArrayList;
import java.util.Objects;

public class AdminChinhSuaPdfActivity extends AppCompatActivity {
    private static final String TAG = "AdminChinhSuaPdfActivity";
    private ActivityAdminChinhSuaPdfBinding binding;
    private String idTruyen, idTheLoaiChon, tenTheLoaiChon;
    private ArrayList<String> tenTLArrayList, idTLArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminChinhSuaPdfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Chỉnh sửa");

        idTruyen = getIntent().getStringExtra("idTruyen");

        layTheLoai();
        layTruyenChonDeChinhSua();

        binding.tvCategoryPickerChinhSua.setOnClickListener(v -> taoAlertDialogChoPicker());
        binding.btnXacNhanChinhSua.setOnClickListener(v -> {});
    }

    private void layTheLoai() {
        tenTLArrayList = new ArrayList<>();
        idTLArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("TheLoai");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                idTLArrayList.clear();
                tenTLArrayList.clear();
                for (DataSnapshot ds: snapshot.getChildren()) {
                    String uid = String.valueOf(ds.child("uid").getValue());
                    String theLoai = String.valueOf(ds.child("theLoai").getValue());

                    idTLArrayList.add(uid);
                    tenTLArrayList.add(theLoai);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void layTruyenChonDeChinhSua() {
        DatabaseReference refTruyen = FirebaseDatabase.getInstance().getReference("Truyen");
        refTruyen.child(idTruyen).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        idTheLoaiChon = snapshot.child("theLoai_uid").getValue(String.class);
                        String moTa = snapshot.child("moTa").getValue(String.class);
                        String tenTruyen = snapshot.child("tenTruyen").getValue(String.class);

                        binding.edTenPdfChinhSua.setText(tenTruyen);
                        binding.edMoTaPdfChinhSua.setText(moTa);

                        DatabaseReference refTheLoai = FirebaseDatabase.getInstance().getReference("TheLoai");
                        refTheLoai.child(idTheLoaiChon)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String theLoai = snapshot.child("theLoai").getValue(String.class);
                                        binding.tvCategoryPickerChinhSua.setText(theLoai);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void taoAlertDialogChoPicker() {
        // Mảng có kích cỡ của thể loại được lấy từ FIREBASE
        String[] mangTheLoai = new String[tenTLArrayList.size()];
        for (int i = 0; i < tenTLArrayList.size(); i++) {
            mangTheLoai[i] = tenTLArrayList.get(i);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn thể loại")
                .setItems(mangTheLoai, (dialog, which) -> {
                    tenTheLoaiChon = tenTLArrayList.get(which);
                    idTheLoaiChon = idTLArrayList.get(which);

                    binding.tvCategoryPickerChinhSua.setText(tenTheLoaiChon);
                    Log.d(TAG, "Thể loại được chọn: " + tenTheLoaiChon + " và id: " + idTheLoaiChon);
                })
                .show();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}