package com.mt.pdfviewer.Pdf;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.ParcelFileDescriptor;

import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.io.File;
import java.util.ArrayList;

public class PdfUtils {
    public ArrayList<File> layPdfTrongThuMuc(File file) {

        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();

        if (files != null) {
            for (File singleFile : files) {
                if (singleFile.isDirectory() && !singleFile.isHidden())
                    arrayList.addAll(layPdfTrongThuMuc(singleFile));
                else if (singleFile.getName().endsWith(".pdf"))
                    arrayList.add(singleFile);
            }
        }
        return arrayList;
    }
    // Lấy trang đầu tiên làm bìa PDF
    public static Bitmap xuatRaBitmap(File filePath, Context context) {
        Bitmap bmp = null;
        PdfiumCore pdfiumCore = new PdfiumCore(context);
        try {
            ParcelFileDescriptor fd = ParcelFileDescriptor.open(filePath, ParcelFileDescriptor.MODE_READ_ONLY);
            PdfDocument pdfDocument = pdfiumCore.newDocument(fd);
            pdfiumCore.openPage(pdfDocument, 0);
            int width = pdfiumCore.getPageWidthPoint(pdfDocument, 0);
            int height = pdfiumCore.getPageHeightPoint(pdfDocument, 0);
            bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            pdfiumCore.renderPageBitmap(pdfDocument, bmp, 0, 0, 0, width, height);
            pdfiumCore.closeDocument(pdfDocument);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }
}
