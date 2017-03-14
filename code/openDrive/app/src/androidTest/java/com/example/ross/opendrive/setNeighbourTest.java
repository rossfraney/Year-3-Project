package com.example.ross.opendrive;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.example.ross.opendrive.Main2Activity.neighboursNum;
import static junit.framework.Assert.assertTrue;

/**
 * Created by ross on 12/03/2017.
 */
@RunWith(AndroidJUnit4.class)
public class setNeighbourTest {


    @Rule
    public ActivityTestRule<setNeighbour> mNeighbourTestRule =
            new ActivityTestRule<>(setNeighbour.class);

    @Test
    public void checkSetNumber() throws Exception{
            onView(withId(R.id.edittext)).perform(typeText("0860000000"));
            onView(withId(R.id.button)).perform(click());
            assertTrue(neighboursNum.equals("0860000000"));
    }
}
