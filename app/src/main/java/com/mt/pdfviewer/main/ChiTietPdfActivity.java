package com.mt.pdfviewer.main;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mt.pdfviewer.databinding.ActivityChiTietPdfBinding;

import java.util.Objects;

public class ChiTietPdfActivity extends AppCompatActivity {
    private final static String TAG = "ChiTietPdfActivity";
    private ActivityChiTietPdfBinding binding;
    private String idTruyen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChiTietPdfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Chi tiết");

        idTruyen = getIntent().getStringExtra("idTruyen");

        layChiTietTruyen();

        binding.fabDocSach.setOnClickListener(v -> {
            Log.d(TAG, "Reading!");
        });
    }

    private void layChiTietTruyen() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Truyen");
        ref.child(idTruyen)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String tenTruyen = snapshot.child("tenTruyen").getValue(String.class);
                        String moTa = snapshot.child("moTa").getValue(String.class);
                        String theLoaiId = snapshot.child("theLoai_uid").getValue(String.class);
                        String url = snapshot.child("duongUrlTruyen").getValue(String.class);
                        String dauThoiGian = String.valueOf(snapshot.child("dauThoiGianCapNhat").getValue());

                        String dauThoiGianDinhDang = Utils.dinhDangThoiGian(Long.parseLong(dauThoiGian));

                        Utils.layTheLoaiPdf(theLoaiId, binding.tvTheLoaiChiTiet);
                        Utils.layBiaPdfTuDuongDan(url, tenTruyen, binding.biaTruyenChiTiet);
                        Utils.layKichCoPdf(url, binding.tvKichCoChiTiet);

                        binding.tvTenTruyen.setText(tenTruyen);
                        binding.tvMoTaChiTiet.setText(moTa);
                        binding.tvThoiGianCapNhatChiTiet.setText(dauThoiGianDinhDang);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}