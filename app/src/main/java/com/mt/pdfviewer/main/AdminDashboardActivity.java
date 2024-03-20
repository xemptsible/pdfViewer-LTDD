package com.mt.pdfviewer.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mt.pdfviewer.R;
import com.mt.pdfviewer.auth.LoginActivity;
import com.mt.pdfviewer.databinding.ActivityAdminDashboardBinding;
import com.mt.pdfviewer.main.admin.AdminThemCategoryActivity;

import java.util.Objects;

public class AdminDashboardActivity extends AppCompatActivity {
    private ActivityAdminDashboardBinding binding;
    private FirebaseAuth firebaseAuth;
    private AlertDialog.Builder dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        binding = ActivityAdminDashboardBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.btnThemCategory.setOnClickListener(v -> {
            startActivity(new Intent(this, AdminThemCategoryActivity.class));
        });


        xacThucNguoiDung();

        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setTitle("Admin Dashboard");
    }
    private void xacThucNguoiDung() {
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
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