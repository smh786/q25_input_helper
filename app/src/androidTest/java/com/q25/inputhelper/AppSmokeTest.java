package com.q25.inputhelper;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public final class AppSmokeTest {
    @Test
    public void packageNameIsExpected() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.q25.inputhelper", context.getPackageName());
    }
}
