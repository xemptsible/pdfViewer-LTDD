package com.mt.pdfviewer.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mt.pdfviewer.R;
import com.mt.pdfviewer.databinding.ActivityRegisterBinding;
import com.mt.pdfviewer.databinding.ActivityUserDashboardBinding;

import java.util.Objects;

public class UserDashboardActivity extends AppCompatActivity {
    private ActivityUserDashboardBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //https://developer.android.com/topic/libraries/view-binding#activities
        //Tương ứng với findViewById
        binding = ActivityUserDashboardBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firebaseAuth = FirebaseAuth.getInstance();
        xacThucNguoiDung();

        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setTitle("Library");


    }

    private void xacThucNguoiDung() {
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