package fpoly.md05.appduanmd05.View.Bill;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;

import fpoly.md05.appduanmd05.Adapter.SanPhamAdapter;
import fpoly.md05.appduanmd05.Model.SanPhamModels;
import fpoly.md05.appduanmd05.Presenter.SanPhamPreSenter;
import fpoly.md05.appduanmd05.Presenter.SanPhamView;
import fpoly.md05.appduanmd05.R;

public class ContentProDuctActivity extends AppCompatActivity implements SanPhamView{

    private LinearLayout linear2;
    private ArrayList<String> arrayList;

    private ViewPager viewPager;

    private FirebaseFirestore db;
    private RecyclerView rcv;

    private SanPhamPreSenter sanPhamPreSenter;

    private ArrayList<SanPhamModels> arr_sp_nb;

    private SanPhamAdapter  sanPhamNBAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_pro_duct);
        rcv=findViewById(R.id.rcvGoiY);
        Intent intent = getIntent();
        arr_sp_nb = new ArrayList<>();
        sanPhamPreSenter = new SanPhamPreSenter((SanPhamView) this);
        sanPhamPreSenter.HandlegetDataSanPhamNB();
        db = FirebaseFirestore.getInstance();
        if (intent != null && intent.hasExtra("SP")) {
            SanPhamModels sanPhamModels = (SanPhamModels) intent.getSerializableExtra("SP");
            if (sanPhamModels != null) {
                // Hiển thị chi tiết sản phẩm trên giao diện
                ImageView imgHinhanh = findViewById(R.id.chitiet_image);
                TextView txtTensp = findViewById(R.id.txtTensp);
                TextView txtGia = findViewById(R.id.chiTiet_giaBan);
                TextView txtSoLuong = findViewById(R.id.chiTiet_soLuong);
                TextView txtMoTa = findViewById(R.id.chiTiet_moTa);
                TextView txtGia2=findViewById(R.id.chiTiet_giaBan2);

                Picasso.get().load(sanPhamModels.getHinhanh()).into(imgHinhanh);
                txtTensp.setText(sanPhamModels.getTensp());
                txtGia.setText("Giá bán: "+NumberFormat.getInstance().format(sanPhamModels.getGiatien()) + " Đ");
                txtSoLuong.setText("Số lượng: "+NumberFormat.getInstance().format(sanPhamModels.getSoluong()));
                txtGia2.setText(NumberFormat.getInstance().format(sanPhamModels.getGiatien()) + " Đ");
                SpannableString spannableGia = new SpannableString("Giá bán: " + NumberFormat.getInstance().format(sanPhamModels.getGiatien()) + " Đ");
                spannableGia.setSpan(new ForegroundColorSpan(getResources().getColor(android.R.color.black)), 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableGia.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorRed)), 8, spannableGia.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                txtGia.setText(spannableGia);

                SpannableString spannableSoLuong = new SpannableString("Số lượng: " + NumberFormat.getInstance().format(sanPhamModels.getSoluong()));
                spannableSoLuong.setSpan(new ForegroundColorSpan(getResources().getColor(android.R.color.black)), 0, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableSoLuong.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorRed)), 10, spannableSoLuong.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                txtSoLuong.setText(spannableSoLuong);

                txtMoTa.setText(sanPhamModels.getMota());
                txtMoTa.setText(sanPhamModels.getMota());
            }
        }

        // Thêm vào giỏ hàng
        AppCompatImageButton btnGioHang = findViewById(R.id.btnGioHang);
        btnGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thực hiện các thao tác khi nhấn nút Giỏ hàng
            }
        });

        // Mua ngay
        AppCompatButton btnMuaNgay = findViewById(R.id.btnMuaNgay);
        btnMuaNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ContentProDuctActivity.this, CartActivity.class);
                startActivity(intent1);
            }
        });

        linear2 = findViewById(R.id.linear2);

        // Lắng nghe sự kiện cuộn của NestedScrollView
        NestedScrollView nestedScrollView = findViewById(R.id.nestedScrollView);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // vuốt xuống
                if (scrollY > oldScrollY) {
                    hideButtons();
                }
                //  vuốt lên
                else {
                    showButtons();
                }
            }
        });

        WindowManager windowManager = getWindowManager();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        int screenWidth = windowManager.getDefaultDisplay().getWidth();

        ImageView imageView = findViewById(R.id.chitiet_image);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        params.height = screenHeight / 3;
        params.width = (int) (screenWidth * 0.8);
        imageView.setLayoutParams(params);
    }

    private void hideButtons() {
        linear2.setVisibility(View.GONE);
    }

    private void showButtons() {
        linear2.setVisibility(View.VISIBLE);
    }

    public void onXemThemClick(View view) {
        TextView chiTietMoTa = findViewById(R.id.chiTiet_moTa);
        TextView xemThemMoTa = findViewById(R.id.xemThemMoTa);
        int lineHeight = chiTietMoTa.getLineHeight();
        int twoLinesHeight = 2 * lineHeight;

        if (chiTietMoTa.getMaxLines() == Integer.MAX_VALUE) {
            chiTietMoTa.setMaxLines(3);
            xemThemMoTa.setText("Xem thêm");
        } else {
            chiTietMoTa.setMaxLines(Integer.MAX_VALUE);
            xemThemMoTa.setText("Thu gọn");
        }
    }


    @Override
    public void getDataSanPham(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong, String kichco, Long type, String mausac) {

    }

    public void getDataSanPhamNB(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong, String kichco, Long type, String mausac) {
        arr_sp_nb.add(new SanPhamModels(id, tensp, giatien, hinhanh, loaisp, mota, soluong, kichco, type, mausac));
        sanPhamNBAdapter = new SanPhamAdapter(ContentProDuctActivity.this, arr_sp_nb, 2);
        rcv.setLayoutManager(new LinearLayoutManager(ContentProDuctActivity.this, RecyclerView.HORIZONTAL, false));
        rcv.setAdapter(sanPhamNBAdapter);
    }

    @Override
    public void getDataSanPhamGiamGia(String id, String tensp, Long giatien, String hinhanh, String loaisp, String mota, Long soluong, String kichco, Long type, String mausac) {

    }


    @Override
    public void OnEmptyList() {

    }

}