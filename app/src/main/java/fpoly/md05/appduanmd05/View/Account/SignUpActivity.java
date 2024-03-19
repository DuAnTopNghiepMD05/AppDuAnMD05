package fpoly.md05.appduanmd05.View.Account;

import static fpoly.md05.appduanmd05.Validate.Email.isValidEmail;
import static fpoly.md05.appduanmd05.Validate.borderError.resetErrorAndBorder;
import static fpoly.md05.appduanmd05.Validate.borderError.setRedBorderAndError;
import static fpoly.md05.appduanmd05.Validate.phone.isValidPhoneNumber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fpoly.md05.appduanmd05.R;

public class SignUpActivity extends AppCompatActivity {
    EditText email, pass, rePass;
    TextView signIn;
    Button signUp;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        anhXaView();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetErrorAndBorder(email);
                resetErrorAndBorder(pass);
                resetErrorAndBorder(rePass);

                List<EditText> editTextList = Arrays.asList(email, pass, rePass);
                List<String> errorMessages = new ArrayList<>();
                String password = pass.getText().toString();
                String confirmPassword = rePass.getText().toString();
                for (EditText editText : editTextList) {
                    if (editText.getText().toString().isEmpty()) {
                        errorMessages.add("Vui lòng nhập đầy đủ thông tin cho." + editText.getHint().toString());
                        setRedBorderAndError(editText, "Vui lòng nhập đầy đủ thông tin.");
                    }
                }

                if (!errorMessages.isEmpty()) {
                    for (String errorMessage : errorMessages) {
                        Log.e("Validation Error", errorMessage);
                    }
                    return;
                }

                if (!isValidEmail(email.getText().toString())) {
                    errorMessages.add("Email không hợp lệ.");
                    setRedBorderAndError(email, "Vui lòng nhập đúng định dạng email.");
                }
                if (password.length() < 6) {
                    errorMessages.add("Mật khẩu không hợp lệ.");
                    setRedBorderAndError(pass, "Vui lòng nhập trên 6 ký tự");
                }

                if (!password.equals(confirmPassword)) {
                    errorMessages.add("Mật khẩu và xác nhận mật khẩu không khớp.");
                    setRedBorderAndError(pass, "Mật khẩu không khớp.");
                    setRedBorderAndError(rePass, "Mật khẩu không khớp.");
                }

                if (!errorMessages.isEmpty()) {
                    return;
                }

                // Đăng ký tài khoản trên Firebase
                mAuth.createUserWithEmailAndPassword(email.getText().toString().trim(), pass.getText().toString().trim())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        user.sendEmailVerification()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(SignUpActivity.this, "Đã gửi email xác thực. Vui lòng kiểm tra email của bạn.", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(SignUpActivity.this, "Lỗi khi gửi email xác thực.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                        // Tiếp tục với quá trình tạo dữ liệu người dùng trên Firestore
                                        createUserProfile(user);
                                    }
                                } else {
                                    // Nếu đăng ký thất bại, hiển thị thông báo cho người dùng.
                                    Toast.makeText(SignUpActivity.this, "Đăng ký thất bại.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void createUserProfile(FirebaseUser user) {
        String userId = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> userData = new HashMap<>();
        userData.put("uid", userId);

        db.collection("thongtinUser").document(userId).set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Dữ liệu được thêm thành công, tiếp tục tạo dữ liệu trống bên trong profile
                        Map<String, Object> profileData = new HashMap<>();
                        profileData.put("hoten", "");
                        profileData.put("sdt", "");
                        profileData.put("avatar", "");
                        profileData.put("diachi", "");
                        profileData.put("email", "");
                        profileData.put("gioitinh", "");
                        profileData.put("ngaysinh", "");
                        profileData.put("trangthai", "");
                        profileData.put("uid", userId);

                        // Thêm dữ liệu vào bảng Profile trong Firestore
                        db.collection("thongtinUser").document(userId)
                                .collection("Profile").document()
                                .set(profileData)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Dữ liệu profile được thêm thành công, chuyển đến màn hình đăng nhập
                                        signIn();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Xử lý thất bại khi thêm dữ liệu vào Profile
                                        Toast.makeText(SignUpActivity.this, "Lỗi khi thêm dữ liệu vào Profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý thất bại khi thêm dữ liệu vào thongtinUser
                        Toast.makeText(SignUpActivity.this, "Lỗi khi thêm dữ liệu vào Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void anhXaView() {
        email = findViewById(R.id.signUp_Email);
        pass = findViewById(R.id.signUp_Pass);
        rePass = findViewById(R.id.signUp_RePass);
        signUp = findViewById(R.id.signUp_Register);
        signIn = findViewById(R.id.signUp_Login);
    }

    void signIn() {
        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(intent);
        finishAffinity();
    }
}
