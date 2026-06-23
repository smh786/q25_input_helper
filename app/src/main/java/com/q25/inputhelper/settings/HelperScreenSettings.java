package com.q25.inputhelper.settings;

import android.content.Context;
import android.content.SharedPreferences;

public final class HelperScreenSettings {
    private static final String PREFERENCES_NAME = "helper_screen_settings";
    private static final boolean DEFAULT_ENABLED = true;

    private HelperScreenSettings() {
    }

    public static boolean isEnabled(Context context, HelperScreen helperScreen) {
        return preferences(context).getBoolean(helperScreen.preferenceKey(), DEFAULT_ENABLED);
    }

    public static void setEnabled(Context context, HelperScreen helperScreen, boolean enabled) {
        preferences(context).edit()
                .putBoolean(helperScreen.preferenceKey(), enabled)
                .apply();
    }

    private static SharedPreferences preferences(Context context) {
        Context storageContext = context.createDeviceProtectedStorageContext();
        if (storageContext == null) {
            storageContext = context;
        }
        return storageContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }
}
