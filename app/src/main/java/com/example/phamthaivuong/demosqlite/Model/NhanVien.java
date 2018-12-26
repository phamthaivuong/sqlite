package com.example.phamthaivuong.demosqlite.Model;

public class NhanVien {
    public int id;
    public String ten;
    public String sdt;
    public String email;
    public byte[] anh;


    public NhanVien(int id, String ten, String sdt, String email, byte[] anh) {
        this.id = id;
        this.ten = ten;
        this.sdt = sdt;
        this.email = email;
        this.anh = anh;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getAnh() {
        return anh;
    }

    public void setAnh(byte[] anh) {
        this.anh = anh;
    }
}
