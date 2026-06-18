package com.q25.inputhelper.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.view.KeyEvent;

import org.junit.Test;

public final class Q25KeyTranslatorTest {
    @Test
    public void q25PinKeysMapToDigits() {
        assertEquals(Q25KeyTranslator.Input.DIGIT_1, Q25KeyTranslator.toPinInput(KeyEvent.KEYCODE_W));
        assertEquals(Q25KeyTranslator.Input.DIGIT_5, Q25KeyTranslator.toPinInput(KeyEvent.KEYCODE_D));
        assertEquals(Q25KeyTranslator.Input.DIGIT_9, Q25KeyTranslator.toPinInput(KeyEvent.KEYCODE_C));
        assertEquals(Q25KeyTranslator.Input.DIGIT_0, Q25KeyTranslator.toPinInput(KeyEvent.KEYCODE_0));
    }

    @Test
    public void controlKeysMapToPinActions() {
        assertEquals(Q25KeyTranslator.Input.ENTER, Q25KeyTranslator.toPinInput(KeyEvent.KEYCODE_ENTER));
        assertEquals(Q25KeyTranslator.Input.ENTER, Q25KeyTranslator.toPinInput(KeyEvent.KEYCODE_DPAD_CENTER));
        assertEquals(Q25KeyTranslator.Input.DELETE, Q25KeyTranslator.toPinInput(KeyEvent.KEYCODE_DEL));
    }

    @Test
    public void unrelatedKeysAreIgnored() {
        assertNull(Q25KeyTranslator.toPinInput(KeyEvent.KEYCODE_A));
        assertNull(Q25KeyTranslator.toPinInput(KeyEvent.KEYCODE_ALT_RIGHT));
    }

    @Test
    public void pinInputsMapToSystemUiButtonIds() {
        assertEquals("com.android.systemui:id/key1",
                Q25KeyTranslator.systemUiPinButtonId(Q25KeyTranslator.Input.DIGIT_1));
        assertEquals("com.android.systemui:id/key_enter",
                Q25KeyTranslator.systemUiPinButtonId(Q25KeyTranslator.Input.ENTER));
        assertEquals("com.android.systemui:id/delete_button",
                Q25KeyTranslator.systemUiPinButtonId(Q25KeyTranslator.Input.DELETE));
    }

    @Test
    public void pinInputsExposeAccessibilityLabelsForFallbackMatching() {
        assertEquals("1", Q25KeyTranslator.systemUiPinButtonFallbackLabels(
                Q25KeyTranslator.Input.DIGIT_1).get(0));
        assertTrue(Q25KeyTranslator.systemUiPinButtonFallbackLabels(
                Q25KeyTranslator.Input.ENTER).isEmpty());
        assertTrue(Q25KeyTranslator.systemUiPinButtonFallbackLabels(
                Q25KeyTranslator.Input.DELETE).isEmpty());
    }

    @Test
    public void q25CalculatorKeysMapToDigits() {
        assertEquals(Q25KeyTranslator.Input.DIGIT_1, Q25KeyTranslator.toCalculatorInput(KeyEvent.KEYCODE_W));
        assertEquals(Q25KeyTranslator.Input.DIGIT_2, Q25KeyTranslator.toCalculatorInput(KeyEvent.KEYCODE_E));
        assertEquals(Q25KeyTranslator.Input.DIGIT_3, Q25KeyTranslator.toCalculatorInput(KeyEvent.KEYCODE_R));
        assertEquals(Q25KeyTranslator.Input.DIGIT_4, Q25KeyTranslator.toCalculatorInput(KeyEvent.KEYCODE_S));
        assertEquals(Q25KeyTranslator.Input.DIGIT_5, Q25KeyTranslator.toCalculatorInput(KeyEvent.KEYCODE_D));
        assertEquals(Q25KeyTranslator.Input.DIGIT_6, Q25KeyTranslator.toCalculatorInput(KeyEvent.KEYCODE_F));
        assertEquals(Q25KeyTranslator.Input.DIGIT_7, Q25KeyTranslator.toCalculatorInput(KeyEvent.KEYCODE_Z));
        assertEquals(Q25KeyTranslator.Input.DIGIT_8, Q25KeyTranslator.toCalculatorInput(KeyEvent.KEYCODE_X));
        assertEquals(Q25KeyTranslator.Input.DIGIT_9, Q25KeyTranslator.toCalculatorInput(KeyEvent.KEYCODE_C));
    }

    @Test
    public void calculatorOperatorKeysMapToActions() {
        assertEquals(Q25KeyTranslator.Input.SUBTRACT, Q25KeyTranslator.toCalculatorInput(KeyEvent.KEYCODE_I));
        assertEquals(Q25KeyTranslator.Input.ADD, Q25KeyTranslator.toCalculatorInput(KeyEvent.KEYCODE_O));
        assertEquals(Q25KeyTranslator.Input.MULTIPLY, Q25KeyTranslator.toCalculatorInput(KeyEvent.KEYCODE_A));
        assertEquals(Q25KeyTranslator.Input.FACTORIAL, Q25KeyTranslator.toCalculatorInput(KeyEvent.KEYCODE_B));
        assertEquals(Q25KeyTranslator.Input.DIVIDE, Q25KeyTranslator.toCalculatorInput(KeyEvent.KEYCODE_G));
        assertEquals(Q25KeyTranslator.Input.DECIMAL, Q25KeyTranslator.toCalculatorInput(KeyEvent.KEYCODE_M));
        assertEquals(Q25KeyTranslator.Input.LEFT_PAREN, Q25KeyTranslator.toCalculatorInput(KeyEvent.KEYCODE_T));
        assertEquals(Q25KeyTranslator.Input.RIGHT_PAREN, Q25KeyTranslator.toCalculatorInput(KeyEvent.KEYCODE_Y));
        assertEquals(Q25KeyTranslator.Input.PERCENT, Q25KeyTranslator.toCalculatorInput(KeyEvent.KEYCODE_U));
        assertEquals(Q25KeyTranslator.Input.SCIENTIFIC_TOGGLE, Q25KeyTranslator.toCalculatorInput(KeyEvent.KEYCODE_SYM));
        assertEquals(Q25KeyTranslator.Input.SCIENTIFIC_TOGGLE, Q25KeyTranslator.toCalculatorInput(KeyEvent.KEYCODE_ALT_LEFT));
        assertEquals(Q25KeyTranslator.Input.SCIENTIFIC_TOGGLE, Q25KeyTranslator.toCalculatorInput(KeyEvent.KEYCODE_ALT_RIGHT));
        assertEquals(Q25KeyTranslator.Input.DELETE, Q25KeyTranslator.toCalculatorInput(KeyEvent.KEYCODE_DEL));
    }

    @Test
    public void unrelatedCalculatorKeysAreIgnored() {
        assertNull(Q25KeyTranslator.toCalculatorInput(KeyEvent.KEYCODE_P));
    }

    @Test
    public void calculatorInputsMapToCalculatorButtonIds() {
        assertEquals("com.android.calculator2:id/digit_1",
                Q25KeyTranslator.calculatorButtonId(Q25KeyTranslator.Input.DIGIT_1));
        assertEquals("com.android.calculator2:id/op_sub",
                Q25KeyTranslator.calculatorButtonId(Q25KeyTranslator.Input.SUBTRACT));
        assertEquals("com.android.calculator2:id/op_add",
                Q25KeyTranslator.calculatorButtonId(Q25KeyTranslator.Input.ADD));
        assertEquals("com.android.calculator2:id/op_mul",
                Q25KeyTranslator.calculatorButtonId(Q25KeyTranslator.Input.MULTIPLY));
        assertEquals("com.android.calculator2:id/op_div",
                Q25KeyTranslator.calculatorButtonId(Q25KeyTranslator.Input.DIVIDE));
        assertEquals("com.android.calculator2:id/dec_point",
                Q25KeyTranslator.calculatorButtonId(Q25KeyTranslator.Input.DECIMAL));
        assertEquals("com.android.calculator2:id/op_pct",
                Q25KeyTranslator.calculatorButtonId(Q25KeyTranslator.Input.PERCENT));
        assertEquals("com.android.calculator2:id/parens",
                Q25KeyTranslator.calculatorButtonId(Q25KeyTranslator.Input.LEFT_PAREN));
        assertEquals("com.android.calculator2:id/parens",
                Q25KeyTranslator.calculatorButtonId(Q25KeyTranslator.Input.RIGHT_PAREN));
        assertEquals("com.android.calculator2:id/del",
                Q25KeyTranslator.calculatorButtonId(Q25KeyTranslator.Input.DELETE));
        assertEquals("com.android.calculator2:id/collapse_expand",
                Q25KeyTranslator.calculatorButtonId(Q25KeyTranslator.Input.SCIENTIFIC_TOGGLE));
    }

    @Test
    public void calculatorInputsMapToActiveCalculatorPackageButtonIds() {
        assertEquals("com.google.android.calculator:id/digit_1",
                Q25KeyTranslator.calculatorButtonId(
                        "com.google.android.calculator",
                        Q25KeyTranslator.Input.DIGIT_1));
        assertEquals("com.google.android.calculator:id/op_add",
                Q25KeyTranslator.calculatorButtonId(
                        "com.google.android.calculator",
                        Q25KeyTranslator.Input.ADD));
        assertEquals("com.google.android.calculator:id/del",
                Q25KeyTranslator.calculatorButtonId(
                        "com.google.android.calculator",
                        Q25KeyTranslator.Input.DELETE));
        assertEquals("com.google.android.calculator:id/dec_point",
                Q25KeyTranslator.calculatorButtonId(
                        "com.google.android.calculator",
                        Q25KeyTranslator.Input.DECIMAL));
        assertEquals("com.google.android.calculator:id/op_pct",
                Q25KeyTranslator.calculatorButtonId(
                        "com.google.android.calculator",
                        Q25KeyTranslator.Input.PERCENT));
        assertEquals("com.google.android.calculator:id/parens",
                Q25KeyTranslator.calculatorButtonId(
                        "com.google.android.calculator",
                        Q25KeyTranslator.Input.LEFT_PAREN));
        assertEquals("com.google.android.calculator:id/collapse_expand",
                Q25KeyTranslator.calculatorButtonId(
                        "com.google.android.calculator",
                        Q25KeyTranslator.Input.SCIENTIFIC_TOGGLE));
    }

    @Test
    public void calculatorInputsExposeAccessibilityLabelsForFallbackMatching() {
        assertEquals("1", Q25KeyTranslator.calculatorButtonFallbackLabels(
                Q25KeyTranslator.Input.DIGIT_1).get(0));
        assertEquals("minus", Q25KeyTranslator.calculatorButtonFallbackLabels(
                Q25KeyTranslator.Input.SUBTRACT).get(0));
        assertEquals("plus", Q25KeyTranslator.calculatorButtonFallbackLabels(
                Q25KeyTranslator.Input.ADD).get(0));
        assertEquals("multiply", Q25KeyTranslator.calculatorButtonFallbackLabels(
                Q25KeyTranslator.Input.MULTIPLY).get(0));
        assertEquals("divide", Q25KeyTranslator.calculatorButtonFallbackLabels(
                Q25KeyTranslator.Input.DIVIDE).get(0));
        assertEquals("point", Q25KeyTranslator.calculatorButtonFallbackLabels(
                Q25KeyTranslator.Input.DECIMAL).get(0));
        assertEquals("percent", Q25KeyTranslator.calculatorButtonFallbackLabels(
                Q25KeyTranslator.Input.PERCENT).get(0));
        assertEquals("left or right parenthesis", Q25KeyTranslator.calculatorButtonFallbackLabels(
                Q25KeyTranslator.Input.LEFT_PAREN).get(0));
        assertEquals("left or right parenthesis", Q25KeyTranslator.calculatorButtonFallbackLabels(
                Q25KeyTranslator.Input.RIGHT_PAREN).get(0));
        assertTrue(Q25KeyTranslator.calculatorButtonFallbackLabels(
                Q25KeyTranslator.Input.DELETE).contains("Delete"));
        assertTrue(Q25KeyTranslator.calculatorButtonFallbackLabels(
                Q25KeyTranslator.Input.SCIENTIFIC_TOGGLE).contains("Show scientific buttons"));
        assertTrue(Q25KeyTranslator.calculatorButtonFallbackLabels(
                Q25KeyTranslator.Input.SCIENTIFIC_TOGGLE).contains("Hide scientific buttons"));
    }

    @Test
    public void calculatorLeavesReturnKeysForTheAppToHandle() {
        assertNull(Q25KeyTranslator.toCalculatorInput(KeyEvent.KEYCODE_ENTER));
        assertNull(Q25KeyTranslator.toCalculatorInput(KeyEvent.KEYCODE_DPAD_CENTER));
        assertNull(Q25KeyTranslator.calculatorButtonId(Q25KeyTranslator.Input.ENTER));
        assertTrue(Q25KeyTranslator.calculatorButtonFallbackLabels(Q25KeyTranslator.Input.ENTER).isEmpty());
    }

    @Test
    public void calculatorParenthesesUseDirectText() {
        assertEquals("!", Q25KeyTranslator.calculatorDirectText(Q25KeyTranslator.Input.FACTORIAL));
        assertEquals("(", Q25KeyTranslator.calculatorDirectText(Q25KeyTranslator.Input.LEFT_PAREN));
        assertEquals(")", Q25KeyTranslator.calculatorDirectText(Q25KeyTranslator.Input.RIGHT_PAREN));
        assertNull(Q25KeyTranslator.calculatorDirectText(Q25KeyTranslator.Input.PERCENT));
    }
}
