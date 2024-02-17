package fpoly.md05.appduanmd05.Model;

import com.google.firebase.firestore.FirebaseFirestore;

import fpoly.md05.appduanmd05.Presenter.IGioHang;

public class GioHangModels {

    private FirebaseFirestore db;
    private String id;

    private String id_sanpham;

    private long soluong;

    private IGioHang callback;

    public GioHangModels() {

    }

    public GioHangModels(IGioHang callback) {
        this.callback = callback;
        db = FirebaseFirestore.getInstance();
    }

    public GioHangModels(String id, String id_sanpham, long soluong) {
        this.id = id;
        this.id_sanpham = id_sanpham;
        this.soluong = soluong;
    }

    public GioHangModels(String id_sanpham, long soluong) {
        this.id_sanpham = id_sanpham;
        this.soluong = soluong;///
    }
}
