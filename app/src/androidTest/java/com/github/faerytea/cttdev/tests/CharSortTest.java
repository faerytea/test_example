package com.github.faerytea.cttdev.tests;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.util.Log;
import android.widget.Button;

import androidx.core.content.ContextCompat;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.lifecycle.ActivityLifecycleCallback;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitor;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import org.junit.Rule;
import org.junit.Test;
import org.junit.internal.runners.statements.FailOnTimeout;
import org.junit.runners.model.Statement;

import java.util.Collection;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.runner.lifecycle.Stage.RESUMED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CharSortTest {
    public static final String TEXT        = "abacabaabaca";
    public static final String SORTED_TEXT = "aaaaaaabbbcc";
    @Rule
    public ActivityScenarioRule<MainActivity> mainActivity = new ActivityScenarioRule<>(MainActivity.class);
    private volatile boolean hit = false;

    @Test
    public void checkSortingCharsScenario() {
        onView(withId(R.id.char_sort)).perform(click());
        onView(withId(R.id.data)).perform(typeText(TEXT)).check(matches(withText(TEXT)));
        onView(withText(R.string.sort)).perform(click());
        onView(withId(R.id.result)).check(matches(withText(SORTED_TEXT))).perform(click());
        mainActivity.getScenario().onActivity(a -> {
            final ClipboardManager clipboardManager = ContextCompat.getSystemService(a, ClipboardManager.class);
            assertNotNull(clipboardManager);
            final ClipData primaryClip = clipboardManager.getPrimaryClip();
            assertNotNull(primaryClip);
            assertEquals(1, primaryClip.getItemCount());
            assertEquals(SORTED_TEXT, primaryClip.getItemAt(0).getText());
        });
        final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        hit = false;
        final ActivityLifecycleMonitor activityLifecycleMonitor = ActivityLifecycleMonitorRegistry.getInstance();
        final ActivityLifecycleCallback callback = (a, stage) -> {
            Log.d("TEST", a.getClass().getSimpleName() + " is " + stage);
            if (stage == RESUMED && a.getClass().getSimpleName().equals(SortActivity.class.getSimpleName())) {
                hit = true;
                synchronized (this) {
                    notify();
                }
            }
        };
        instrumentation.runOnMainSync(() -> {
            final Collection<Activity> resumedActivities = activityLifecycleMonitor
                    .getActivitiesInStage(RESUMED);
            if (resumedActivities.iterator().hasNext()) {
                final Activity activity = resumedActivities.iterator().next();
                activity.recreate();
                activityLifecycleMonitor.addLifecycleCallback(callback);
            }
        });
        final long now = System.currentTimeMillis();
        while (!hit) {
            try {
                synchronized (this) {
                    wait(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new AssertionError(e);
            }
            if (System.currentTimeMillis() - now >= 10_000) {
                activityLifecycleMonitor.removeLifecycleCallback(callback);
                throw new IllegalStateException();
            }
        }
        activityLifecycleMonitor.removeLifecycleCallback(callback);
        onView(withId(R.id.result)).check(matches(withText(SORTED_TEXT)));
    }
}
