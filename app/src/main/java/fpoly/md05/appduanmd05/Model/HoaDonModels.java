package fpoly.md05.appduanmd05.Model;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;

import fpoly.md05.appduanmd05.Presenter.IHoaDon;

public class HoaDonModels implements Serializable {

    private FirebaseFirestore db;

    private String id;

    private String uid;

    private String diachi;

    private String hoten;

    private String ngaydat;

    private String phuongthuc;

    private String sdt;

    private long tongtien;

    private long type;

    private IHoaDon callback;

    public HoaDonModels() {

    }

    public HoaDonModels(IHoaDon callback) {
        this.callback = callback;
        db = FirebaseFirestore.getInstance();
    }

    public HoaDonModels(FirebaseFirestore db, String id, String uid, String diachi, String hoten, String ngaydat, String phuongthuc, String sdt, long tongtien, long type, IHoaDon callback) {
        this.db = db;
        this.id = id;
        this.uid = uid;
        this.diachi = diachi;
        this.hoten = hoten;
        this.ngaydat = ngaydat;
        this.phuongthuc = phuongthuc;
        this.sdt = sdt;
        this.tongtien = tongtien;
        this.type = type;
        this.callback = callback;
    }
}
