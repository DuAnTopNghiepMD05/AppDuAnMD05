package fpoly.md05.appduanmd05.Presenter;

import fpoly.md05.appduanmd05.Model.GioHangModels;

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

    @Override
    public void OnSucess() {
        callback.OnSucess();
    }

    @Override
    public void OnFail() {
        callback.OnFail();
    }
}
