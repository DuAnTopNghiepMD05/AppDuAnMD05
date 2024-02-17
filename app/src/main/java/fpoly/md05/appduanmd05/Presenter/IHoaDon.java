package fpoly.md05.appduanmd05.Presenter;

public interface IHoaDon {

    void getDateHD(String id, String uid, String diachi, String hoten, String ngaydat, String phuongthuc, String sdt, Long tongtien, Long type);

    void OnSucess();

    void OnFail();
}
