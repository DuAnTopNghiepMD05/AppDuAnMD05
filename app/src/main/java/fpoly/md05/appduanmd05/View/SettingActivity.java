package fpoly.md05.appduanmd05.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import fpoly.md05.appduanmd05.R;

public class SettingActivity extends AppCompatActivity {

    LinearLayout changePasswordLayout, notificationSettingsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting);

        // Ánh xạ
        changePasswordLayout = findViewById(R.id.setting_changePass);
        notificationSettingsLayout = findViewById(R.id.setting_changeInfo);

        // Xử lý sự kiện nhấn cho đổi mật khẩu
        changePasswordLayout.setOnClickListener(v -> {
            Intent intent = new Intent(SettingActivity.this, ChangePassActivity.class);
            startActivity(intent);
        });

        // Xử lý sự kiện nhấn cho cài đặt thông báo
        notificationSettingsLayout.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng này chưa được hỗ trợ", Toast.LENGTH_SHORT).show();
        });
    }
}
