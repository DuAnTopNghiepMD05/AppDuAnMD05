package fpoly.md05.appduanmd05.View.Bill;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import fpoly.md05.appduanmd05.Adapter.GioHangAdapter;
import fpoly.md05.appduanmd05.Model.SanPhamModels;
import fpoly.md05.appduanmd05.Presenter.GioHangPreSenter;
import fpoly.md05.appduanmd05.Presenter.GioHangView;
import fpoly.md05.appduanmd05.Presenter.NotificationHelper;
import fpoly.md05.appduanmd05.R;
import fpoly.md05.appduanmd05.View.HoanThanhActivity;
import fpoly.md05.appduanmd05.View.WebViewActivity;

public class CartActivity extends AppCompatActivity implements GioHangView {
    private RecyclerView rcVBill;
    private GioHangAdapter sanPhamAdapter;
    private GioHangPreSenter gioHangPreSenter;
    private ArrayList<SanPhamModels> arrayList;

    private Button btnthanhtoan;
    private  String s[]={"Thanh toán khi nhận hàng","Thanh toán MOMO"};
    private  long tongtien = 0;
    private ProgressBar progressBar;
    private  String hoten="",diachi="",sdt="";
    private Spinner spinner;
    private  int check =  0 ;
    private Toolbar toolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        InitWidget();
        Init();
    }

    private void Init() {
        arrayList = new ArrayList<>();
        gioHangPreSenter = new GioHangPreSenter(this);
        gioHangPreSenter.HandlegetDataGioHang();
        btnthanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arrayList.size()>0){
                    DiaLogThanhToan();
                }else{
                    Toast.makeText(CartActivity.this, "Sản phẩm không có trong giỏ hàng !", Toast.LENGTH_SHORT).show();
                }

            }
        });
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return true;
            }
            //chức năng xóa sp trong giỏ hàng
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                AlertDialog.Builder buidler = new AlertDialog.Builder(CartActivity.this);
                buidler.setMessage("Bạn có muôn xóa  sản phẩm "+arrayList.get(pos).getTensp());
                buidler.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SanPhamModels sanPham = arrayList.get(pos);
                        gioHangPreSenter.HandleUpdateSoLuongSanPhamTrongFirebase(sanPham.getIdsp(), sanPham.getSoluong());
                        gioHangPreSenter.HandlegetDataGioHang(arrayList.get(pos).getId());
                        // Giả sử bạn muốn giảm số lượng sản phẩm này
                        arrayList.remove(pos);
                        sanPhamAdapter.notifyDataSetChanged();
                        check = 1;
                    }
                });

                buidler.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sanPhamAdapter.notifyDataSetChanged();
                    }
                });
                buidler.show();
            }
            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;
                    Drawable icon;
                    if (dX > 0) {
                        // Vuốt sang phải
                        icon = ContextCompat.getDrawable(CartActivity.this, R.drawable.baseline_add_24);
                        icon.setBounds(
                                itemView.getLeft(),
                                itemView.getTop(),
                                itemView.getLeft() + icon.getIntrinsicWidth(),
                                itemView.getBottom()
                        );
                    } else {
                        // Vuốt sang trái
                        icon = ContextCompat.getDrawable(CartActivity.this, R.drawable.baseline_add_24);
                        icon.setBounds(
                                itemView.getRight() - icon.getIntrinsicWidth(),
                                itemView.getTop(),
                                itemView.getRight(),
                                itemView.getBottom()
                        );
                    }
                    icon.draw(c);
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(rcVBill);
    }


    private void DiaLogThanhToan() {

        Dialog dialog = new Dialog(CartActivity.this);
        dialog.setContentView(R.layout.dialog_thanhtoan);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        CustomInit(dialog);
    }

    private void CustomInit(Dialog dialog) {
        spinner = dialog.findViewById(R.id.spinerphguongthuc);
        EditText edithoten = dialog.findViewById(R.id.edithoten);
        EditText editdiachi = dialog.findViewById(R.id.editdiachi);
        EditText editsdt = dialog.findViewById(R.id.editsdt);
        Button btnxacnhan = dialog.findViewById(R.id.btnxacnhan);
        TextView txttongtien = dialog.findViewById(R.id.txttongtien);
        dialog.setCanceledOnTouchOutside(false);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,s);
        spinner.setAdapter(arrayAdapter);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        tongtien = 0;

        for (SanPhamModels sanPhamModels : arrayList){
            tongtien += sanPhamModels.getGiatien() * sanPhamModels.getSoluong();
        }

        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        txttongtien.setText("Tổng tiền: "+NumberFormat.getNumberInstance().format(tongtien)+" Đ");
        btnxacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hoten = edithoten.getText().toString();
                diachi = editdiachi.getText().toString();
                sdt = editsdt.getText().toString();
                if (hoten.length()>0){
                    if(diachi.length()>0){
                        if (sdt.length()>0){
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            Calendar calendar = Calendar.getInstance();
                            String ngaydat = simpleDateFormat.format(calendar.getTime());
                            String phuongthuc =spinner.getSelectedItem().toString();

                            switch (spinner.getSelectedItemPosition()){
                                case 0:
                                    gioHangPreSenter.HandleAddHoaDon(ngaydat,diachi,hoten,sdt,phuongthuc,tongtien,arrayList);
                                    dialog.cancel();break;
                                case 1:
                                    dialog.cancel();
                                    break;

                            }
                            sendNotification("Thông báo", "Đơn hàng của bạn đã được đặt thành công!");
                            startActivity(new Intent(CartActivity.this, HoanThanhActivity.class));
                            progressBar.setVisibility(View.VISIBLE);


                        }else{
                            Toast.makeText(CartActivity.this, "Số điện thoại không để trống", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(CartActivity.this, "Địa chỉ không để trống", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(CartActivity.this, "Họ tên không để trống", Toast.LENGTH_SHORT).show();
                }
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Kiểm tra xem mục được chọn có phải là "Thanh toán MOMO" không
                if (position == 1) { // Giả sử "Thanh toán MOMO" là mục thứ hai trong Spinner
                    // Tạo Intent để mở WebViewActivity
                    Intent intent = new Intent(CartActivity.this, WebViewActivity.class);

                    // Gửi URL thanh toán MOMO qua Intent. Bạn cần thay thế "YOUR_MOMO_PAYMENT_URL" với URL thực tế.
                    intent.putExtra("URL", "https://www.google.com/");

                    // Khởi chạy WebViewActivity
                    startActivity(intent);

                    // Đóng dialog
                    dialog.dismiss();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì nếu không có mục nào được chọn
            }
        });

    }
    //capnaht tien


    //Dien thong tin đặt hàn

    private void InitWidget() {
        rcVBill = findViewById(R.id.rcvBill);
        btnthanhtoan = findViewById(R.id.btnthanhtoan);
        progressBar= findViewById(R.id.progressbar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void calculateTotalAmount() {
        tongtien = 0; // Reset tổng tiền trước khi tính toán lại
        for (SanPhamModels sanPham : arrayList) {
            tongtien += sanPham.getGiatien() * sanPham.getSoluong();
        }
        // Định dạng số tiền để hiển thị cho phù hợp, ví dụ: định dạng tiền tệ
        String formattedTotal = NumberFormat.getNumberInstance().format(tongtien) + " Đ";
        // Cập nhật TextView hiển thị tổng tiền trong giao diện người dùng// Đảm bảo TextView được hiển thị
        runOnUiThread(() -> {
            TextView txtTotalAmount = findViewById(R.id.txtTotalAmount);
            txtTotalAmount.setText("Tổng tiền: " + formattedTotal + " Đ");
        });
    }
    
    @Override
    public void OnSucess() {
        if(check == 0){
            Toast.makeText(CartActivity.this, "Đặt Hàng Thành Công!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(CartActivity.this, "Thao tác thành công!", Toast.LENGTH_SHORT).show();
        }
        calculateTotalAmount();
        progressBar.setVisibility(View.GONE);
        sanPhamAdapter.notifyDataSetChanged();


    }

    @Override
    public void OnFail() {
        Toast.makeText(CartActivity.this, "Đặt Hàng thất bại !", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataFromFirestore();
    }

    private void loadDataFromFirestore() {
        // Xóa danh sách cũ
        arrayList.clear();

        // Lấy dữ liệu mới từ Firestore và cập nhật vào arrayList
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("GioHang")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            SanPhamModels sanPham = document.toObject(SanPhamModels.class);
                            arrayList.add(sanPham);
                        }

                        // Cập nhật adapter và giao diện người dùng
                        if (sanPhamAdapter != null) {
                            sanPhamAdapter.notifyDataSetChanged();
                            calculateTotalAmount(); // Cập nhật lại tổng tiền nếu cần
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    @Override
    public void getDataSanPham(String id, String idsp,String tensp, Long giatien, String hinhanh, String loaisp, Long soluong, String kichco, Long type, String mausac) {
        try{
            arrayList.add(new SanPhamModels(id,idsp,tensp,giatien,hinhanh,loaisp,soluong,kichco,type,mausac));
            sanPhamAdapter = new GioHangAdapter(CartActivity.this,arrayList,1);
            sanPhamAdapter.setCallback(new GioHangAdapter.AdapterCallback() {
                @Override
                public void onUpdateSoLuongSanPham(String idSanPham, long soLuongMoi) {
                    calculateTotalAmount();
                }

                @Override
                public void onUpdateTotalAmount(long totalAmount) {
                }
            });
            rcVBill.setLayoutManager(new LinearLayoutManager(CartActivity.this));
            rcVBill.setAdapter(sanPhamAdapter);
            calculateTotalAmount(); // Cập nhật tổng tiền ban đầu
        }catch (Exception e){

        }
        progressBar.setVisibility(View.GONE);
    }


    public void sendNotification(String title, String message) {
        NotificationHelper.showNotification(this, title, message);
    }


}