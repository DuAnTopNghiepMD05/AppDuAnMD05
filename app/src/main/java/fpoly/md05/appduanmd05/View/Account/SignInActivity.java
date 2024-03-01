package fpoly.md05.appduanmd05.View.Account;

import static fpoly.md05.appduanmd05.Validate.Email.isValidEmail;
import static fpoly.md05.appduanmd05.Validate.borderError.resetErrorAndBorder;
import static fpoly.md05.appduanmd05.Validate.borderError.setRedBorderAndError;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fpoly.md05.appduanmd05.MainActivity;
import fpoly.md05.appduanmd05.R;
import fpoly.md05.appduanmd05.View.HomeActivity;

public class SignInActivity extends AppCompatActivity {
    EditText sign_in_email,sign_in_password;
    Button button_SignIn;
    FirebaseAuth mAuth;
    TextView screenSignUp,forgot_pass;
    CheckBox checkboxRemember;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "login_pref";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_REMEMBER_ME = "remember_me";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        //create sign in activity
        mAuth= FirebaseAuth.getInstance();
        sign_in_email=findViewById(R.id.sign_in_email);
        sign_in_password=findViewById(R.id.sign_in_password);
        button_SignIn=findViewById(R.id.button_SignIn);
        screenSignUp=findViewById(R.id.tv_SignUp);
        forgot_pass=findViewById(R.id.forgot_Password);
        checkboxRemember=findViewById(R.id.checkBox_Remember);
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // Load saved login information
        if (sharedPreferences.getBoolean(KEY_REMEMBER_ME, false)) {
            String savedUsername = sharedPreferences.getString(KEY_EMAIL, "");
            String savedPassword = sharedPreferences.getString(KEY_PASSWORD, "");

            sign_in_email.setText(savedUsername);
            sign_in_password.setText(savedPassword);
            checkboxRemember.setChecked(true);
        }
        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(SignInActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot_password,null);
                EditText email_box=dialogView.findViewById(R.id.email_box);

                builder.setView(dialogView);
                AlertDialog dialog = builder.create();

                dialogView.findViewById(R.id.btnForgot).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String userEmail = email_box.getText().toString().trim();
                        Log.d("concac", "onClick: "+userEmail);
                        if(TextUtils.isEmpty(userEmail) && !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                            Toast.makeText(SignInActivity.this, "Nhập email đã đăng kí", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(SignInActivity.this, "Kiểm tra email", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    SignInActivity.this.recreate();
                                }else {
                                    Toast.makeText(SignInActivity.this, "Chưa gửi, lỗi", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                dialogView.findViewById(R.id.btnCancelForgot).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                if (dialog.getWindow() != null){
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                dialog.show();
            }
        });
        screenSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
        button_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = sign_in_email.getText().toString().trim();
                String userPass = sign_in_password.getText().toString().trim();
                resetErrorAndBorder(sign_in_email);
                resetErrorAndBorder(sign_in_password);
                List<EditText> editTextList = Arrays.asList(sign_in_email,sign_in_password);
                List<String> errorMessages = new ArrayList<>();
//                String password = pass.getText().toString();
//                String confirmPassword = rePass.getText().toString();
                for (EditText editText : editTextList) {
                    if (editText.getText().toString().isEmpty()) {
                        errorMessages.add("Vui lòng nhập đầy đủ thông tin cho " + editText.getHint().toString());
                        setRedBorderAndError(editText, "Vui lòng nhập đầy đủ thông tin");
                    }

                }
                if (!isValidEmail(sign_in_email.getText().toString())) {
                    errorMessages.add("Email không hợp lệ");
                    setRedBorderAndError(sign_in_email, "Vui lòng nhập đúng định dạng email");
                }
                if(sign_in_password.getText().length()<6){
                    errorMessages.add("Mật khẩu không hợp lệ");
                    setRedBorderAndError(sign_in_password, "Vui lòng nhập trên 6 ký tự");
                }
                if(isValidEmail(sign_in_email.getText().toString())&&!sign_in_password.getText().toString().isEmpty()){
                    signInUser(userEmail, userPass);
                }
                if (checkboxRemember.isChecked()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(KEY_EMAIL, userEmail);
                    editor.putString(KEY_PASSWORD, userPass);
                    editor.putBoolean(KEY_REMEMBER_ME, true);
                    editor.apply();
                } else {
                    // Clear saved login information if "Remember Me" is not checked
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove(KEY_EMAIL);
                    editor.remove(KEY_PASSWORD);
                    editor.remove(KEY_REMEMBER_ME);
                    editor.apply();
                }

            }
        });

    }

    private void signInUser(String userEmail, String userPass) {
        mAuth.signInWithEmailAndPassword(userEmail,userPass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(SignInActivity.this, "Đăng nhập thành công.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignInActivity.this, "Đăng nhập không thành công.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}