package fpoly.md05.appduanmd05.View.Bill;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.util.ArrayList;

import fpoly.md05.appduanmd05.Adapter.SanPhamAdapter;
import fpoly.md05.appduanmd05.Model.HoaDonModels;
import fpoly.md05.appduanmd05.Model.SanPhamModels;
import fpoly.md05.appduanmd05.Presenter.GioHangPreSenter;
import fpoly.md05.appduanmd05.Presenter.GioHangView;
import fpoly.md05.appduanmd05.Presenter.HoaDonPreSenter;
import fpoly.md05.appduanmd05.Presenter.HoaDonView;
import fpoly.md05.appduanmd05.Presenter.NotificationHelper;
import fpoly.md05.appduanmd05.R;

public class ContentBillActivity extends AppCompatActivity implements GioHangView, HoaDonView {

    private Intent intent;

    private HoaDonModels hoaDonModels;

    private TextView txtmaHD, txthoten, txtdiachi, txtsdt, txttongtien, txtrangthai;
    private Toolbar toolbar;
    private ImageView hinhanh;
    private Button btncapnhat;
    private GioHangPreSenter gioHangPreSenter;
    private ArrayList<SanPhamModels> arrayList;
    private SanPhamAdapter sanPhamAdapter;
    private RecyclerView rcvBill;
    private HoaDonPreSenter hoaDonPreSenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_bill);
        InitWidget();
        Init();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("HoaDon").document(hoaDonModels.getId());

        docRef.addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                System.err.println("Lỗi lắng nghe: " + e);
                return;
            }

            if (snapshot != null && snapshot.exists()) {
                Long newStatus = snapshot.getLong("trangthai");
                String uid = snapshot.getString("UID");
                if (newStatus != null) {
                    if (newStatus == 1) {
                        sendNotification("Thông báo", "Đơn hàng "+uid+" đang xử lý");
                    } else if (newStatus == 2) {
                        sendNotification("Thông báo", "Đơn hàng "+uid+" đang được giao đến bạn");
                    } else if (newStatus == 3) {
                        sendNotification("Thông báo", "Đơn hàng "+uid+" đã được giao thành công");
                    } else if (newStatus == 4) {
                        sendNotification("Thông báo", "Đơn hàng "+uid+" đã bị hủy");
                    }
                }
            } else {
                System.out.println("Dữ liệu không tồn tại");
            }
        });
    }


    private void Init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Chi tiết hóa đơn");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        intent = getIntent();
        hoaDonModels = (HoaDonModels) intent.getSerializableExtra("HD");
        int type = intent.getIntExtra("TYPE", 0);

        txtdiachi.setText("Địa chỉ : "+hoaDonModels.getDiachi());
        txtmaHD.setText("Mã HD :"+hoaDonModels.getId());
        txthoten.setText("Họ tên : "+hoaDonModels.getHoten());
        txtsdt.setText("Liên hệ : "+hoaDonModels.getSdt());
        txttongtien.setText("Giá tiền: "+ NumberFormat.getNumberInstance().format(hoaDonModels.getTongtien()));

        switch ((int) hoaDonModels.getType()){
            case  1: txtrangthai.setText("Trạng Thái : Đang xử lý");break;
            case  2: txtrangthai.setText("Trạng Thái : Đang giao hàng");break;
            case  3: txtrangthai.setText("Trạng Thái : Giao Hàng Thành Công");break;
            case  4: txtrangthai.setText("Trạng Thái : Hủy Đơn Hàng");break;
        }
        gioHangPreSenter = new GioHangPreSenter(this);
        hoaDonPreSenter = new HoaDonPreSenter(this);
        arrayList = new ArrayList<>();

        if(type == 4){
            gioHangPreSenter.HandlegetDataCTHD(hoaDonModels.getId(),hoaDonModels.getUid());
        }else{
            gioHangPreSenter.HandlegetDataCTHD(hoaDonModels.getId());
        }

        btncapnhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiaLogUpDate();
            }
        });
    }

    private void DiaLogUpDate() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_update_trangthai);
        dialog.show();
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Spinner spiner = dialog.findViewById(R.id.spinerCapNhat);
        String[] s = {"Chọn Mục","Hủy Đơn Hàng"} ;
        ArrayAdapter arrayAdapter  = new ArrayAdapter(this, android.R.layout.simple_list_item_1,s);
        spiner.setAdapter( arrayAdapter);
        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    if(hoaDonModels.getType() <3){
                        hoaDonPreSenter.CapNhatTrangThai(4,hoaDonModels.getId());
                        dialog.cancel();
                    }else if(hoaDonModels.getType() == 4){
                        Toast.makeText(ContentBillActivity.this, "Đơn hàng đã hủy!", Toast.LENGTH_SHORT).show();
//                        sendNotification("Thông báo","Đơn hàng của bạn đã được hủy");

                    }else{
                        Toast.makeText(ContentBillActivity.this, "Đơn hàng bạn không thể hủy", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void InitWidget() {
        toolbar = findViewById(R.id.toolbar);
        txtdiachi = findViewById(R.id.txtdiachi);
        txthoten = findViewById(R.id.txthoten);
        txtrangthai = findViewById(R.id.txtrangthaidonhang);
        txtsdt = findViewById(R.id.txtsdt);
        txttongtien = findViewById(R.id.txttongtien);
        txtmaHD = findViewById(R.id.txtmaHD);
        rcvBill = findViewById(R.id.rcvSP);

        btncapnhat = findViewById(R.id.btncapnhat);
    }

    @Override
    public void OnSucess() {
        Toast.makeText(this, "Cập nhật thành công ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getDataSanPham(String id, String idsp, String tensp, Long giatien, String hinhanh, String loaisp, Long soluong, String kichco, Long type, String mausac) {
        arrayList.add(new SanPhamModels(id,idsp,tensp,giatien,hinhanh,loaisp,soluong,kichco,type,mausac));
        sanPhamAdapter = new SanPhamAdapter(this,arrayList,1);
        rcvBill.setLayoutManager(new LinearLayoutManager(this));
        rcvBill.setAdapter(sanPhamAdapter);
    }

    @Override
    public void getDataHD(String id, String uid, String diachi, String hoten, String ngaydat, String phuongthuc, String sdt, Long tongtien, Long type) {

    }

    @Override
    public void OnFail() {
        Toast.makeText(this, "Thất Bại ! Lỗi hệ thống bảo trì", Toast.LENGTH_SHORT).show();
    }
    public void sendNotification(String title, String message) {
        NotificationHelper.showNotification(this, title, message);
    }

}