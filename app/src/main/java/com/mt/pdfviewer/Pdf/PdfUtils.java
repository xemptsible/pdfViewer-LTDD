package com.mt.pdfviewer.Pdf;

import java.io.File;
import java.util.ArrayList;

public class PdfUtils {
    public ArrayList<File> getPdf(File file) {

        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();

        if (files != null) {
            for (File singleFile : files) {
                if (singleFile.isDirectory() && !singleFile.isHidden())
                    arrayList.addAll(getPdf(singleFile));
                else if (singleFile.getName().endsWith(".pdf"))
                    arrayList.add(singleFile);
            }
        }
        return arrayList;
    }
}
