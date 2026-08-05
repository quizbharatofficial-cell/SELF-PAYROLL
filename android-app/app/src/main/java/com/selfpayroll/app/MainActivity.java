package com.selfpayroll.app;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.JavascriptInterface;

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
        webView.addJavascriptInterface(new AndroidBridge(), "Android");
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



    private class AndroidBridge {

        @JavascriptInterface
        public void checkForUpdate() {
            runOnUiThread(() -> MainActivity.this.checkForUpdate());
        }

        @JavascriptInterface
        public String getLatestVersion() {
            return updateManager == null ? "" : updateManager.getLatestVersion();
        }

        @JavascriptInterface
public boolean isQrScannerAvailable() {
            return true;
        }

        @JavascriptInterface
        public String scanQrCode() {
            // Sprint 2 placeholder.
            // Sprint 3 will launch a real QR scanner.
            return "";
        }

        public boolean hasCameraPermission() {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                return checkSelfPermission(android.Manifest.permission.CAMERA)
                        == android.content.pm.PackageManager.PERMISSION_GRANTED;
            }
            return true;
        }

        @JavascriptInterface
        public void requestCameraPermission() {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                if (checkSelfPermission(android.Manifest.permission.CAMERA)
                        != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(
                            new String[]{
                                    android.Manifest.permission.CAMERA
                            },
                            2028
                    );
                }
            }
        }

        public boolean hasLocationPermission() {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                return checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == android.content.pm.PackageManager.PERMISSION_GRANTED;
            }
            return true;
        }

        @JavascriptInterface
        public void requestLocationPermission() {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(
                            new String[]{
                                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                            },
                            2027
                    );
                }
            }
        }

        public String getLatestWhatsNew() {
            return updateManager == null ? "" : updateManager.getLatestWhatsNew();
        }

    }

}