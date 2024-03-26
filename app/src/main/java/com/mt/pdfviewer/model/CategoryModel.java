package com.mt.pdfviewer.model;

public class CategoryModel {
    private String theLoai, uid;

    public CategoryModel() {
    }

    public CategoryModel(String theLoai, String uid) {
        this.theLoai = theLoai;
        this.uid = uid;
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
