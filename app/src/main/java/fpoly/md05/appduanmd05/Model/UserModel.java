package fpoly.md05.appduanmd05.Model;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import fpoly.md05.appduanmd05.Presenter.IUSER;

public class UserModel {

    private FirebaseAuth firebaseAuth;
    private String email;

    private String valid_email = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private String pass;

    private IUSER callback;

    public UserModel() {

    }

    public UserModel(IUSER callback) {
        this.callback = callback;
        firebaseAuth = FirebaseAuth.getInstance();

    }

}
