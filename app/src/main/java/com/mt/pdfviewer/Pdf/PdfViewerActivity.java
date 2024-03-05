package com.mt.pdfviewer.Pdf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.github.barteksc.pdfviewer.PDFView;
import com.mt.pdfviewer.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class PdfViewerActivity extends AppCompatActivity {

    ArrayList<File> pdfFiles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        PDFView pdfView = findViewById(R.id.pdfView);

        Intent intent = getIntent();
        Log.d("PdfViewerActivity", Objects.requireNonNull(intent.getStringExtra("pdfName")));

        pdfView.fromFile(new File(Objects.requireNonNull(intent.getStringExtra("pdfName")))).load();
    }
}