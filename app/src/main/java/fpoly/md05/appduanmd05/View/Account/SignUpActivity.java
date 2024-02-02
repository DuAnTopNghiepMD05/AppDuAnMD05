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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fpoly.md05.appduanmd05.R;

public class SignUpActivity extends AppCompatActivity {
    EditText name,sdt,email,pass,rePass;
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
                resetErrorAndBorder(name);
                resetErrorAndBorder(sdt);
                resetErrorAndBorder(email);
                resetErrorAndBorder(pass);
                resetErrorAndBorder(rePass);
                //123
                //validate
                List<EditText> editTextList = Arrays.asList(name, sdt, email, pass, rePass);
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

                //validate kiểm tra định dạng sdt
                if (!isValidPhoneNumber(sdt.getText().toString())) {
                    errorMessages.add("Số điện thoại không hợp lệ");
                    setRedBorderAndError(sdt, "Vui lòng nhập đúng định dạng sdt");
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
                if(!name.getText().toString().isEmpty()&&isValidPhoneNumber(sdt.getText().toString())
                        &&isValidEmail(email.getText().toString())&&password.equals(confirmPassword)
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
        String auth_email=email.getText().toString().trim();
        String auth_pass=pass.getText().toString().trim();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(auth_email,auth_pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            signIn();
                        } else {
                            // If sign in fails, display a message to the user.

                        }
                    }
                });
    }


    void anhXaView(){
        //create sign up activity
        name=findViewById(R.id.signUp_username);
        sdt=findViewById(R.id.signUp_Phone);
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