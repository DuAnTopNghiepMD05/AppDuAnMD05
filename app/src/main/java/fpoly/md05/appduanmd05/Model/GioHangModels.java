package fpoly.md05.appduanmd05.Model;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
        this.soluong = soluong;/////////////
    }

    public void AddCart(String idsp) {
        db.collection("GioHang")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("ALL")
                .whereEqualTo("id_sanpham", idsp)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.size() != 0) {
                            if (queryDocumentSnapshots.size() > 0) {
                                for (QueryDocumentSnapshot d : queryDocumentSnapshots) {
                                    long soluong_sp = d.getLong("soluong");
                                    if (soluong_sp > 0) {
                                        soluong_sp += 1;
                                        db.collection("GioHang")
                                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .collection("ALL")
                                                .document(d.getId())
                                                .update("soluong", soluong_sp)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            callback.OnSucess();
                                                        } else {
                                                            callback.OnFail();
                                                        }
                                                    }
                                                });
                                    }
                                }
                            }
                        } else {
                            GioHangModels gioHangModels = new GioHangModels(idsp, 1);
                            db.collection("GioHang")
                                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .collection("ALL")
                                    .add(gioHangModels)
                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            if (task.isSuccessful()) {
                                                callback.OnSucess();
                                            } else {
                                                callback.OnFail();
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    public void HandlegetDataGioHang() {
        db.collection("GioHang")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("ALL")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.size() > 0) {
                            for (QueryDocumentSnapshot s : queryDocumentSnapshots) {
                                db.collection("SanPham").document(s.getString("id_sanpham"))
                                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(@NonNull DocumentSnapshot d) {

                                                callback.getDataSanPham(s.getId(), s.getString("id_sanpham"), d.getString("tensp"),
                                                        d.getLong("giatien"), d.getString("hinhanh"),
                                                        d.getString("loaisp"),
                                                        s.getLong("soluong"), d.getString("nhasanxuat"),
                                                        d.getLong("type"), d.getString("mausac"));
                                            }
                                        });
                            }
                        }
                    }
                });
    }

    public void HandlegetDataGioHang(String id) {
        db.collection("GioHang")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("ALL")
                .document(id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            callback.OnSucess();
                        } else {
                            callback.OnFail();
                        }
                    }
                });
    }
}
