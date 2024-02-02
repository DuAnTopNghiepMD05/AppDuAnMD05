package fpoly.md05.appduanmd05.View.Account;

import static fpoly.md05.appduanmd05.Validate.Email.isValidEmail;
import static fpoly.md05.appduanmd05.Validate.borderError.resetErrorAndBorder;
import static fpoly.md05.appduanmd05.Validate.borderError.setRedBorderAndError;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import java.util.List;

import fpoly.md05.appduanmd05.MainActivity;
import fpoly.md05.appduanmd05.R;

public class SignInActivity extends AppCompatActivity {
    EditText sign_in_email,sign_in_password;
    Button button_SignIn;
    FirebaseAuth mAuth;
    TextView screenSignUp;
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
                //validate
                List<String> errorMessages = new ArrayList<>();
                resetErrorAndBorder(sign_in_email);
                resetErrorAndBorder(sign_in_password);

                if (userEmail.isEmpty()) {
                    errorMessages.add("Email không được để trống");
                    setRedBorderAndError(sign_in_password, "Vui lòng nhập Email !");
                }
                if (!isValidEmail(userEmail)) {
                    errorMessages.add("Email không hợp lệ");
                    setRedBorderAndError(sign_in_email, "Vui lòng nhập đúng định dạng email !");
                }

                if (userPass.isEmpty()) {
                    errorMessages.add("Mật khẩu không được để trống");
                    setRedBorderAndError(sign_in_password, "Vui lòng nhập mật khẩu !");
                }
                signInUser(userEmail, userPass);
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
                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
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