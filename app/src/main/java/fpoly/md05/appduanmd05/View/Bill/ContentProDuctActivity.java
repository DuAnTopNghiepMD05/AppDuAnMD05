package fpoly.md05.appduanmd05.View.Bill;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import fpoly.md05.appduanmd05.R;

public class ContentProDuctActivity extends AppCompatActivity {
//LƯU Ý: ĐÃ ÁNH XẠ IMAGEVIEW
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set tỉ lệ chiều rộng chiều cao theo tỉ lệ màn hình
        setContentView(R.layout.activity_content_pro_duct);
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