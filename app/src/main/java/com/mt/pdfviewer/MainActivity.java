package com.mt.pdfviewer;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipe;
    private ImageView pdfMenuBtn;
    private PdfAdapter pdfAdapter;
    private RecyclerView pdfRv;
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
        return super.onOptionsItemSelected(item);
    }


// Bấm giữ pdf để mở context menu
/*    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add("Testing");
        menu.add("Testing2");
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (Objects.equals(item.getTitle(), "Testing"))
            Toast.makeText(this, "Test", Toast.LENGTH_SHORT).show();
        else if (Objects.equals(item.getTitle(), "Testing2"))
            Toast.makeText(this, "Test2!", Toast.LENGTH_SHORT).show();
        return super.onContextItemSelected(item);
    }*/

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

    public ArrayList<File> findPdf(File file) {

        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();

        if (files != null) {
            for (File singleFile : files) {
                if (singleFile.isDirectory() && !singleFile.isHidden())
                    arrayList.addAll(findPdf(singleFile));
                else if (singleFile.getName().endsWith(".pdf"))
                    arrayList.add(singleFile);
            }
        }
        return arrayList;
    }

    private void displayPdf() {
        pdfRv = findViewById(R.id.rvPdf);
        pdfRv.setHasFixedSize(true);
        pdfRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        ArrayList<File> pdfFiles = new ArrayList<>(findPdf(Environment.getExternalStorageDirectory()));
        pdfAdapter = new PdfAdapter(this, pdfFiles);
        pdfRv.setAdapter(pdfAdapter);
    }
}