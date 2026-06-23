package com.q25.inputhelper;

import android.app.Activity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public final class MainActivity extends Activity {
    private static final String REPOSITORY_URL = "https://github.com/smh786/q25_input_helper";
    private static final String KOFI_URL = "https://ko-fi.com/sethschroeder";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int padding = dp(20);
        FrameLayout root = new FrameLayout(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(padding, padding, padding, padding);
        root.addView(layout, new FrameLayout.LayoutParams(
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

        TextView setup = new TextView(this);
        setup.setText("Enable the Q25 Input Helper accessibility service to test input fixes.");
        setup.setTextSize(14);
        setup.setPadding(0, dp(16), 0, 0);
        layout.addView(setup);

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
}
