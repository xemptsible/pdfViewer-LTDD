package com.mt.pdfviewer;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.mt.pdfviewer.Auth.User;
import com.mt.pdfviewer.Pdf.PdfAdapter;
import com.mt.pdfviewer.Pdf.PdfUtils;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipe;
    private PdfAdapter pdfAdapter;
    public ArrayList<File> pdfFiles;
    public PdfUtils pdfUtils = new PdfUtils();
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        storageRuntimePermission();

        swipe = findViewById(R.id.swipeContainer);

        swipe.setOnRefreshListener(() -> {
            pdfAdapter.clear();
            swipe.setRefreshing(false);
            storageRuntimePermission();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_layout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.removePdf)
            Toast.makeText(this, "This was clicked",Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

    private void displayPdf() {
        RecyclerView pdfRv = findViewById(R.id.rvPdf);
        pdfRv.setHasFixedSize(true);
        pdfRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        pdfFiles = new ArrayList<>(pdfUtils.getPdf(Environment.getExternalStorageDirectory()));
        Log.d(TAG, String.valueOf(pdfFiles));

        pdfAdapter = new PdfAdapter(this, pdfFiles);
        pdfRv.setAdapter(pdfAdapter);
    }

    private void storageRuntimePermission() {
        Dexter.withContext(MainActivity.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        displayPdf();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(MainActivity.this, "Permission is required", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }
}