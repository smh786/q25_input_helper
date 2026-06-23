package com.q25.inputhelper;

import android.app.Activity;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.q25.inputhelper.settings.HelperScreen;
import com.q25.inputhelper.settings.HelperScreenSettings;

public final class MainActivity extends Activity {
    private TextView serviceStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int padding = dp(20);
        ScrollView scrollView = new ScrollView(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(padding, padding, padding, padding);
        scrollView.addView(layout);

        TextView title = new TextView(this);
        title.setText("Q25 Input Helper");
        title.setTextSize(22);
        layout.addView(title);

        TextView body = new TextView(this);
        body.setText("Targeted keyboard compatibility fixes for Q25 lockscreen and system app input screens.");
        body.setTextSize(16);
        body.setPadding(0, dp(12), 0, 0);
        layout.addView(body);

        serviceStatus = new TextView(this);
        serviceStatus.setTextSize(14);
        serviceStatus.setPadding(0, dp(20), 0, 0);
        layout.addView(serviceStatus);

        Button accessibilitySettings = new Button(this);
        accessibilitySettings.setText("Open accessibility settings");
        accessibilitySettings.setAllCaps(false);
        accessibilitySettings.setOnClickListener(view ->
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)));
        layout.addView(accessibilitySettings);

        TextView helperTitle = new TextView(this);
        helperTitle.setText("Helper screens");
        helperTitle.setTextSize(18);
        helperTitle.setPadding(0, dp(28), 0, dp(4));
        layout.addView(helperTitle);

        for (HelperScreen helperScreen : HelperScreen.values()) {
            layout.addView(createHelperRow(helperScreen));
        }

        setContentView(scrollView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateServiceStatus();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            updateServiceStatus();
        }
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    private LinearLayout createHelperRow(final HelperScreen helperScreen) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(0, dp(14), 0, dp(14));

        LinearLayout textColumn = new LinearLayout(this);
        textColumn.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        );
        row.addView(textColumn, textParams);

        TextView title = new TextView(this);
        title.setText(helperScreen.title());
        title.setTextSize(16);
        textColumn.addView(title);

        TextView summary = new TextView(this);
        summary.setText(helperScreen.summary());
        summary.setTextSize(13);
        summary.setPadding(0, dp(4), dp(12), 0);
        textColumn.addView(summary);

        Switch enabledSwitch = new Switch(this);
        enabledSwitch.setChecked(HelperScreenSettings.isEnabled(this, helperScreen));
        enabledSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                HelperScreenSettings.setEnabled(this, helperScreen, isChecked));
        row.addView(enabledSwitch);

        row.setOnClickListener(view -> enabledSwitch.setChecked(!enabledSwitch.isChecked()));
        return row;
    }

    private void updateServiceStatus() {
        if (serviceStatus == null) {
            return;
        }

        serviceStatus.setText(isAccessibilityServiceEnabled()
                ? "Accessibility service is enabled."
                : "Accessibility service is disabled.");
    }

    private boolean isAccessibilityServiceEnabled() {
        AccessibilityManager accessibilityManager =
                (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
        if (accessibilityManager == null) {
            return false;
        }

        String expectedPackage = getPackageName();
        String expectedClass = InputAccessibilityService.class.getName();
        for (AccessibilityServiceInfo serviceInfo :
                accessibilityManager.getEnabledAccessibilityServiceList(
                        AccessibilityServiceInfo.FEEDBACK_ALL_MASK)) {
            ResolveInfo resolveInfo = serviceInfo.getResolveInfo();
            ServiceInfo resolvedService = resolveInfo == null ? null : resolveInfo.serviceInfo;
            if (resolvedService != null
                    && expectedPackage.equals(resolvedService.packageName)
                    && expectedClass.equals(resolvedService.name)) {
                return true;
            }
        }
        return false;
    }
}
