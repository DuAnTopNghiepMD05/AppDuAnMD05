package fpoly.md05.appduanmd05.Model;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;

import fpoly.md05.appduanmd05.Presenter.ISanPham;

public class SanPhamModels implements Serializable {

    private FirebaseAuth firebaseAuth;

    private FirebaseFirestore db;


    private String id;

    private String idsp;

    private String tensp;

    private long giatien;

    private String hinhanh;

    private String loaisp;

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
        db = FirebaseFirestore.getInstance();
    }

    public SanPhamModels(String id, String id_sp, String tensp, long giatien, String hinhanh, String loaisp, long soluong, String kichco, long type, String mausac) {
        this.id = id;
        this.tensp = tensp;
        this.giatien = giatien;
        this.hinhanh = hinhanh;
        this.loaisp = loaisp;
        this.soluong = soluong;
        this.kichco = kichco;
        this.type = type;
        this.mausac = mausac;
        this.idsp = id_sp;
    }


    public SanPhamModels(String id, String tensp, long giatien, String hinhanh, String loaisp, String mota, long soluong, String kichco,
                         long type, String mausac) {
        this.id = id;
        this.tensp = tensp;
        this.giatien = giatien;
        this.hinhanh = hinhanh;
        this.loaisp = loaisp;
        this.mota = mota;
        this.soluong = soluong;
        this.kichco = kichco;
        this.type = type;
        this.mausac = mausac;
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
        return loaisp;
    }

    public void setLaoisp(String laoisp) {
        this.loaisp = laoisp;
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

    public void HandlegetDataSanPhamAll() {
        db.collection("SanPham")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.size() > 0) {
                            for (QueryDocumentSnapshot d : queryDocumentSnapshots) {

                                callback.getDataSanPham(d.getId(), d.getString("tensp"),
                                        d.getLong("giatien"), d.getString("hinhanh"),
                                        d.getString("loaisp"), d.getString("mota"),
                                        d.getLong("soluong"), d.getString("kichco"),
                                        d.getLong("type"), d.getString("mausac")
                                );
                            }
                        }

                    }
                });
    }


    public void HandlegetDataSanPham() {
        db.collection("SanPham")
                .whereEqualTo("type", 1)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.size() > 0) {
                            for (QueryDocumentSnapshot d : queryDocumentSnapshots) {

                                callback.getDataSanPham(d.getId(), d.getString("tensp"),
                                        d.getLong("giatien"), d.getString("hinhanh"),
                                        d.getString("loaisp"), d.getString("mota"),
                                        d.getLong("soluong"), d.getString("kichco"),
                                        d.getLong("type"), d.getString("mausac"));
                            }
                        }
                    }
                });
    }

    public void HandlegetDataSanPhamNoiBat() {
        db.collection("SanPham")
                .whereEqualTo("type", 2)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.size() > 0) {
                            for (QueryDocumentSnapshot d : queryDocumentSnapshots) {
                                // lấy id trên firebase
                                callback.getDataSanPhamNB(d.getId(), d.getString("tensp"),
                                        d.getLong("giatien"), d.getString("hinhanh"),
                                        d.getString("loaisp"), d.getString("mota"),
                                        d.getLong("soluong"), d.getString("kichco"),
                                        d.getLong("type"), d.getString("mausac"));
                            }
                        }
                    }
                });
    }

    public void HandlegetDataSanPham(String loaisp, int type) {

        String key = "";

        switch (type) {
            case 1:
                key = "loaisp";
                db.collection("SanPham")
                        .whereEqualTo(key, loaisp)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (queryDocumentSnapshots.size() > 0) {
                                    for (QueryDocumentSnapshot d : queryDocumentSnapshots) {

                                        callback.getDataSanPham(d.getId(), d.getString("tensp"),
                                                d.getLong("giatien"), d.getString("hinhanh"),
                                                d.getString("loaisp"), d.getString("mota"),
                                                d.getLong("soluong"), d.getString("kichco"),
                                                d.getLong("type"), d.getString("mausac"));
                                    }
                                } else {
                                    callback.OnEmptyList();
                                }
                            }
                        });
                break;

            case 2:
                key = "tensp";
                db.collection("SanPham")
                        .whereEqualTo(key, loaisp)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (queryDocumentSnapshots.size() > 0) {
                                    for (QueryDocumentSnapshot d : queryDocumentSnapshots) {

                                        callback.getDataSanPham(d.getId(), d.getString("tensp"),
                                                d.getLong("giatien"), d.getString("hinhanh"),
                                                d.getString("loaisp"), d.getString("mota"),
                                                d.getLong("soluong"), d.getString("kichco"),
                                                d.getLong("type"), d.getString("mausac"));
                                    }
                                } else {
                                    callback.OnEmptyList();
                                }
                            }
                        });
                break;
        }

    }
}
