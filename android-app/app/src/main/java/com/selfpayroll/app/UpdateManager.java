package com.selfpayroll.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateManager {

    private static final String API =
            "https://api.github.com/repos/quizbharatofficial-cell/SELF-PAYROLL/releases/latest";

    private static final String CHANNEL_ID = "self_payroll_updates";

    private final Activity activity;

    private volatile String latestVersion = "";
    private volatile String latestApkUrl = "";
    private volatile String latestWhatsNew = "";


    public UpdateManager(Activity activity) {
        this.activity = activity;
        createNotificationChannel();
    }

    public void checkAutomatically() {
        check(false);
    }

    public void checkManually() {
        check(true);
    }

    private void check(boolean manual) {
        new Thread(() -> {
            try {
                HttpURLConnection connection =
                        (HttpURLConnection) new URL(API).openConnection();

                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.setRequestProperty(
                        "Accept",
                        "application/vnd.github+json"
                );
                connection.setRequestProperty(
                        "User-Agent",
                        "SELF-PAYROLL-Android"
                );

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream())
                );

                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                reader.close();
                connection.disconnect();

                JSONObject release =
                        new JSONObject(result.toString());

                String tag = release.optString("tag_name", "");
                String version = tag.replaceFirst("^[vV]", "");
                String whatsNew = release.optString(
                        "body",
                        "Performance improvements and bug fixes."
                );

                String apkUrl = "";

                JSONArray assets = release.optJSONArray("assets");

                if (assets != null) {
                    for (int i = 0; i < assets.length(); i++) {
                        JSONObject asset = assets.getJSONObject(i);

                        String name =
                                asset.optString("name", "");

                        if (name.toLowerCase().endsWith(".apk")) {
                            apkUrl = asset.optString(
                                    "browser_download_url",
                                    ""
                            );
                            break;
                        }
                    }
                }

                latestVersion = version;
                latestApkUrl = apkUrl;
                latestWhatsNew = whatsNew;

                String installedVersion =
                        getInstalledVersion();

                if (isNewer(version, installedVersion)) {

                    final String finalApkUrl = apkUrl;

                    activity.runOnUiThread(() -> {
                        showUpdateNotification(
                                version,
                                whatsNew,
                                finalApkUrl
                        );

                        if (manual) {
                            showUpdateDialog(
                                    version,
                                    whatsNew,
                                    finalApkUrl
                            );
                        }
                    });

                } else if (manual) {

                    activity.runOnUiThread(() ->
                            new AlertDialog.Builder(activity)
                                    .setTitle("SELF PAYROLL")
                                    .setMessage(
                                            "You already have the latest version (" +
                                                    installedVersion + ")."
                                    )
                                    .setPositiveButton("OK", null)
                                    .show()
                    );
                }

            } catch (Exception e) {

                if (manual) {
                    activity.runOnUiThread(() ->
                            new AlertDialog.Builder(activity)
                                    .setTitle("Update Check Failed")
                                    .setMessage(
                                            "Could not check for updates. Please check your internet connection and try again."
                                    )
                                    .setPositiveButton("OK", null)
                                    .show()
                    );
                }
            }
        }).start();
    }

    private String getInstalledVersion() {
        try {
            PackageInfo info =
                    activity.getPackageManager()
                            .getPackageInfo(
                                    activity.getPackageName(),
                                    0
                            );

            return info.versionName == null
                    ? "0"
                    : info.versionName;

        } catch (Exception e) {
            return "0";
        }
    }

    private boolean isNewer(
            String latest,
            String installed
    ) {

        try {
            String[] a = latest.split("\\.");
            String[] b = installed.split("\\.");

            int length = Math.max(
                    a.length,
                    b.length
            );

            for (int i = 0; i < length; i++) {

                int x = i < a.length
                        ? Integer.parseInt(
                                a[i].replaceAll("[^0-9]", "")
                        )
                        : 0;

                int y = i < b.length
                        ? Integer.parseInt(
                                b[i].replaceAll("[^0-9]", "")
                        )
                        : 0;

                if (x > y) return true;
                if (x < y) return false;
            }

        } catch (Exception ignored) {
        }

        return false;
    }

    private void showUpdateNotification(
            String version,
            String whatsNew,
            String apkUrl
    ) {

        Intent intent =
                new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(apkUrl)
                );

        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        activity,
                        1001,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT |
                                PendingIntent.FLAG_IMMUTABLE
                );

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(
                        activity,
                        CHANNEL_ID
                )
                        .setSmallIcon(
                                R.drawable.self_payroll_icon
                        )
                        .setContentTitle(
                                "SELF PAYROLL Update Available"
                        )
                        .setContentText(
                                "Version " + version +
                                        " is ready. Tap to update."
                        )
                        .setStyle(
                                new NotificationCompat.BigTextStyle()
                                        .bigText(
                                                "Version " + version +
                                                        "\n\nWhat's New:\n" +
                                                        whatsNew
                                        )
                        )
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setPriority(
                                NotificationCompat.PRIORITY_HIGH
                        );

        NotificationManager manager =
                (NotificationManager)
                        activity.getSystemService(
                                Context.NOTIFICATION_SERVICE
                        );

        manager.notify(
                2026,
                builder.build()
        );
    }

    private void showUpdateDialog(
            String version,
            String whatsNew,
            String apkUrl
    ) {

        new AlertDialog.Builder(activity)
                .setTitle(
                        "SELF PAYROLL " +
                                version +
                                " Available"
                )
                .setMessage(
                        "What's New:\n\n" +
                                whatsNew
                )
                .setNegativeButton(
                        "Later",
                        null
                )
                .setPositiveButton(
                        "Update",
                        (dialog, which) -> {
                            if (
                                    apkUrl != null &&
                                    !apkUrl.isEmpty()
                            ) {
                                activity.startActivity(
                                        new Intent(
                                                Intent.ACTION_VIEW,
                                                Uri.parse(apkUrl)
                                        )
                                );
                            }
                        }
                )
                .show();
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.O) {

            NotificationChannel channel =
                    new NotificationChannel(
                            CHANNEL_ID,
                            "App Updates",
                            NotificationManager.IMPORTANCE_HIGH
                    );

            channel.setDescription(
                    "SELF PAYROLL update notifications"
            );

            NotificationManager manager =
                    activity.getSystemService(
                            NotificationManager.class
                    );

            manager.createNotificationChannel(
                    channel
            );
        }
    }


    public String getLatestVersion() {
        return latestVersion;
    }

    public String getLatestApkUrl() {
        return latestApkUrl;
    }

    public String getLatestWhatsNew() {
        return latestWhatsNew;
    }

}
