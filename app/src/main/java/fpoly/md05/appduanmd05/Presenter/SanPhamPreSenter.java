package fpoly.md05.appduanmd05.Presenter;

import fpoly.md05.appduanmd05.Model.SanPhamModels;

public class SanPhamPreSenter implements ISanPham {
    private SanPhamModels sanPhamModels;
    private SanPhamView callback;

    public SanPhamPreSenter(SanPhamView callback) {
        this.callback = callback;
        sanPhamModels = new SanPhamModels(this);
    }

    public void HandlegetDataSanPham(){
        sanPhamModels.HandlegetDataSanPham();
    }

    @Override
    public void getDataSanPham(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong,
                               String kichco, Long type,String mausac) {
        callback.getDataSanPham(id,tensp,giatien,hinhanh,loaisp,mota,soluong,kichco,type,mausac);
    }

    @Override
    public void OnEmptyList() {
        callback.OnEmptyList();
    }
    // truyen dữ liệu qua màn hình
    @Override
    public void getDataSanPhamNB(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong, String kichco, Long type, String mausac) {
        callback.getDataSanPhamNB(id,tensp,giatien,hinhanh,loaisp,mota,soluong,kichco,type,mausac);
    }

    public void HandlegetDataSanPham(String loaisp,int type) {
        sanPhamModels.HandlegetDataSanPham(loaisp,type);
    }

    public void HandlegetDataSanPhamNB() {
        sanPhamModels.HandlegetDataSanPhamNoiBat();
    }
}
