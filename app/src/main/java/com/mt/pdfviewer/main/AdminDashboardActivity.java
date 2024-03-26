package com.mt.pdfviewer.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mt.pdfviewer.R;
import com.mt.pdfviewer.adapter.CategoryAdapter;
import com.mt.pdfviewer.auth.LoginActivity;
import com.mt.pdfviewer.databinding.ActivityAdminDashboardBinding;
import com.mt.pdfviewer.main.admin.AdminThemCategoryActivity;
import com.mt.pdfviewer.main.admin.AdminThemPdfActivity;
import com.mt.pdfviewer.model.CategoryModel;

import java.util.ArrayList;
import java.util.Objects;

public class AdminDashboardActivity extends AppCompatActivity {
    private ActivityAdminDashboardBinding binding;
    private FirebaseAuth firebaseAuth;
    private RecyclerView rvCategory;
    private android.widget.SearchView searchView;
    private DatabaseReference theLoaiRef;
    private ArrayList<CategoryModel> categoryModels;
    private CategoryAdapter categoryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdminDashboardBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        rvCategory = binding.rvCategoryAdmin;
        searchView = binding.svCategory;

        binding.btnThemCategory.setOnClickListener(v -> {
            startActivity(new Intent(this, AdminThemCategoryActivity.class));
        });

        binding.btnThemSach.setOnClickListener((v -> {
            startActivity(new Intent(this, AdminThemPdfActivity.class));
        }));

        if (firebaseAuth == null) {
            firebaseAuth = FirebaseAuth.getInstance();
        }

        xacThucNguoiDung();
        taiTheLoai();

//        runOnUiThread(() -> searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                categoryAdapter.getFilter().filter(query);
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                categoryAdapter.getFilter().filter(newText);
//                return true;
//            }
//        }));

        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setTitle("Admin Dashboard");
    }
    private void xacThucNguoiDung() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void taiTheLoai() {
        categoryModels = new ArrayList<>();
        theLoaiRef = FirebaseDatabase.getInstance().getReference("TheLoai");
        theLoaiRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryModels.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    CategoryModel categoryModel = ds.getValue(CategoryModel.class);
                    categoryModels.add(categoryModel);
                }
                categoryAdapter = new CategoryAdapter(AdminDashboardActivity.this, categoryModels);
                rvCategory.setAdapter(categoryAdapter);
                rvCategory.setHasFixedSize(true);
                rvCategory.setLayoutManager(new LinearLayoutManager(AdminDashboardActivity.this, LinearLayoutManager.VERTICAL, false));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_layout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.dashboardLogout) {
            Toast.makeText(this, "This was clicked", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
            xacThucNguoiDung();
        }
        return super.onOptionsItemSelected(item);
    }
}