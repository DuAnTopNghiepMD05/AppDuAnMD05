package fpoly.md05.appduanmd05;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import fpoly.md05.appduanmd05.View.Account.SignInActivity;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    private MediaPlayer mediaPlayer;
    private boolean isMediaPlayerStarted = false; // Biến cờ để kiểm tra xem MediaPlayer đã bắt đầu chưa

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Áp dụng animation lắc cho ImageView
        ImageView imageView = findViewById(R.id.logoapp); // Đảm bảo id này phù hợp với ImageView trong layout của bạn
        Animation swingAnimation = AnimationUtils.loadAnimation(this, R.anim.swing);
        imageView.startAnimation(swingAnimation);

        // Phát file nhạc
        mediaPlayer = MediaPlayer.create(SplashScreen.this, R.raw.splash_music);
        mediaPlayer.start();
        isMediaPlayerStarted = true; // Cập nhật trạng thái của MediaPlayer

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashScreen.this, SignInActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Kiểm tra và dừng MediaPlayer nếu nó đang phát
        if (mediaPlayer != null && isMediaPlayerStarted) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
