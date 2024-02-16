package fpoly.md05.appduanmd05.Model;

import com.google.firebase.auth.FirebaseAuth;

import fpoly.md05.appduanmd05.Presenter.IUSER;

public class UserModel {

    private FirebaseAuth firebaseAuth;
    private String email;

    private String pass;

    private IUSER callback;

    public UserModel() {

    }

    public UserModel(IUSER callback) {
        this.callback = callback;
        firebaseAuth = FirebaseAuth.getInstance();

    }

}
