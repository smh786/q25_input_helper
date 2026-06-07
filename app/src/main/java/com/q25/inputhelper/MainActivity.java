package com.q25.inputhelper;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public final class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int padding = dp(20);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(padding, padding, padding, padding);

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

        setContentView(layout);
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
