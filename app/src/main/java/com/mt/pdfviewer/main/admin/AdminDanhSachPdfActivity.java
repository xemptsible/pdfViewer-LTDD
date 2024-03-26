package com.mt.pdfviewer.main.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mt.pdfviewer.adapter.PdfAdapterAdmin;
import com.mt.pdfviewer.databinding.ActivityAdminDanhSachPdfBinding;
import com.mt.pdfviewer.main.AdminDashboardActivity;
import com.mt.pdfviewer.model.PdfModel;

import java.util.ArrayList;
import java.util.Objects;

public class AdminDanhSachPdfActivity extends AppCompatActivity {

    private ActivityAdminDanhSachPdfBinding binding;
    private ArrayList<PdfModel> pdfModelArrayList;
    private PdfAdapterAdmin pdfAdapterAdmin;
    private SearchView searchViewTruyen;
    private RecyclerView rvTruyen;
    private String uidTheLoai, tenTheLoai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminDanhSachPdfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        rvTruyen = binding.rvPdfTrongTheLoai;
        searchViewTruyen = binding.svTruyenTrongTheLoai;

        uidTheLoai = getIntent().getStringExtra("uidTheLoai");
        tenTheLoai = getIntent().getStringExtra("tenTheLoai");

        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle(tenTheLoai);

        khoiTaoTimKiemTruyen();

        layTruyen();
    }

    private void khoiTaoTimKiemTruyen() {
        runOnUiThread(() -> searchViewTruyen.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                pdfAdapterAdmin.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                pdfAdapterAdmin.getFilter().filter(newText);
                return true;
            }
        }));
    }

    private void layTruyen() {
        pdfModelArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Truyen");
        // Sắp xếp vật thể mà có child đang kiếm
        ref.orderByChild("theLoai_uid").equalTo(uidTheLoai)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        pdfModelArrayList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            PdfModel pdfModel = ds.getValue(PdfModel.class);
                            pdfModelArrayList.add(pdfModel);
                        }
                        pdfAdapterAdmin = new PdfAdapterAdmin(AdminDanhSachPdfActivity.this, pdfModelArrayList);
                        rvTruyen.setAdapter(pdfAdapterAdmin);
                        rvTruyen.setHasFixedSize(true);
                        rvTruyen.setLayoutManager(new LinearLayoutManager(AdminDanhSachPdfActivity.this, LinearLayoutManager.VERTICAL, false));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Không có intent này thì bị lỗi
            // bởi vì cả activity này và activity Dashboard đều chiếm luồng UI lúc sử dụng SearchView
            Intent intent = new Intent(this, AdminDashboardActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}