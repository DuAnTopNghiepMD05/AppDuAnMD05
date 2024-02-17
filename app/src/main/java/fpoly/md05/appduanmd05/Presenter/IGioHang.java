package fpoly.md05.appduanmd05.Presenter;

public interface IGioHang {

    void getDataSanPham(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong, String kichco, Long type, String mausac);

    void OnSucess();

    void OnFail();
}
