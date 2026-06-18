package com.q25.inputhelper.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.view.KeyEvent;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CalculatorInputSimulationTest {
    private static final String PACKAGE_NAME = "com.android.calculator2";

    @Test
    public void q25CalculatorKeysClickExpectedButtonsOnStockKeypad() {
        FakeCalculatorKeypad keypad = FakeCalculatorKeypad.stockCalculator2();

        assertEquals("1", keypad.click(KeyEvent.KEYCODE_W));
        assertEquals("2", keypad.click(KeyEvent.KEYCODE_E));
        assertEquals("3", keypad.click(KeyEvent.KEYCODE_R));
        assertEquals("4", keypad.click(KeyEvent.KEYCODE_S));
        assertEquals("5", keypad.click(KeyEvent.KEYCODE_D));
        assertEquals("6", keypad.click(KeyEvent.KEYCODE_F));
        assertEquals("7", keypad.click(KeyEvent.KEYCODE_Z));
        assertEquals("8", keypad.click(KeyEvent.KEYCODE_X));
        assertEquals("9", keypad.click(KeyEvent.KEYCODE_C));
        assertEquals("-", keypad.click(KeyEvent.KEYCODE_I));
        assertEquals("+", keypad.click(KeyEvent.KEYCODE_O));
        assertEquals("*", keypad.click(KeyEvent.KEYCODE_A));
        assertEquals("/", keypad.click(KeyEvent.KEYCODE_G));
        assertEquals(".", keypad.click(KeyEvent.KEYCODE_M));
        assertEquals("!", keypad.click(KeyEvent.KEYCODE_B));
        assertEquals("(", keypad.click(KeyEvent.KEYCODE_T));
        assertEquals(")", keypad.click(KeyEvent.KEYCODE_Y));
        assertEquals("%", keypad.click(KeyEvent.KEYCODE_U));
        assertEquals("scientific toggle", keypad.click(KeyEvent.KEYCODE_SYM));
        assertEquals("backspace", keypad.click(KeyEvent.KEYCODE_DEL));
    }

    @Test
    public void q25CalculatorParenthesesSequenceUsesYForClosingParen() {
        FakeCalculatorKeypad keypad = FakeCalculatorKeypad.stockCalculator2();

        assertEquals("(12)", keypad.clickSequence(
                KeyEvent.KEYCODE_T,
                KeyEvent.KEYCODE_W,
                KeyEvent.KEYCODE_E,
                KeyEvent.KEYCODE_Y
        ));
    }

    @Test
    public void q25CalculatorOperatorKeysClickButtonsWhenOnlyAccessibilityWordsAreAvailable() {
        FakeCalculatorKeypad keypad = FakeCalculatorKeypad.wordLabelOnlyCalculator();

        assertEquals("-", keypad.click(KeyEvent.KEYCODE_I));
        assertEquals("+", keypad.click(KeyEvent.KEYCODE_O));
        assertEquals("*", keypad.click(KeyEvent.KEYCODE_A));
        assertEquals("/", keypad.click(KeyEvent.KEYCODE_G));
    }

    @Test
    public void q25CalculatorKeysAreConsumedWhenTheTargetButtonExists() {
        FakeCalculatorKeypad keypad = FakeCalculatorKeypad.stockCalculator2();

        assertTrue(keypad.consumes(KeyEvent.KEYCODE_W));
        assertTrue(keypad.consumes(KeyEvent.KEYCODE_O));
        assertTrue(keypad.consumes(KeyEvent.KEYCODE_A));
        assertTrue(keypad.consumes(KeyEvent.KEYCODE_DEL));
    }

    private static final class FakeCalculatorKeypad {
        private final List<Button> buttons;

        private FakeCalculatorKeypad(List<Button> buttons) {
            this.buttons = buttons;
        }

        static FakeCalculatorKeypad stockCalculator2() {
            List<Button> buttons = new ArrayList<>();
            buttons.add(button("digit_1", "1", "1"));
            buttons.add(button("digit_2", "2", "2"));
            buttons.add(button("digit_3", "3", "3"));
            buttons.add(button("digit_4", "4", "4"));
            buttons.add(button("digit_5", "5", "5"));
            buttons.add(button("digit_6", "6", "6"));
            buttons.add(button("digit_7", "7", "7"));
            buttons.add(button("digit_8", "8", "8"));
            buttons.add(button("digit_9", "9", "9"));
            buttons.add(button("op_sub", "-", "minus"));
            buttons.add(button("op_add", "+", "plus"));
            buttons.add(button("op_mul", "*", "multiply"));
            buttons.add(button("op_div", "/", "divide"));
            buttons.add(button("op_pct", "%", "percent"));
            buttons.add(button("dec_point", ".", "point"));
            buttons.add(button("parens", "parens", "left or right parenthesis"));
            buttons.add(button("collapse_expand", "scientific toggle", "Show scientific buttons"));
            buttons.add(button("del", "backspace", "delete"));
            return new FakeCalculatorKeypad(buttons);
        }

        static FakeCalculatorKeypad wordLabelOnlyCalculator() {
            return new FakeCalculatorKeypad(Arrays.asList(
                    button("custom_minus", "-", "minus"),
                    button("custom_plus", "+", "plus"),
                    button("custom_multiply", "*", "multiply"),
                    button("custom_divide", "/", "divide")
            ));
        }

        String click(int keyCode) {
            Q25KeyTranslator.Input input = Q25KeyTranslator.toCalculatorInput(keyCode);
            if (input == null) {
                return null;
            }

            String directText = Q25KeyTranslator.calculatorDirectText(input);
            if (directText != null) {
                return directText;
            }

            Button button = findById(input);
            if (button == null) {
                button = findByLabel(input);
            }
            return button == null ? null : button.output;
        }

        boolean consumes(int keyCode) {
            return click(keyCode) != null;
        }

        String clickSequence(int... keyCodes) {
            StringBuilder output = new StringBuilder();
            for (int keyCode : keyCodes) {
                String value = click(keyCode);
                if (value != null) {
                    output.append(value);
                }
            }
            return output.toString();
        }

        private Button findById(Q25KeyTranslator.Input input) {
            String buttonId = Q25KeyTranslator.calculatorButtonId(PACKAGE_NAME, input);
            for (Button button : buttons) {
                if (button.viewId.equals(buttonId)) {
                    return button;
                }
            }
            return null;
        }

        private Button findByLabel(Q25KeyTranslator.Input input) {
            List<String> labels = Q25KeyTranslator.calculatorButtonFallbackLabels(input);
            for (Button button : buttons) {
                if (labels.contains(button.label)) {
                    return button;
                }
            }
            return null;
        }

        private static Button button(String resourceName, String output, String label) {
            return new Button(PACKAGE_NAME + ":id/" + resourceName, output, label);
        }
    }

    private static final class Button {
        final String viewId;
        final String output;
        final String label;

        Button(String viewId, String output, String label) {
            this.viewId = viewId;
            this.output = output;
            this.label = label;
        }
    }
}
