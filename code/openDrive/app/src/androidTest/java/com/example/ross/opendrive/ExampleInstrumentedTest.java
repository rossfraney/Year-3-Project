package com.example.ross.opendrive;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityTestRule<Main2Activity> mLoginActivityTestRule =
            new ActivityTestRule<>(Main2Activity.class);

    @Test
    public void toggleButtonTogglesNotis() throws Exception{
        for(int i = 0; i < 50; i++) {
            onView(withId(R.id.toggleButton)).perform(click());
            assertFalse(MyFirebaseMessagingService.notis);
            onView(withId(R.id.toggleButton)).perform(click());
            assertTrue(MyFirebaseMessagingService.notis);
        }
    }
}
