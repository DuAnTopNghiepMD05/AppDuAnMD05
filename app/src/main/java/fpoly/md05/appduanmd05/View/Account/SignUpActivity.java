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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fpoly.md05.appduanmd05.R;

public class SignUpActivity extends AppCompatActivity {
    EditText email,pass,rePass;
    TextView signIn;
    Button signUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
     anhXaView();
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetErrorAndBorder(email);
                resetErrorAndBorder(pass);
                resetErrorAndBorder(rePass);

                //validate
                List<EditText> editTextList = Arrays.asList(email, pass, rePass);
                List<String> errorMessages = new ArrayList<>();
                String password = pass.getText().toString();
                String confirmPassword = rePass.getText().toString();
                for (EditText editText : editTextList) {
                    if (editText.getText().toString().isEmpty()) {
                        errorMessages.add("Vui lòng nhập đầy đủ thông tin cho " + editText.getHint().toString());
                        setRedBorderAndError(editText, "Vui lòng nhập đầy đủ thông tin");
                    }

                }

                if (!errorMessages.isEmpty()) {
                    for (String errorMessage : errorMessages) {
                        Log.e("Validation Error", errorMessage);
                    }
                }

                //validate định dạng email
                if (!isValidEmail(email.getText().toString())) {
                    errorMessages.add("Email không hợp lệ");
                    setRedBorderAndError(email, "Vui lòng nhập đúng định dạng email");
                }
                if(pass.getText().length()<6){
                    errorMessages.add("Mật khẩu không hợp lệ");
                    setRedBorderAndError(pass, "Vui lòng nhập trên 6 ký tự");
                }
                //validate pass


                if (!password.equals(confirmPassword)) {
                    errorMessages.add("Mật khẩu và xác nhận mật khẩu không khớp");
                    setRedBorderAndError(pass, "Mật khẩu không khớp");
                    setRedBorderAndError(rePass, "Mật khẩu không khớp");
                }
                if(isValidEmail(email.getText().toString())&&password.equals(confirmPassword)
                &&!pass.getText().toString().isEmpty()&&!rePass.getText().toString().isEmpty()){
                    onclickSignUp();
                }
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();

            }
        });
    }
    private void onclickSignUp(){
        String auth_email = email.getText().toString().trim();
        String auth_pass = pass.getText().toString().trim();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(auth_email, auth_pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user != null) {
                                String userId = user.getUid();

                                // Kết nối với Firestore
                                FirebaseFirestore db = FirebaseFirestore.getInstance();

                                // Tạo đối tượng dữ liệu để thêm vào Firestore
                                Map<String, Object> userData = new HashMap<>();
                                userData.put("uid", userId);

                                // Thêm dữ liệu vào bảng thongtinUser trong Firestore
                                db.collection("thongtinUser").document(userId).set(userData)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                // Dữ liệu được thêm thành công, tạo dữ liệu trống bên trong profile
                                                Map<String, Object> profileData = new HashMap<>();

                                                profileData.put("hoten", ""); // Họ tên rỗng
                                                profileData.put("sdt", ""); // Số điện thoại rỗng
                                                profileData.put("avatar", ""); // Giới tính rỗng
                                                profileData.put("diachi", ""); // Địa chỉ rỗng
                                                profileData.put("email", ""); // Giới tính rỗng
                                                profileData.put("gioitinh", ""); // Giới tính rỗng
                                                profileData.put("ngaysinh", ""); // Ngày sinh rỗng
                                                profileData.put("trangthai", ""); // uid của user
                                                profileData.put("uid", userId); // uid của user

                                                // Thêm dữ liệu vào bảng Profile trong Firestore
                                                db.collection("thongtinUser").document(userId)
                                                        .collection("Profile").document()
                                                        .set(profileData)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                // Dữ liệu profile được thêm thành công, xử lý tiếp
                                                                signIn();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                // Xử lý thất bại khi thêm dữ liệu vào Profile
                                                                Toast.makeText(SignUpActivity.this, "Lỗi khi thêm dữ liệu vào Profile", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Xử lý thất bại khi thêm dữ liệu vào thongtinUser
                                                Toast.makeText(SignUpActivity.this, "Lỗi khi thêm dữ liệu vào Firestore", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                            // Nếu đăng ký thất bại, hiển thị thông báo cho người dùng.
                            Toast.makeText(SignUpActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    void anhXaView(){
        //create sign up activity
        email=findViewById(R.id.signUp_Email);
        pass=findViewById(R.id.signUp_Pass);
        rePass=findViewById(R.id.signUp_RePass);
        signUp=findViewById(R.id.signUp_Register);
        signIn=findViewById(R.id.signUp_Login);
    }
    void signIn(){
        Intent intent=new Intent(SignUpActivity.this,SignInActivity.class);
        startActivity(intent);
        finishAffinity();
    }

}