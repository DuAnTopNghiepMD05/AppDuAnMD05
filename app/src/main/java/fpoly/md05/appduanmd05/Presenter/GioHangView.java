package fpoly.md05.appduanmd05.Presenter;

public interface GioHangView {

    void OnSucess();

    void OnFail();

    void getDataSanPham(String id, String idsp,String tensp, Long giatien, String hinhanh, String loaisp, Long soluong, String kichco, Long type, String mausac);
}
