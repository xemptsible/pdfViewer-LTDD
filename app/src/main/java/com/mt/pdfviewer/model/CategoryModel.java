package com.mt.pdfviewer.model;

public class CategoryModel {
    private String uid, theLoai;
    private long dauThoiGian;

    public CategoryModel() {
    }

    public CategoryModel(String uid, String theLoai, long dauThoiGian) {
        this.uid = uid;
        this.theLoai = theLoai;
        this.dauThoiGian = dauThoiGian;
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

    public long getDauThoiGian() {
        return dauThoiGian;
    }

    public void setDauThoiGian(long dauThoiGian) {
        this.dauThoiGian = dauThoiGian;
    }
}
