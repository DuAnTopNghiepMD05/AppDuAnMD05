package fpoly.md05.appduanmd05.View.Bill;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static java.security.AccessController.getContext;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import fpoly.md05.appduanmd05.Adapter.GioHangAdapter;
import fpoly.md05.appduanmd05.Adapter.SanPhamAdapter;
import fpoly.md05.appduanmd05.Model.SanPhamModels;
import fpoly.md05.appduanmd05.Presenter.GioHangPreSenter;
import fpoly.md05.appduanmd05.Presenter.GioHangView;
import fpoly.md05.appduanmd05.Presenter.NotificationHelper;
import fpoly.md05.appduanmd05.Presenter.SanPhamPreSenter;
import fpoly.md05.appduanmd05.Presenter.SanPhamView;
import fpoly.md05.appduanmd05.R;
import fpoly.md05.appduanmd05.View.HoanThanhActivity;
import fpoly.md05.appduanmd05.View.HomeActivity;
import fpoly.md05.appduanmd05.View.WebViewActivity;

public class ContentProDuctActivity extends AppCompatActivity implements GioHangView {

    private Intent intent;
    private SanPhamModels sanPhamModels;
    private TextView txttensp, txtgiatien, txtmota, txtnsx, txtbaohanh, txtsoluong;
    private Toolbar toolbar;
    private ImageView hinhanh;
    private Button btndathang, btnmuangay;
    private GioHangPreSenter gioHangPreSenter;

    private RecyclerView rcVBill;
    private GioHangAdapter sanPhamAdapter;
    private ArrayList<SanPhamModels> arrayList;

    private Button btnthanhtoan;
    private String s[] = {"Thanh toán khi nhận hàng", "Thanh toán MOMO"};
    private long tongtien = 0;
    private ProgressBar progressBar;
    private String hoten = "", diachi = "", sdt = "";
    private Spinner spinner;
    private int check = 0;

    private SanPhamModels productForCheckout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_pro_duct);
        InitWidget();
        Init();
        // Sau khi nhấn nút Đặt hàng và hoàn tất các hành động cần thiết

    }

    private void Init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Chi tiết sản phẩm");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        intent = getIntent();
        sanPhamModels = (SanPhamModels) intent.getSerializableExtra("SP");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        txtnsx.setText("Kích cỡ: "+sanPhamModels.getKichco());
        txtmota.setText("Mô tả: " + sanPhamModels.getMota());
//        txtbaohanh.setText("Màu sắc: "+sanPhamModels.getMausac());
        txttensp.setText("Tên sản phẩm: " + sanPhamModels.getTensp());
        txtgiatien.setText("Giá tiền: " + NumberFormat.getNumberInstance().format(sanPhamModels.getGiatien()));
        txtsoluong.setText("Số lượng: " + sanPhamModels.getSoluong());
        Picasso.get().load(sanPhamModels.getHinhanh()).into(hinhanh);
        txtmota.setMaxLines(3);
        gioHangPreSenter = new GioHangPreSenter(this);
///oke
        db.collection("SanPham").document(sanPhamModels.getId())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        String TAG;
                        if (e != null) {
                            return;
                        }

                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            // Cập nhật UI tại đây với thông tin mới nhất từ Firestore
                            SanPhamModels updatedProduct = documentSnapshot.toObject(SanPhamModels.class);
                            updateUI(updatedProduct);
                        } else {
                        }
                    }
                });

        btndathang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Giả định thêm một sản phẩm vào giỏ hàng mỗi lần nhấn nút Đặt hàng
                if (sanPhamModels.getSoluong() > 0) { // Kiểm tra nếu số lượng sản phẩm còn lại > 0
                    gioHangPreSenter.AddCart(sanPhamModels.getId(), 1);
                    Log.d("soluong", "onClick: " + sanPhamModels.getSoluong());
                    // Thêm 1 sản phẩm vào giỏ hàng

                    // Giảm số lượng sản phẩm đi 1 sau khi thêm vào giỏ
//                    long newQuantity = sanPhamModels.getSoluong() - 1;
//                    updateProductQuantity(sanPhamModels.getId(), newQuantity);

                    // Cập nhật lại UI với số lượng mới
//                    txtsoluong.setText("Số lượng: " + newQuantity);
//                    sanPhamModels.setSoluong(newQuantity); // Cập nhật số lượng trong model
                } else {
                    Toast.makeText(ContentProDuctActivity.this, "Sản phẩm đã hết hàng", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnmuangay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductToBillDetails(productForCheckout);
                DiaLogThanhToan();

            }
        });

    }



    private void DiaLogThanhToan() {
        Dialog dialog = new Dialog(ContentProDuctActivity.this);
        dialog.setContentView(R.layout.dialog_thanhtoan);

        // Các thành phần UI trong dialog...
        Button btnxacnhan = dialog.findViewById(R.id.btnxacnhan);

        // Tạo một đối tượng SanPhamModels mới hoặc sao chép từ đối tượng hiện tại
        SanPhamModels productForCheckout = new SanPhamModels(
                sanPhamModels.getIdsp(),
                sanPhamModels.getTensp(),
                sanPhamModels.getGiatien(),
                sanPhamModels.getHinhanh(),
                sanPhamModels.getLaoisp(),
                sanPhamModels.getMota(),
                1L, // Giả sử bạn đặt số lượng là 1
                sanPhamModels.getKichco(),
                sanPhamModels.getType(),
                sanPhamModels.getMausac()
        );

        btnxacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Xác nhận thông tin và thêm sản phẩm vào cơ sở dữ liệu
                addProductToBillDetails(productForCheckout);
                dialog.dismiss(); // Đóng dialog sau khi hoàn tất
            }
        });

        dialog.show();
    }

//    private void addProductToBillDetails(SanPhamModels product) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        Map<String, Object> billDetail = new HashMap<>();
//        billDetail.put("id_sanpham", product.getIdsp());
//        billDetail.put("soluong", product.getSoluong());
//        // Thêm các thông tin khác của sản phẩm vào Map nếu cần
//
//        db.collection("ChiTietHoaDon").add(billDetail)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d(TAG, "Chi tiết hóa đơn được thêm thành công");
//                        Toast.makeText(ContentProDuctActivity.this, "Thêm vào chi tiết hóa đơn thành công", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d(TAG, "Lỗi khi thêm chi tiết hóa đơn", e);
//                        Toast.makeText(ContentProDuctActivity.this, "Lỗi khi thêm vào chi tiết hóa đơn", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }



    private void CustomInit(Dialog dialog) {
        spinner = dialog.findViewById(R.id.spinerphguongthuc);
        EditText edithoten = dialog.findViewById(R.id.edithoten);
        EditText editdiachi = dialog.findViewById(R.id.editdiachi);
        EditText editsdt = dialog.findViewById(R.id.editsdt);
        Button btnxacnhan = dialog.findViewById(R.id.btnxacnhan);
        TextView txttongtien = dialog.findViewById(R.id.txttongtien);
        dialog.setCanceledOnTouchOutside(false);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, s);
        spinner.setAdapter(arrayAdapter);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        tongtien = 0;

        for (SanPhamModels sanPhamModels : arrayList) {
            tongtien += sanPhamModels.getGiatien() * sanPhamModels.getSoluong();
        }

        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        txttongtien.setText("Tổng tiền: " + NumberFormat.getNumberInstance().format(tongtien) + " Đ");
        btnxacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hoten = edithoten.getText().toString();
                diachi = editdiachi.getText().toString();
                sdt = editsdt.getText().toString();
                if (hoten.length() > 0) {
                    if (diachi.length() > 0) {
                        if (sdt.length() > 0) {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            Calendar calendar = Calendar.getInstance();
                            String ngaydat = simpleDateFormat.format(calendar.getTime());
                            String phuongthuc = spinner.getSelectedItem().toString();

                            switch (spinner.getSelectedItemPosition()) {
                                case 0:
                                    gioHangPreSenter.HandleAddHoaDon(ngaydat, diachi, hoten, sdt, phuongthuc, tongtien, arrayList);
                                    dialog.cancel();
                                    break;
                                case 1:
                                    dialog.cancel();
                                    break;

                            }
                            sendNotification("Thông báo", "Đơn hàng của bạn đã được đặt thành công!");
                            startActivity(new Intent(ContentProDuctActivity.this, HoanThanhActivity.class));
                            progressBar.setVisibility(View.VISIBLE);


                        } else {
                            Toast.makeText(ContentProDuctActivity.this, "Số điện thoại không để trống", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ContentProDuctActivity.this, "Địa chỉ không để trống", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ContentProDuctActivity.this, "Họ tên không để trống", Toast.LENGTH_SHORT).show();
                }
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Kiểm tra xem mục được chọn có phải là "Thanh toán MOMO" không
                if (position == 1) { // Giả sử "Thanh toán MOMO" là mục thứ hai trong Spinner
                    // Tạo Intent để mở WebViewActivity
                    Intent intent = new Intent(ContentProDuctActivity.this, WebViewActivity.class);

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

    private void addProductToBillDetails(SanPhamModels product){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Tạo một Map để lưu thông tin chi tiết hóa đơn
        Map<String, Object> billDetail = new HashMap<>();
        billDetail.put("id_hoadon", sanPhamModels.getId()); // Thay `yourGeneratedBillId` bằng ID hóa đơn bạn đã tạo
        billDetail.put("id_sanpham", sanPhamModels.getIdsp());
        billDetail.put("soluong", sanPhamModels.getSoluong());

        // Thêm vào Firestore
        db.collection("ChitietHoaDon").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("ALL").add(billDetail)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        // Thông báo thành công, chuyển hướng người dùng...
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        // Thông báo lỗi...
                    }
                });
    }

    public void sendNotification(String title, String message) {
        NotificationHelper.showNotification(this, title, message);
    }

    // Sau khi nhấn nút Đặt hàng và hoàn tất các hành động cần thiết

    private void updateUI(SanPhamModels updatedProduct) {
        // Cập nhật thông tin sản phẩm trên UI, ví dụ:
        txtsoluong.setText("Số lượng: " + updatedProduct.getSoluong());
        // Cập nhật thêm các trường thông tin khác nếu cần
    }

    private void updateProductQuantity(String productId, long newQuantity) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("SanPham").document(productId)
                .update("soluong", newQuantity)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ContentProDuctActivity.this, "Cập nhật số lượng sản phẩm thành công", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ContentProDuctActivity.this, "Cập nhật số lượng sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void InitWidget() {
        toolbar = findViewById(R.id.toolbar);
//        txtbaohanh = findViewById(R.id.txtbaohanh);
        txtgiatien = findViewById(R.id.txtgiatien);
        txtmota = findViewById(R.id.txtmota);
//        txtnsx=findViewById(R.id.txtthuonghieu);
//        txtbaohanh=findViewById(R.id.txtbaohanh);
        txttensp = findViewById(R.id.txttensp);
        hinhanh = findViewById(R.id.image_product);
        btndathang = findViewById(R.id.btndathang);
        btnmuangay = findViewById(R.id.btnmuangay);
        txtsoluong = findViewById(R.id.sl_kho);

    }

    @Override
    public void OnSucess() {
        Toast.makeText(this, "Thêm sản phẩm vào giỏ hàng thành công!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void OnFail() {
        Toast.makeText(this, "Thất Bại ! Lỗi hệ thống bảo trì", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getDataSanPham(String id, String idsp, String tensp, Long giatien, String hinhanh, String loaisp, Long soluong, String hansudung, Long type, String trongluong) {

    }

    public void onXemThemClick(View view) {
        TextView xemThemMoTa = findViewById(R.id.xemThemMoTa);
        int lineHeight = txtmota.getLineHeight();
        int twoLinesHeight = 2 * lineHeight;

        if (txtmota.getMaxLines() == Integer.MAX_VALUE) {
            txtmota.setMaxLines(3);
            xemThemMoTa.setText("Xem thêm");
        } else {
            txtmota.setMaxLines(Integer.MAX_VALUE);
            xemThemMoTa.setText("Thu gọn");
        }
    }
}