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
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.mt.pdfviewer.pdf.PdfAdapter;
import com.mt.pdfviewer.pdf.PdfUtils;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipe;
    private PdfAdapter pdfAdapter;
    public ArrayList<File> pdfFiles;
    private final PdfUtils pdfUtils = new PdfUtils();
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SearchView searchView = findViewById(R.id.searchView);
        swipe = findViewById(R.id.swipeContainer);
        storageRuntimePermission();

        swipe.setOnRefreshListener(() -> {
            pdfAdapter.xoaHet();
            swipe.setRefreshing(false);
            storageRuntimePermission();
            khoiTaoRV();
        });
        khoiTaoTimKiem(searchView);
    }

    private void khoiTaoTimKiem(SearchView searchView) {
//        Ném lỗi android.view.ViewRoot$CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.
//        https://stackoverflow.com/questions/5161951/android-only-the-original-thread-that-created-a-view-hierarchy-can-touch-its-vi
        runOnUiThread(() -> searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                pdfAdapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                pdfAdapter.getFilter().filter(newText);
                return true;
            }
        }));
    }

    private void khoiTaoRV() {
        RecyclerView pdfRv = findViewById(R.id.rvPdf);
        pdfRv.setHasFixedSize(true);
        pdfRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        pdfAdapter = new PdfAdapter(this, pdfFiles);
        pdfRv.setAdapter(pdfAdapter);
    }

    private void layPdfTrongThuMuc() {
        pdfFiles = new ArrayList<>(pdfUtils.layPdfTrongThuMuc(Environment.getExternalStorageDirectory()));
        Log.d(TAG, String.valueOf(pdfFiles));
    }

    private void storageRuntimePermission() {
        Dexter.withContext(MainActivity.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        layPdfTrongThuMuc();
                        khoiTaoRV();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_layout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.xoaPdf)
            Toast.makeText(this, "This was clicked",Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }
}