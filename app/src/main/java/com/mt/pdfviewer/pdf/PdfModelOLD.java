package com.mt.pdfviewer.pdf;

import androidx.annotation.NonNull;

public class PdfModelOLD {
    private static int idDem = 0;
    private int id;
    private String title;

    public PdfModelOLD(String title) {
        this.id = idDem++;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void resetDemId() {
        idDem = 0;
    }

    @NonNull
    @Override
    public String toString() {
        return "ID: " + getId() +
                ", Name: " + getTitle();
    }
}
