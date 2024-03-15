package fpoly.md05.appduanmd05.Presenter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;

import fpoly.md05.appduanmd05.Model.GioHangModels;
import fpoly.md05.appduanmd05.Model.SanPhamModels;

public class GioHangPreSenter implements IGioHang{

    private GioHangModels gioHangModels;
    private GioHangView callback;

    public GioHangPreSenter(GioHangView callback) {
        this.callback = callback;
        gioHangModels  = new GioHangModels(this);
    }


    public  void AddCart(String idsp, int soluong){
        gioHangModels.AddCart(idsp);
    }

    public  void  HandlegetDataGioHang(){
        gioHangModels.HandlegetDataGioHang();
    }
    public  void  HandlegetDataGioHang(String id){
        gioHangModels.HandlegetDataGioHang(id);
    }

    @Override
    public void getDataSanPham(String id, String id_sp, String tensp, Long giatien, String hinhanh, String loaisp, Long soluong, String kichco, Long type, String mausac) {
        callback.getDataSanPham(id,id_sp,tensp,giatien,hinhanh,loaisp,soluong,kichco,type,mausac);
    }

    public void HandleAddHoaDon(String ngaydat, String diachi, String hoten, String sdt, String phuongthuc, long tongtien, ArrayList<SanPhamModels> arrayList) {
        gioHangModels.HandleThanhToan(ngaydat,diachi,hoten,sdt,phuongthuc,tongtien,arrayList);
    }

    public void HandlegetDataCTHD(String id) {
        gioHangModels.HandleGetDataCTHD(id);

    }
    public void HandlegetDataCTHD(String id,String uid) {
        gioHangModels.HandleGetDataCTHD(id,uid);

    }
    public void UpdateSoLuongSanPham(String idSanPham, long soLuongMoi) {
        gioHangModels.UpdateSoLuongSanPham(idSanPham, soLuongMoi);
    }

    public void HandleUpdateSoLuongSanPhamTrongFirebase(String productId, long soLuongMoi) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference productRef = db.collection("SanPham").document(productId);

        db.runTransaction((Transaction.Function<Void>) transaction -> {
                    DocumentSnapshot snapshot = transaction.get(productRef);
                    long soLuongHienTai = snapshot.getLong("soluong");
                    long soLuongCapNhat = soLuongHienTai + soLuongMoi;
                    if (soLuongCapNhat < 0) soLuongCapNhat = 0; // Đảm bảo số lượng không bị âm
                    transaction.update(productRef, "soluong", soLuongCapNhat);
                    return null;
                }).addOnSuccessListener(aVoid -> Log.d(TAG, "Transaction success!"))
                .addOnFailureListener(e -> Log.w(TAG, "Transaction failure.", e));
    }



    @Override
    public void OnSucess() {
        callback.OnSucess();
    }

    @Override
    public void OnFail() {
        callback.OnFail();
    }
}
