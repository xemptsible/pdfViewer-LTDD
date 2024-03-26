package com.mt.pdfviewer.model;

public class PdfModel {
    private String uid, tenTruyen, moTa, theLoai_uid, duongUrlTruyen;
    private long dauThoiGian;

    public PdfModel() {
    }

    public PdfModel(String uid, String tenTruyen, String moTa, String theLoai_uid, String duongUrlTruyen, long dauThoiGian) {
        this.uid = uid;
        this.tenTruyen = tenTruyen;
        this.moTa = moTa;
        this.theLoai_uid = theLoai_uid;
        this.duongUrlTruyen = duongUrlTruyen;
        this.dauThoiGian = dauThoiGian;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTenTruyen() {
        return tenTruyen;
    }

    public void setTenTruyen(String tenTruyen) {
        this.tenTruyen = tenTruyen;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getTheLoai_uid() {
        return theLoai_uid;
    }

    public void setTheLoai_uid(String theLoai_uid) {
        this.theLoai_uid = theLoai_uid;
    }

    public long getDauThoiGian() {
        return dauThoiGian;
    }

    public void setDauThoiGian(long dauThoiGian) {
        this.dauThoiGian = dauThoiGian;
    }

    public String getDuongUrlTruyen() {
        return duongUrlTruyen;
    }

    public void setDuongUrlTruyen(String duongUrlTruyen) {
        this.duongUrlTruyen = duongUrlTruyen;
    }
}
