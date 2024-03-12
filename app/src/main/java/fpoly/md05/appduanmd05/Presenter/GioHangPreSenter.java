package fpoly.md05.appduanmd05.Presenter;

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


    public  void AddCart(String idsp){
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



    @Override
    public void OnSucess() {
        callback.OnSucess();
    }

    @Override
    public void OnFail() {
        callback.OnFail();
    }
}
