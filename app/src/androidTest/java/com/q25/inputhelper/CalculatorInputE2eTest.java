package com.q25.inputhelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNotNull;

import android.app.Instrumentation;
import android.os.ParcelFileDescriptor;
import android.view.KeyEvent;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.Until;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

@RunWith(AndroidJUnit4.class)
public final class CalculatorInputE2eTest {
    private static final String SERVICE_COMPONENT =
            "com.q25.inputhelper/com.q25.inputhelper.InputAccessibilityService";
    private static final String GOOGLE_CALCULATOR_PACKAGE = "com.google.android.calculator";
    private static final String AOSP_CALCULATOR_PACKAGE = "com.android.calculator2";
    private static final long TIMEOUT_MS = 5_000L;

    private Instrumentation instrumentation;
    private UiDevice device;
    private String calculatorPackage;

    @Before
    public void setUp() throws Exception {
        instrumentation = InstrumentationRegistry.getInstrumentation();
        device = UiDevice.getInstance(instrumentation);
        calculatorPackage = findInstalledCalculatorPackage();
        assumeNotNull("Calculator app is not installed on this device/emulator", calculatorPackage);

        enableInputHelperService();
        launchCalculator();
        clearCalculator();
    }

    @After
    public void tearDown() throws Exception {
        disableInputHelperService();
    }

    @Test
    public void q25DigitKeysEnterNumbersInCalculator() {
        device.pressKeyCode(KeyEvent.KEYCODE_W);
        device.pressKeyCode(KeyEvent.KEYCODE_E);
        device.pressKeyCode(KeyEvent.KEYCODE_R);

        waitForFormula("123");
    }

    @Test
    public void q25CalculatorKeysEnterOperatorsAndDirectText() {
        device.pressKeyCode(KeyEvent.KEYCODE_T);
        device.pressKeyCode(KeyEvent.KEYCODE_W);
        device.pressKeyCode(KeyEvent.KEYCODE_E);
        device.pressKeyCode(KeyEvent.KEYCODE_Y);
        device.pressKeyCode(KeyEvent.KEYCODE_A);
        device.pressKeyCode(KeyEvent.KEYCODE_R);
        device.pressKeyCode(KeyEvent.KEYCODE_U);

        waitForFormula("(12)×3%");
    }

    private String findInstalledCalculatorPackage() {
        String packages = runShellForOutput("pm list packages");
        if (packages.contains("package:" + GOOGLE_CALCULATOR_PACKAGE)) {
            return GOOGLE_CALCULATOR_PACKAGE;
        }
        if (packages.contains("package:" + AOSP_CALCULATOR_PACKAGE)) {
            return AOSP_CALCULATOR_PACKAGE;
        }
        return null;
    }

    private void enableInputHelperService() throws IOException {
        runShell("settings put secure enabled_accessibility_services " + SERVICE_COMPONENT);
        runShell("settings put secure accessibility_enabled 1");
        device.waitForIdle();
    }

    private void disableInputHelperService() throws IOException {
        runShell("settings delete secure enabled_accessibility_services");
        runShell("settings put secure accessibility_enabled 0");
    }

    private void launchCalculator() throws IOException {
        runShell("monkey -p " + calculatorPackage + " 1");
        assertTrue(device.wait(Until.hasObject(By.pkg(calculatorPackage)), TIMEOUT_MS));
    }

    private void clearCalculator() {
        UiObject2 clearButton = device.wait(
                Until.findObject(By.res(calculatorPackage, "clr")),
                TIMEOUT_MS
        );
        if (clearButton != null) {
            clearButton.click();
            device.waitForIdle();
            return;
        }

        UiObject2 deleteButton = device.findObject(By.res(calculatorPackage, "del"));
        for (int i = 0; i < 20 && deleteButton != null && !formulaText().isEmpty(); i++) {
            deleteButton.click();
            device.waitForIdle();
        }
    }

    private void waitForFormula(String expected) {
        assertTrue(device.wait(Until.findObject(By.res(calculatorPackage, "formula")), TIMEOUT_MS) != null);
        assertTrue(device.wait(
                ignored -> expected.equals(formulaText()),
                TIMEOUT_MS
        ));
        assertEquals(expected, formulaText());
    }

    private String formulaText() {
        UiObject2 formula = device.findObject(By.res(calculatorPackage, "formula"));
        if (formula == null || formula.getText() == null) {
            return "";
        }
        return formula.getText();
    }

    private void runShell(String command) throws IOException {
        ParcelFileDescriptor fd = instrumentation.getUiAutomation().executeShellCommand(command);
        fd.close();
    }

    private String runShellForOutput(String command) {
        try {
            ParcelFileDescriptor fd = instrumentation.getUiAutomation().executeShellCommand(command);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(fd.getFileDescriptor())
            ))) {
                StringBuilder output = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append('\n');
                }
                return output.toString();
            } finally {
                fd.close();
            }
        } catch (IOException e) {
            throw new AssertionError("Shell command failed: " + command, e);
        }
    }
}
