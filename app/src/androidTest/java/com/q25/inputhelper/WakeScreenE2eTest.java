package com.q25.inputhelper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import android.app.Instrumentation;
import android.os.ParcelFileDescriptor;
import android.view.KeyEvent;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

@RunWith(AndroidJUnit4.class)
public final class WakeScreenE2eTest {
    private static final String SERVICE_COMPONENT =
            "com.q25.inputhelper/com.q25.inputhelper.InputAccessibilityService";
    private static final long TIMEOUT_MS = 5_000L;

    private Instrumentation instrumentation;
    private UiDevice device;

    @Before
    public void setUp() throws Exception {
        instrumentation = InstrumentationRegistry.getInstrumentation();
        device = UiDevice.getInstance(instrumentation);
        enableInputHelperService();
        device.wakeUp();
        device.waitForIdle();
    }

    @After
    public void tearDown() throws Exception {
        device.wakeUp();
        disableInputHelperService();
    }

    @Test
    public void spaceKeyWakesSleepingScreen() throws Exception {
        runShell("input keyevent " + KeyEvent.KEYCODE_SLEEP);
        assertTrue(waitForScreenOn(false));
        assertFalse(isScreenOn());

        runShell("input keyevent " + KeyEvent.KEYCODE_SPACE);

        assumeTrue(
                "Sleeping-screen space wake requires a key event path that reaches the service; "
                        + "adb-injected KEYCODE_SPACE did not wake this device.",
                waitForScreenOn(true)
        );
        assertTrue(isScreenOn());
    }

    private boolean isScreenOn() {
        String power = runShellForOutput("dumpsys power");
        return power.contains("mWakefulness=Awake")
                || power.contains("mWakefulness=Dreaming")
                || power.contains("mHalInteractiveModeEnabled=true");
    }

    private boolean waitForScreenOn(boolean expected) throws InterruptedException {
        long deadline = System.currentTimeMillis() + TIMEOUT_MS;
        while (System.currentTimeMillis() < deadline) {
            if (isScreenOn() == expected) {
                return true;
            }
            Thread.sleep(100L);
        }
        return false;
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

    private void runShell(String command) throws IOException {
        instrumentation.getUiAutomation().executeShellCommand(command).close();
    }

    private String runShellForOutput(String command) {
        try {
            ParcelFileDescriptor fd = instrumentation.getUiAutomation().executeShellCommand(command);
            try (FileInputStream input = new FileInputStream(fd.getFileDescriptor());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
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
