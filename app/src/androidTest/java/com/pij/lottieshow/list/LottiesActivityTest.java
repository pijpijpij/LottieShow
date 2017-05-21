package com.pij.lottieshow.list;


import android.app.Activity;
import android.app.Instrumentation.ActivityResult;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.pij.lottieshow.R;
import com.pij.lottieshow.detail.LottieActivity;
import com.pij.lottieshow.model.LottieUi;

import org.hamcrest.Matcher;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.BundleMatchers.hasValue;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtras;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasType;
import static android.support.test.espresso.intent.matcher.UriMatchers.hasHost;
import static android.support.test.espresso.intent.matcher.UriMatchers.hasPath;
import static android.support.test.espresso.intent.matcher.UriMatchers.hasScheme;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.pij.hamcrest.Matchers.transformed;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;

@RunWith(AndroidJUnit4.class)
public class LottiesActivityTest {

    @Rule
    public IntentsTestRule<LottiesActivity> activity = new IntentsTestRule<>(LottiesActivity.class, false, false);

    public static boolean isTablet() {
        int smallestScreenWidthDp = InstrumentationRegistry.getTargetContext()
                                                           .getResources()
                                                           .getConfiguration().smallestScreenWidthDp;
        return (smallestScreenWidthDp != Configuration.SMALLEST_SCREEN_WIDTH_DP_UNDEFINED &&
                smallestScreenWidthDp >= 600);
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

    @Test
    public void tClickingOnTwitterHeartCallsDetailActivityWithTwitterHeartInExtra() {
        // given
        assumeFalse(isTablet());
        activity.launchActivity(null);

        setupDetailActivityCompletesImmediately();

        // when
        clickEntry("TwitterHeart");

        // then
        intended(hasComponent(hasClassName(LottieActivity.class.getName())));
        Matcher<Uri> idMatcher = allOf(hasScheme("file"),
                                       hasHost(""),
                                       hasPath(allOf(startsWith("/android_asset"), endsWith("TwitterHeart.json"))));
        intended(hasExtras(hasValue(transformed(LottieUi::id, idMatcher))));
        intended(hasExtras(hasValue(transformed(LottieUi::label, is("TwitterHeart")))));
    }

    @Ignore("No implemented yet")
    @Test
    public void tClickingOnTwitterHeartCallsDetailFragmentWithTwitterHeartInExtra() {
        // given
        assumeTrue(isTablet());
        activity.launchActivity(null);

        // when
        clickEntry("TwitterHeart");

        // then
        // TODO code this
    }

    @Test
    public void tAddLottieButtonAvailable() {
        // given
        activity.launchActivity(null);

        // then
        onView(withId(R.id.fab)).check(matches(allOf(isDisplayed(), isEnabled())));
    }

    @Test
    public void tClickingOnAddLottieCallsSAF() {
        // given
        activity.launchActivity(null);
        setupPickFileCanceledImmediately();

        // then
        onView(withId(R.id.fab)).perform(click());

        intended(allOf(hasAction(Intent.ACTION_OPEN_DOCUMENT), hasType("application/json")));
    }

    private void setupPickFileCanceledImmediately() {
        ActivityResult result = new ActivityResult(Activity.RESULT_CANCELED, null);
        intending(allOf(hasAction(Intent.ACTION_OPEN_DOCUMENT), hasType("application/json"))).respondWith(result);
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