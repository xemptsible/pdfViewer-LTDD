package com.mt.pdfviewer.model;

public class CategoryModel {
    private String uid, theLoai;

    public CategoryModel() {
    }

    public CategoryModel(String uid, String theLoai) {
        this.uid = uid;
        this.theLoai = theLoai;
    }

    public String getTheLoai() {
        return theLoai;
    }

    public void setTheLoai(String theLoai) {
        this.theLoai = theLoai;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
