package kiranamegatara.com.kipas.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import kiranamegatara.com.kipas.R;

public class ForgetPassword extends AppCompatActivity {
    WebView webForgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        webForgot = (WebView)findViewById(R.id.webViewForgot);

        String url ="https://www.kmshipmentstatus.com/ws_sir/forget/forget.php";
//        String url ="http://10.0.0.105/dev/forget/forget.php";
        webForgot.getSettings().setJavaScriptEnabled(true);
        webForgot.loadUrl(url);
        webForgot.setWebViewClient(new MyWebLaunch());
    }

    private class MyWebLaunch extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }
    }
}
