package fpoly.md05.appduanmd05.Model;

import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;

import fpoly.md05.appduanmd05.Presenter.ISanPham;

public class SanPhamModels implements Serializable {

    private FirebaseAuth firebaseAuth;

    private String id;

    private String idsp;

    private String tensp;

    private long giatien;

    private String hinhanh;

    private String laoisp;

    private String mota;

    private long soluong;

    private String kichco;

    private long type;

    private String mausac;

    private ISanPham callback;

    public SanPhamModels() {

    }

    public SanPhamModels(ISanPham callback) {
        this.callback = callback;
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public SanPhamModels(String id, String idsp, String tensp, long giatien, String hinhanh, String laoisp, String mota, long soluong, String kichco, long type, String mausac, ISanPham callback) {

        this.id = id;
        this.idsp = idsp;
        this.tensp = tensp;
        this.giatien = giatien;
        this.hinhanh = hinhanh;
        this.laoisp = laoisp;
        this.mota = mota;
        this.soluong = soluong;
        this.kichco = kichco;
        this.type = type;
        this.mausac = mausac;
        this.callback = callback;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdsp() {
        return idsp;
    }

    public void setIdsp(String idsp) {
        this.idsp = idsp;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public long getGiatien() {
        return giatien;
    }

    public void setGiatien(long giatien) {
        this.giatien = giatien;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public String getLaoisp() {
        return laoisp;
    }

    public void setLaoisp(String laoisp) {
        this.laoisp = laoisp;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public long getSoluong() {
        return soluong;
    }

    public void setSoluong(long soluong) {
        this.soluong = soluong;
    }

    public String getKichco() {
        return kichco;
    }

    public void setKichco(String kichco) {
        this.kichco = kichco;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public String getMausac() {
        return mausac;
    }

    public void setMausac(String mausac) {
        this.mausac = mausac;
    }

    public ISanPham getCallback() {
        return callback;
    }

    public void setCallback(ISanPham callback) {
        this.callback = callback;
    }


}
