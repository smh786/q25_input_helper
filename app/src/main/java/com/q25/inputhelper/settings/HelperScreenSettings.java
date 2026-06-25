package com.q25.inputhelper.settings;

import android.content.Context;
import android.content.SharedPreferences;

public final class HelperScreenSettings {
    private static final String PREFERENCES_NAME = "helper_screen_settings";
    private static final boolean DEFAULT_ENABLED = true;
    private static final String LEGACY_ALT_ENTER_NEWLINE_KEY = "helper_alt_enter_newline_enabled";
    private static final String LEGACY_SEND_ON_ENTER_KEY = "helper_send_on_enter_enabled";

    private HelperScreenSettings() {
    }

    public static boolean isEnabled(Context context, HelperScreen helperScreen) {
        SharedPreferences preferences = preferences(context);
        if (helperScreen == HelperScreen.CHAT_COMPOSER_SHORTCUTS
                && !preferences.contains(helperScreen.preferenceKey())) {
            return preferences.getBoolean(LEGACY_ALT_ENTER_NEWLINE_KEY, DEFAULT_ENABLED)
                    && preferences.getBoolean(LEGACY_SEND_ON_ENTER_KEY, DEFAULT_ENABLED);
        }
        return preferences.getBoolean(helperScreen.preferenceKey(), DEFAULT_ENABLED);
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
