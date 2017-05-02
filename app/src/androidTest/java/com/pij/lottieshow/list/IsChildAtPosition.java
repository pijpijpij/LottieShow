package com.pij.lottieshow.list;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * <p>Created on 02/05/2017.</p>
 * @author Pierrejean
 */
@SuppressWarnings("WeakerAccess")
public class IsChildAtPosition extends TypeSafeMatcher<View> {

    private final Matcher<View> parentMatcher;
    private final int position;

    @SuppressWarnings("WeakerAccess")
    public static Matcher<View> childAtPosition(Matcher<View> parentMatcher, final int position) {
        return new IsChildAtPosition(parentMatcher, position);
    }

    @SuppressWarnings("WeakerAccess")
    public IsChildAtPosition(Matcher<View> parentMatcher, int position) {
        this.parentMatcher = parentMatcher;
        this.position = position;
    }

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
}
