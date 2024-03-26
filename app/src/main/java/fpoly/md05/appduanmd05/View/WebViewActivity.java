package fpoly.md05.appduanmd05.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import fpoly.md05.appduanmd05.R;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        WebView myWebView = findViewById(R.id.webview);
        String url = getIntent().getStringExtra("URL");
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new MyWebViewClient());
        myWebView.addJavascriptInterface(this, "Android");
        myWebView.loadUrl(url);

    }

    private class MyWebViewClient extends WebViewClient {
        private static final String TAG = "webviewclient";

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d(TAG, "onPageFinished: ");
            // Inject JavaScript to extract payment status from JSON response
            view.evaluateJavascript("javascript:(function() { " +
                    "var jsonText = document.body.textContent;" +
                    "var jsonData = JSON.parse(jsonText);" +
                    "if (jsonData.code === '00') {" +
                    "   window.Android.showToast('Payment successful');" +
                    "} else {" +
                    "   window.Android.showToast('Payment failed');" +
                    "}})();", null);
        }
    }

    // JavaScript interface method to show toast message
    @android.webkit.JavascriptInterface
    public void showToast(String message) {
        if (message.matches("payment successful")) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            finish();
            //TODO: tắt activity và quay lại activity cũ cập nhật trạng thái đơn hàng
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            finish();
            //TODO: tắt activity
        }
        Log.d("TAG", "showToast: " + message);
    }
}