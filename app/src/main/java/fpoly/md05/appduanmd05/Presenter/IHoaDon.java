package fpoly.md05.appduanmd05.Presenter;

public interface IHoaDon {

    void OnSucess();

    void OnFail();

    void getDataHD(String id, String uid, String diachi, String hoten, String ngaydat, String phuongthuc, String sdt, Long tongtien, Long trangthai);
}
