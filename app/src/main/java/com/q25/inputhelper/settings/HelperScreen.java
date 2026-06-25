package com.q25.inputhelper.settings;

public enum HelperScreen {
    CHAT_COMPOSER_SHORTCUTS(
            "helper_chat_composer_shortcuts_enabled",
            "Chat composer shortcuts",
            "Use Alt+Enter for new lines and Enter to send on supported message screens."
    ),
    SYSTEM_UI_PIN(
            "helper_system_ui_pin_enabled",
            "Lockscreen PIN",
            "Route Q25 number keys to the Android lockscreen PIN pad."
    ),
    CALCULATOR(
            "helper_calculator_enabled",
            "Calculator",
            "Route Q25 number and operator keys to supported calculator apps."
    );

    private final String preferenceKey;
    private final String title;
    private final String summary;

    HelperScreen(String preferenceKey, String title, String summary) {
        this.preferenceKey = preferenceKey;
        this.title = title;
        this.summary = summary;
    }

    public String preferenceKey() {
        return preferenceKey;
    }

    public String title() {
        return title;
    }

    public String summary() {
        return summary;
    }
}
