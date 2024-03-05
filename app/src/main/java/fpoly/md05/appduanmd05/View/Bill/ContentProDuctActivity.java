package fpoly.md05.appduanmd05.View.Bill;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;

import fpoly.md05.appduanmd05.Model.SanPhamModels;
import fpoly.md05.appduanmd05.R;

public class ContentProDuctActivity extends AppCompatActivity {
//LƯU Ý: ĐÃ ÁNH XẠ IMAGEVIEW
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set tỉ lệ chiều rộng chiều cao theo tỉ lệ màn hình
        setContentView(R.layout.activity_content_pro_duct);
        Intent intent= getIntent();
        if(intent != null && intent.hasExtra("SP")) {
            SanPhamModels sanPhamModels = (SanPhamModels) intent.getSerializableExtra("SP");
            if(sanPhamModels != null) {
                // Hiển thị chi tiết sản phẩm trên giao diện
                ImageView imgHinhanh = findViewById(R.id.chitiet_image);
                TextView txtTensp = findViewById(R.id.txtTensp);
                TextView txtGia = findViewById(R.id.chiTiet_giaBan);
                TextView txtSoLuong = findViewById(R.id.chiTiet_soLuong);
                TextView txtMoTa = findViewById(R.id.chiTiet_moTa);

                //
                Picasso.get().load(sanPhamModels.getHinhanh()).into(imgHinhanh);
                txtTensp.setText(sanPhamModels.getTensp());
                txtGia.setText(NumberFormat.getInstance().format(sanPhamModels.getGiatien()) + " Đ");
                txtSoLuong.setText(NumberFormat.getInstance().format(sanPhamModels.getSoluong()) );
                txtMoTa.setText(sanPhamModels.getMota());


            }
        }
        //thêm vào giỏ hàng
        Button btnGioHang=findViewById(R.id.btnGioHang);
        btnGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //mua ngay
        Button btnMuaNgay=findViewById(R.id.btnMuaNgay);
        btnMuaNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1= new Intent(ContentProDuctActivity.this,CartActivity.class);
                startActivity(intent1);
            }
        });

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenHeight = display.getHeight();
        int screenWidth = display.getWidth();

        ImageView imageView = findViewById(R.id.chitiet_image);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        params.height = screenHeight / 3;
        params.width = (int) (screenWidth * 0.8);
        imageView.setLayoutParams(params);
    }
}