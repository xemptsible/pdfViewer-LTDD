package com.mt.pdfviewer.Auth;

public class User {
    private String uid;
    private String tenTaiKhoan;
    private String email;
    private String matKhau;
    private String hinhCaNhan;
    private int laAdmin;
    private long dauThoiGian;

    public User() {

    }

    public User(String uid, String tenTaiKhoan, String email, String matKhau, String hinhCaNhan, int laAdmin, long dauThoiGian) {
        this.uid = uid;
        this.tenTaiKhoan = tenTaiKhoan;
        this.email = email;
        this.matKhau = matKhau;
        this.hinhCaNhan = hinhCaNhan;
        this.laAdmin = laAdmin;
        this.dauThoiGian = dauThoiGian;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTenTaiKhoan() {
        return tenTaiKhoan;
    }

    public void setTenTaiKhoan(String tenTaiKhoan) {
        this.tenTaiKhoan = tenTaiKhoan;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getHinhCaNhan() {
        return hinhCaNhan;
    }

    public void setHinhCaNhan(String hinhCaNhan) {
        this.hinhCaNhan = hinhCaNhan;
    }

    public int getLaAdmin() {
        return laAdmin;
    }

    public void setLaAdmin(int laAdmin) {
        this.laAdmin = laAdmin;
    }

    public long getDauThoiGian() {
        return dauThoiGian;
    }

    public void setDauThoiGian(long dauThoiGian) {
        this.dauThoiGian = dauThoiGian;
    }
}
