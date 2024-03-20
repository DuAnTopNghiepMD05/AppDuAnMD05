package fpoly.md05.appduanmd05.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import fpoly.md05.appduanmd05.R;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        WebView myWebView = findViewById(R.id.webview);
        String url = getIntent().getStringExtra("URL");
        myWebView.loadUrl(url);
    }
}