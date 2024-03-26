package fpoly.md05.appduanmd05.View.Bill;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

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
import fpoly.md05.appduanmd05.View.WebViewActivity;

public class ContentBillActivity extends AppCompatActivity implements GioHangView, HoaDonView {

    private Intent intent;

    private HoaDonModels hoaDonModels;

    private TextView txtmaHD, txthoten, txtdiachi, txtsdt, txttongtien, txtrangthai, txtphuongthuc;
    private Toolbar toolbar;
    private ImageView hinhanh;
    private Button btncapnhat;
    private GioHangPreSenter gioHangPreSenter;
    private ArrayList<SanPhamModels> arrayList;
    private SanPhamAdapter sanPhamAdapter;
    private RecyclerView rcvBill;
    private HoaDonPreSenter hoaDonPreSenter;
    private ProgressBar loadTT;

    Button payButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_bill);
        InitWidget();
        Init();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("HoaDon").document(hoaDonModels.getId());

        // Fetch the document to get the payment method
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    // Assuming 'phuongthuc' field holds the payment method
                    String paymentMethod = document.getString("phuongthuc");
                    String paymentStatus = document.getString("paymentStatus");
//                    if (paymentStatus == null) {
//                        paymentStatus = "";
//                    }
                    Log.d(TAG, "onCreate: " + "SUCCESS".equals(paymentStatus));
                    // Check if the payment method is VNPAY
                    if ("vnpay".equals(paymentMethod) && !"SUCCESS".equals(paymentStatus)) {
                        // Make the payment button visible
                         // Add your button ID here

                        payButton.setVisibility(View.VISIBLE);


                        // Optionally, set an onClickListener for the button to handle payment
                        payButton.setOnClickListener(v -> {
                            // Handle VNPAY payment here
                            Log.d("vnpayduc", "Payment method is VNPAY"+hoaDonModels.getId());
                            Log.d("vnpayduc", "Payment method is VNPAY"+hoaDonModels.getUid());
                            Log.d("vnpayduc", "Payment method is VNPAY"+hoaDonModels.getTongtien());

                            String url = "https://nggkm70j-5000.asse.devtunnels.ms/api/card-payment?service=vnpay";
                            RequestQueue queue = Volley.newRequestQueue(ContentBillActivity.this);

                            loadTT.setVisibility(View.VISIBLE);
// Tạo một JSONObject chứa thông tin cần gửi
                            JSONObject postData = new JSONObject();
                            try {
                                postData.put("amount", hoaDonModels.getTongtien());
                                postData.put("bankCode", "VNBANK");
                                postData.put("language", "vn");
                                postData.put("userId", hoaDonModels.getUid());
                                postData.put("billId", hoaDonModels.getId());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d(TAG, "onCreate: " + postData.toString());
// Tạo yêu cầu JSONObjectRequest
                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                    (Request.Method.POST, url, postData, new Response.Listener<JSONObject>() {

                                        @Override
                                        public void onResponse(JSONObject response) {
                                            // Xử lý phản hồi từ server
                                            JSONObject jsonObject = response;
                                            String url = null;
                                            try {
                                                url = jsonObject.getString("paymentUrl");
                                                Log.d(TAG, "onResponse: " + url);
                                                Intent intent = new Intent(ContentBillActivity.this, WebViewActivity.class);
                                                intent.putExtra("URL", url);
                                                startActivity(intent);
                                                loadTT.setVisibility(View.INVISIBLE);
                                                payButton.setVisibility(View.VISIBLE);
                                            } catch (JSONException e) {
                                                throw new RuntimeException(e);
                                            }


                                            // Bạn có thể thêm mã để xử lý phản hồi tại đây (ví dụ: mở WebView với URL thanh toán)
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            // Xử lý lỗi
                                            loadTT.setVisibility(View.INVISIBLE);
                                            payButton.setVisibility(View.VISIBLE);
                                            Log.d("Error.Response", error.toString());
                                        }
                                    });
// Thêm yêu cầu vào RequestQueue.
                            queue.add(jsonObjectRequest);
                            Log.d(TAG, "onCreate:sdfdsf " + String.valueOf(queue.getSequenceNumber()));
                            payButton.setVisibility(View.INVISIBLE);
                        });
                    }
                } else {
                    payButton.setVisibility(View.INVISIBLE);
                    Log.d("Document", "No data found");
                }
            } else {
                Log.d("Document", "get failed with ", task.getException());
            }
        });

        // Existing Firestore snapshot listener for document changes
        docRef.addSnapshotListener((snapshot, e) -> {
            // Your existing snapshot listener code...
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
        txtphuongthuc.setText("Phương thức thanh toán : "+hoaDonModels.getPhuongthuc());


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
        txtphuongthuc = findViewById(R.id.txtphuongthucthanhtoan);
        payButton = findViewById(R.id.your_payment_button_id);
        loadTT = findViewById(R.id.loadTT);
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