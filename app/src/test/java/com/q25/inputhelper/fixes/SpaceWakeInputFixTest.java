package com.q25.inputhelper.fixes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.view.KeyEvent;

import org.junit.Test;

public final class SpaceWakeInputFixTest {
    @Test
    public void spaceDownWakesAndConsumesWhenScreenIsNotInteractive() {
        SpaceWakeInputFix.ScreenWakeController controller =
                new SpaceWakeInputFix.ScreenWakeController();
        FakeWakeAction wakeAction = new FakeWakeAction();

        assertTrue(controller.onKeyEvent(
                () -> false,
                wakeAction,
                KeyEvent.ACTION_DOWN,
                KeyEvent.KEYCODE_SPACE,
                0
        ));
        assertTrue(wakeAction.wokeScreen);
    }

    @Test
    public void spaceIsIgnoredWhenScreenIsInteractive() {
        SpaceWakeInputFix.ScreenWakeController controller =
                new SpaceWakeInputFix.ScreenWakeController();
        FakeWakeAction wakeAction = new FakeWakeAction();

        assertFalse(controller.onKeyEvent(
                () -> true,
                wakeAction,
                KeyEvent.ACTION_DOWN,
                KeyEvent.KEYCODE_SPACE,
                0
        ));
        assertFalse(wakeAction.wokeScreen);
    }

    @Test
    public void nonSpaceKeysAreIgnoredWhenScreenIsNotInteractive() {
        SpaceWakeInputFix.ScreenWakeController controller =
                new SpaceWakeInputFix.ScreenWakeController();
        FakeWakeAction wakeAction = new FakeWakeAction();

        assertFalse(controller.onKeyEvent(
                () -> false,
                wakeAction,
                KeyEvent.ACTION_DOWN,
                KeyEvent.KEYCODE_ENTER,
                0
        ));
        assertFalse(wakeAction.wokeScreen);
    }

    @Test
    public void repeatedSpaceDownIsConsumedWithoutRepeatedWakeAction() {
        SpaceWakeInputFix.ScreenWakeController controller =
                new SpaceWakeInputFix.ScreenWakeController();
        FakeWakeAction wakeAction = new FakeWakeAction();

        assertTrue(controller.onKeyEvent(
                () -> false,
                wakeAction,
                KeyEvent.ACTION_DOWN,
                KeyEvent.KEYCODE_SPACE,
                1
        ));
        assertFalse(wakeAction.wokeScreen);
    }

    @Test
    public void matchingSpaceUpIsConsumedAfterWakeDown() {
        SpaceWakeInputFix.ScreenWakeController controller =
                new SpaceWakeInputFix.ScreenWakeController();

        assertTrue(controller.onKeyEvent(
                () -> false,
                new FakeWakeAction(),
                KeyEvent.ACTION_DOWN,
                KeyEvent.KEYCODE_SPACE,
                0
        ));
        assertTrue(controller.onKeyEvent(
                () -> true,
                new FakeWakeAction(),
                KeyEvent.ACTION_UP,
                KeyEvent.KEYCODE_SPACE,
                0
        ));
    }

    @Test
    public void failedWakeActionDoesNotConsumeSpaceUp() {
        SpaceWakeInputFix.ScreenWakeController controller =
                new SpaceWakeInputFix.ScreenWakeController();

        assertFalse(controller.onKeyEvent(
                () -> false,
                () -> false,
                KeyEvent.ACTION_DOWN,
                KeyEvent.KEYCODE_SPACE,
                0
        ));
        assertFalse(controller.onKeyEvent(
                () -> true,
                new FakeWakeAction(),
                KeyEvent.ACTION_UP,
                KeyEvent.KEYCODE_SPACE,
                0
        ));
    }

    @Test
    public void spaceUpWithoutWakeDownIsIgnored() {
        SpaceWakeInputFix.ScreenWakeController controller =
                new SpaceWakeInputFix.ScreenWakeController();

        assertFalse(controller.onKeyEvent(
                () -> true,
                new FakeWakeAction(),
                KeyEvent.ACTION_UP,
                KeyEvent.KEYCODE_SPACE,
                0
        ));
    }

    private static final class FakeWakeAction implements SpaceWakeInputFix.WakeAction {
        boolean wokeScreen;

        @Override
        public boolean wakeScreen() {
            wokeScreen = true;
            return true;
        }
    }
}
