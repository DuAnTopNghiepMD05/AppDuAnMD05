package fpoly.md05.appduanmd05;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import fpoly.md05.appduanmd05.View.Account.SignInActivity;
import fpoly.md05.appduanmd05.View.HomeActivity;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        ImageView imageView = findViewById(R.id.logoapp);
        Animation swingAnimation = AnimationUtils.loadAnimation(this, R.anim.swing);
        imageView.startAnimation(swingAnimation);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    if (currentUser.isEmailVerified()) {
                        startActivity(new Intent(SplashScreen.this, HomeActivity.class));
                    } else {
                        // Người dùng đã đăng nhập nhưng email chưa được xác thực
                        startActivity(new Intent(SplashScreen.this, SignInActivity.class));
                    }
                } else {
                    // Người dùng chưa đăng nhập
                    startActivity(new Intent(SplashScreen.this, SignInActivity.class));
                }
                finish();
            }
        }, SPLASH_TIME_OUT);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
