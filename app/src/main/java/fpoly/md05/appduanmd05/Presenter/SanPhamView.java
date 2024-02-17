package fpoly.md05.appduanmd05.Presenter;

public interface SanPhamView {

    void getDataSanPham(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong,
                        String kichco, Long type,String mausac);
    void OnEmptyList();
}
