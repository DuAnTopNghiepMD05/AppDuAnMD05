package fpoly.md05.appduanmd05.View.Bill;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;

import fpoly.md05.appduanmd05.Adapter.SanPhamAdapter;
import fpoly.md05.appduanmd05.Model.SanPhamModels;
import fpoly.md05.appduanmd05.Presenter.GioHangPreSenter;
import fpoly.md05.appduanmd05.Presenter.GioHangView;
import fpoly.md05.appduanmd05.Presenter.SanPhamPreSenter;
import fpoly.md05.appduanmd05.Presenter.SanPhamView;
import fpoly.md05.appduanmd05.R;
import fpoly.md05.appduanmd05.View.HomeActivity;

public class ContentProDuctActivity extends AppCompatActivity implements GioHangView {
//123
    private Intent intent;
    private SanPhamModels sanPhamModels;
    private TextView txttensp, txtgiatien, txtmota, txtnsx, txtbaohanh, txtsoluong;
    private Toolbar toolbar;
    private ImageView hinhanh;
    private Button btndathang;
    private GioHangPreSenter gioHangPreSenter;


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
        intent=getIntent();
        sanPhamModels = (SanPhamModels) intent.getSerializableExtra("SP");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        txtnsx.setText("Kích cỡ: "+sanPhamModels.getKichco());
        txtmota.setText("Mô tả: "+sanPhamModels.getMota());
//        txtbaohanh.setText("Màu sắc: "+sanPhamModels.getMausac());
        txttensp.setText("Tên sản phẩm: "+sanPhamModels.getTensp());
        txtgiatien.setText("Giá tiền: "+NumberFormat.getNumberInstance().format(sanPhamModels.getGiatien()));
        txtsoluong.setText("Số lượng: "+sanPhamModels.getSoluong());
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
                    long newQuantity = sanPhamModels.getSoluong() - 1;
                    updateProductQuantity(sanPhamModels.getId(), newQuantity);

                    // Cập nhật lại UI với số lượng mới
                    txtsoluong.setText("Số lượng: " + newQuantity);
                    sanPhamModels.setSoluong(newQuantity); // Cập nhật số lượng trong model
                } else {
                    Toast.makeText(ContentProDuctActivity.this, "Sản phẩm đã hết hàng", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
        txtmota=findViewById(R.id.txtmota);
//        txtnsx=findViewById(R.id.txtthuonghieu);
//        txtbaohanh=findViewById(R.id.txtbaohanh);
        txttensp=findViewById(R.id.txttensp);
        hinhanh=findViewById(R.id.image_product);
        btndathang=findViewById(R.id.btndathang);
        txtsoluong= findViewById(R.id.sl_kho);

    }

    @Override
    public void OnSucess() {
        Toast.makeText(this, "Thêm sản phẩm vào giỏ hàng thành công!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent( ContentProDuctActivity.this, HomeActivity.class));
        finish();
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