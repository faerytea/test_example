package com.github.faerytea.cttdev.tests;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

public class SortSelectionTest {
    @Rule
    public ActivityScenarioRule<MainActivity> mainActivity = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void intSortOpensIntSort() {
        check(SortActivity.Type.INT, R.id.int_sort);
    }


    @Test
    public void charSortOpensCharSort() {
        check(SortActivity.Type.CHAR, R.id.char_sort);
    }


    @Test
    public void wordSortOpensWordSort() {
        check(SortActivity.Type.WORD, R.id.word_sort);
    }

    private void check(SortActivity.Type expected, int id) {
        onView(withId(id)).perform(click());
        intended(allOf(
                hasComponent(hasShortClassName(".SortActivity")),
                toPackage("com.github.faerytea.cttdev.tests"),
                hasExtra(SortActivity.KEY_TYPE, expected.ordinal())));
    }
}
