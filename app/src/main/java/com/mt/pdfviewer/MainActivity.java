package com.mt.pdfviewer;

import static java.lang.System.in;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.shockwave.pdfium.PdfDocument;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private PdfAdapter pdfAdapter;
//    private ArrayList<File> pdfFiles;
//    private ArrayList<Pdf> pdfFiles;
    ArrayList<Pdf> pdfFiles = new ArrayList<>();
    private RecyclerView pdfRv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        storageRuntimePermission();

        File docDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        findPdf(docDir);
        findPdf(downloadDir);
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

/*    public ArrayList<File> findPdf(File file) {

        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();

        for (File singleFile : files) {
            if (singleFile.isDirectory() && !singleFile.isHidden())
                arrayList.addAll(findPdf(singleFile));
            else if (singleFile.getName().endsWith(".pdf"))
                arrayList.add(singleFile);
        }
        return arrayList;
    }*/

    public void findPdf(File pdfDir) {
        Pdf pdfFile;


        File[] listFile = pdfDir.listFiles();

        if (listFile != null) {
            for (File file : listFile) {
                if (file.getName().endsWith(".pdf")) ;
                pdfFile = new Pdf();
                pdfFile.setFileName(file.getName());
                Log.d(TAG, pdfFile.getFileName());
                pdfFile.setFilePath(file.getAbsolutePath());
                pdfFile.setFileDateAdded(new Date(file.lastModified()));
                Log.d(TAG, pdfFile.getFileDateAdded().toString());
                pdfFile.setFileSize(file.length());
                Log.d(TAG, String.valueOf(pdfFile.getFileSize()));
                pdfFiles.add(pdfFile);
            }
        }
    }

    private void displayPdf() {
        pdfRv = findViewById(R.id.rvPdf);
        pdfRv.setHasFixedSize(true);
        pdfRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

//        pdfFiles = new ArrayList<>();
//        pdfFiles.addAll(findPdf(Environment.getExternalStorageDirectory()));
        pdfAdapter = new PdfAdapter(this, pdfFiles);
        pdfRv.setAdapter(pdfAdapter);
    }
}