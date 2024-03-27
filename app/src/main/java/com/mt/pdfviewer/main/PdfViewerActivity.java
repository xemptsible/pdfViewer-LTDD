package com.mt.pdfviewer.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mt.pdfviewer.R;
import com.mt.pdfviewer.databinding.ActivityPdfViewerBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class PdfViewerActivity extends AppCompatActivity {
    private ActivityPdfViewerBinding binding;
    private String idTruyen, tenTruyen;
    private PDFView pdfView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPdfViewerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pdfView  = binding.pdfView;

        idTruyen = getIntent().getStringExtra("idTruyen");
        tenTruyen = getIntent().getStringExtra("tenTruyen");

        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle(tenTruyen);

        moPdfTuUrl();
    }

    private void moPdfTuUrl() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Truyen");

        dbRef.child(idTruyen)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String url = snapshot.child("duongUrlTruyen").getValue(String.class);

                        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(Objects.requireNonNull(url));
                        storageRef.getBytes(Utils.BYTE_ARRAY)
                                .addOnSuccessListener(bytes -> {
                                    pdfView.fromBytes(bytes)
                                            .swipeHorizontal(false)
                                            .pageSnap(true)
                                            .onError(throwable ->
                                                    Toast.makeText(
                                                            PdfViewerActivity.this,
                                                            "Thất bại. Lý do: " + throwable.getMessage(),
                                                            Toast.LENGTH_LONG).show())
                                            .onPageChange((i, i1) -> {
                                                int trangDoc = (i + 1);
                                                binding.soTrangPdfView.setText(trangDoc + "/" + i1);
                                            })
                                            .onPageError((i, throwable) -> {
                                                Toast.makeText(
                                                        PdfViewerActivity.this,
                                                        "Trang " + i +  " bị lỗi. Lý do: " + throwable.getMessage(),
                                                        Toast.LENGTH_LONG).show();
                                            })
                                            .load();

                                })
                                .addOnFailureListener(e -> {

                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}