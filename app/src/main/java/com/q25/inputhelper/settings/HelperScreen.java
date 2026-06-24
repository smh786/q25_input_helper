package com.q25.inputhelper.settings;

public enum HelperScreen {
    SYSTEM_UI_PIN(
            "helper_system_ui_pin_enabled",
            "Lockscreen PIN",
            "Route Q25 number keys to the Android lockscreen PIN pad."
    ),
    CALCULATOR(
            "helper_calculator_enabled",
            "Calculator",
            "Route Q25 number and operator keys to supported calculator apps."
    ),
    MESSAGES_COMPOSER(
            "helper_messages_composer_enabled",
            "Messages composer",
            "Send Google Messages drafts with Enter and suppress Alt+Enter in the composer."
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
