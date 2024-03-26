package com.mt.pdfviewer.main.admin;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mt.pdfviewer.R;
import com.mt.pdfviewer.databinding.ActivityAdminChinhSuaPdfBinding;
import com.mt.pdfviewer.model.CategoryModel;

import java.util.ArrayList;
import java.util.Objects;

public class AdminChinhSuaPdfActivity extends AppCompatActivity {
    private static final String TAG = "AdminChinhSuaPdfActivity";
    private ActivityAdminChinhSuaPdfBinding binding;
    private String idTruyen, idTheLoaiChon, tenTheLoaiChon;
    private FirebaseAuth firebaseAuth;
    private ArrayList<String> tenTLArrayList, idTLArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminChinhSuaPdfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Chỉnh sửa");

        if (firebaseAuth == null) {
            firebaseAuth = FirebaseAuth.getInstance();
        }

        idTruyen = getIntent().getStringExtra("idTruyen");

        layTheLoai();

    }

    private void layTheLoai() {
        tenTLArrayList = new ArrayList<>();
        idTLArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("TheLoai");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

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