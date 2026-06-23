package com.q25.inputhelper;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.Gravity;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.q25.inputhelper.settings.HelperScreen;
import com.q25.inputhelper.settings.HelperScreenSettings;

public final class MainActivity extends Activity {
    private static final String REPOSITORY_URL = "https://github.com/smh786/q25_input_helper";
    private static final String KOFI_URL = "https://ko-fi.com/sethschroeder";

    private TextView serviceStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int padding = dp(20);
        FrameLayout root = new FrameLayout(this);

        ScrollView scrollView = new ScrollView(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(padding, padding, padding, padding + dp(72));
        scrollView.addView(layout);
        root.addView(scrollView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));

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

        TextView footer = new TextView(this);
        footer.setText(buildFooterText());
        footer.setTextSize(12);
        footer.setGravity(Gravity.END);
        footer.setMovementMethod(LinkMovementMethod.getInstance());
        footer.setPadding(padding, dp(8), padding, padding);

        FrameLayout.LayoutParams footerParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM | Gravity.END
        );
        root.addView(footer, footerParams);

        setContentView(root);
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

    private SpannableStringBuilder buildFooterText() {
        SpannableStringBuilder text = new SpannableStringBuilder()
                .append("v")
                .append(BuildConfig.VERSION_NAME)
                .append("\n")
                .append("GitHub")
                .append(" | ")
                .append("Ko-fi");

        setLink(text, "GitHub", REPOSITORY_URL);
        setLink(text, "Ko-fi", KOFI_URL);
        return text;
    }

    private void setLink(SpannableStringBuilder text, String label, String url) {
        int start = text.toString().indexOf(label);
        if (start == -1) {
            return;
        }
        text.setSpan(new URLSpan(url), start, start + label.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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
