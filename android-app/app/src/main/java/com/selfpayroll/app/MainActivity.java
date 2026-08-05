package com.selfpayroll.app;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {

    private UpdateManager updateManager;


    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    2026
            );
        }

updateManager = new UpdateManager(this);
        updateManager.checkAutomatically();

        super.onCreate(savedInstanceState);

        webView = new WebView(this);
        setContentView(webView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("file:///android_asset/index.html");
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    public void checkForUpdate() {
        if (updateManager == null) {
            updateManager = new UpdateManager(this);
        }
        updateManager.checkManually();
    }

}
