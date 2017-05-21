package com.pij.lottieshow.list;


import android.app.Activity;
import android.app.Instrumentation.ActivityResult;
import android.content.Intent;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.pij.lottieshow.R;
import com.pij.lottieshow.detail.LottieActivity;
import com.pij.lottieshow.model.LottieUi;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class LottieActivityTest {

    @Rule
    public IntentsTestRule<LottieActivity> activity = new IntentsTestRule<>(LottieActivity.class, false, false);

    @Test
    public void tOpeningWithUnknownInternalLottieDisplaysASnackbar() {
        // given
        Intent intent = LottieActivity.createIntent(InstrumentationRegistry.getTargetContext(),
                                                    LottieUi.create(Uri.parse("http://sdvasdvsdv"), "whatever"));

        // when
        activity.launchActivity(intent);

        // then
        onView(withId(android.support.design.R.id.snackbar_text)).check(matches(isDisplayed()));
    }

    @Test
    public void tOpeningWithUnknownExternalLottieDisplaysASnackbar() {
        // given
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://sdvasdvsdv")).setType("application/json");

        // when
        activity.launchActivity(intent);

        // then
        onView(withId(android.support.design.R.id.snackbar_text)).check(matches(isDisplayed()));
    }

    @Test
    public void tVersionMenuAvailable() {
        // given
        activity.launchActivity(null);

        // when
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        // then
        onView(withText("Lottie library 1.5.3")).check(matches(allOf(isDisplayed(), isEnabled())));
    }

    /**
     * For when you don't care about the detail activity and want it to return immediately.
     */
    private void setupDetailActivityCompletesImmediately() {
        ActivityResult result = new ActivityResult(Activity.RESULT_OK, null);
        intending(hasComponent(hasClassName(LottieActivity.class.getName()))).respondWith(result);
    }

    private void clickEntry(String label) {
        ViewInteraction recyclerView = onView(allOf(withId(R.id.list), isDisplayed()));
        recyclerView.perform(actionOnItem(hasDescendant(withText(label)), click()));
    }

}