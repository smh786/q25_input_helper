package com.q25.inputhelper.input;

import android.view.KeyEvent;

import java.util.Collections;
import java.util.List;

public final class Q25KeyTranslator {
    public enum Input {
        DIGIT_0,
        DIGIT_1,
        DIGIT_2,
        DIGIT_3,
        DIGIT_4,
        DIGIT_5,
        DIGIT_6,
        DIGIT_7,
        DIGIT_8,
        DIGIT_9,
        ENTER,
        DELETE,
    }

    private Q25KeyTranslator() {
    }

    public static Input toPinInput(int keyCode) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_W:
            case KeyEvent.KEYCODE_1:
                return Input.DIGIT_1;
            case KeyEvent.KEYCODE_E:
            case KeyEvent.KEYCODE_2:
                return Input.DIGIT_2;
            case KeyEvent.KEYCODE_R:
            case KeyEvent.KEYCODE_3:
                return Input.DIGIT_3;
            case KeyEvent.KEYCODE_S:
            case KeyEvent.KEYCODE_4:
                return Input.DIGIT_4;
            case KeyEvent.KEYCODE_D:
            case KeyEvent.KEYCODE_5:
                return Input.DIGIT_5;
            case KeyEvent.KEYCODE_F:
            case KeyEvent.KEYCODE_6:
                return Input.DIGIT_6;
            case KeyEvent.KEYCODE_Z:
            case KeyEvent.KEYCODE_7:
                return Input.DIGIT_7;
            case KeyEvent.KEYCODE_X:
            case KeyEvent.KEYCODE_8:
                return Input.DIGIT_8;
            case KeyEvent.KEYCODE_C:
            case KeyEvent.KEYCODE_9:
                return Input.DIGIT_9;
            case KeyEvent.KEYCODE_0:
                return Input.DIGIT_0;
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_DPAD_CENTER:
                return Input.ENTER;
            case KeyEvent.KEYCODE_DEL:
                return Input.DELETE;
            default:
                return null;
        }
    }

    public static String systemUiPinButtonId(Input input) {
        if (input == null) return null;

        switch (input) {
            case DIGIT_0:
                return "com.android.systemui:id/key0";
            case DIGIT_1:
                return "com.android.systemui:id/key1";
            case DIGIT_2:
                return "com.android.systemui:id/key2";
            case DIGIT_3:
                return "com.android.systemui:id/key3";
            case DIGIT_4:
                return "com.android.systemui:id/key4";
            case DIGIT_5:
                return "com.android.systemui:id/key5";
            case DIGIT_6:
                return "com.android.systemui:id/key6";
            case DIGIT_7:
                return "com.android.systemui:id/key7";
            case DIGIT_8:
                return "com.android.systemui:id/key8";
            case DIGIT_9:
                return "com.android.systemui:id/key9";
            case ENTER:
                return "com.android.systemui:id/key_enter";
            case DELETE:
                return "com.android.systemui:id/delete_button";
            default:
                return null;
        }
    }

    public static List<String> systemUiPinButtonFallbackLabels(Input input) {
        if (input == null) return Collections.emptyList();

        switch (input) {
            case DIGIT_0:
                return Collections.singletonList("0");
            case DIGIT_1:
                return Collections.singletonList("1");
            case DIGIT_2:
                return Collections.singletonList("2");
            case DIGIT_3:
                return Collections.singletonList("3");
            case DIGIT_4:
                return Collections.singletonList("4");
            case DIGIT_5:
                return Collections.singletonList("5");
            case DIGIT_6:
                return Collections.singletonList("6");
            case DIGIT_7:
                return Collections.singletonList("7");
            case DIGIT_8:
                return Collections.singletonList("8");
            case DIGIT_9:
                return Collections.singletonList("9");
            default:
                return Collections.emptyList();
        }
    }
}
