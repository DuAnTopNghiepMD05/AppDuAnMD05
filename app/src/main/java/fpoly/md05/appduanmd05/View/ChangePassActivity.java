package fpoly.md05.appduanmd05.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import fpoly.md05.appduanmd05.R;

public class ChangePassActivity extends AppCompatActivity {

    private EditText oldPasswordEditText, newPasswordEditText, confirmPasswordEditText;
    private Button changePasswordButton;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_change_password);

        auth = FirebaseAuth.getInstance();

        oldPasswordEditText = findViewById(R.id.changepass_txtOldPass);
        newPasswordEditText = findViewById(R.id.changepass_txtNewPass);
        confirmPasswordEditText = findViewById(R.id.changepass_txtRePass);
        changePasswordButton = findViewById(R.id.diaChi_btnThem);

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    private void changePassword() {
        String newPassword = newPasswordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (!newPassword.equals(confirmPassword)) {
            // Mật khẩu mới và xác nhận mật khẩu không khớp
            Toast.makeText(this, "Mật khẩu mới và xác nhận mật khẩu không khớp.", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = auth.getCurrentUser();

        if(user != null) {
            user.updatePassword(newPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ChangePassActivity.this, "Mật khẩu đã được cập nhật thành công.", Toast.LENGTH_SHORT).show();
                            // Logic sau khi cập nhật thành công
                        } else {
                            Toast.makeText(ChangePassActivity.this, "Có lỗi xảy ra khi cập nhật mật khẩu.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(e -> {
                        // Log chi tiết lỗi hoặc hiển thị thông báo chi tiết hơn cho người dùng
                        Log.d("ChangePasswordError", "Failed to update password: " + e.getMessage());
                        Toast.makeText(ChangePassActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });

        }
    }
}