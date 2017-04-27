package com.pij.lottieshow.list;


import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.pij.lottieshow.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * First test, created with the Espresso test recorder and slightly changed.
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class RecordedTest {

    @Rule
    public ActivityTestRule<LottiesActivity> mActivityTestRule = new ActivityTestRule<>(LottiesActivity.class);

    private static Matcher<View> childAtPosition(final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent) &&
                       view.equals(((ViewGroup)parent).getChildAt(position));
            }
        };
    }

    @Test
    public void tClickingOnTwitterHeartShowsDetailWithTitleTwitterHeart() {
        ViewInteraction recyclerView = onView(allOf(withId(R.id.lottie_list), isDisplayed()));
        recyclerView.perform(actionOnItem(hasDescendant(withText("TwitterHeart")), click()));

        ViewInteraction title = onView(allOf(withText("TwitterHeart"),
                                             childAtPosition(allOf(withId(R.id.toolbar),
                                                                   childAtPosition(withId(R.id.app_bar), 0)), 0),
                                             isDisplayed()));

        title.check(matches(withText("TwitterHeart")));

    }
}
