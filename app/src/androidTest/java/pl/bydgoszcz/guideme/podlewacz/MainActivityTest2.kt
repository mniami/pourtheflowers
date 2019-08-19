package pl.bydgoszcz.guideme.podlewacz


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import androidx.test.uiautomator.UiDevice
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.sql.Date

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest2 {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Rule
    @JvmField
    var mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.CAMERA")

    @Test
    fun mainActivityTest2() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(3000)

        val floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.drawer_layout),
                                        0),
                                2),
                        isDisplayed()))
        floatingActionButton.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(3000)

        val appCompatImageView = onView(
                allOf(withId(R.id.ivAddPhoto),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.scrollView3),
                                        0),
                                1)))
        appCompatImageView.perform(scrollTo(), click())

        Thread.sleep(2000)
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.click(553, 1700)
        Thread.sleep(3000)
        device.click(860, 1700)
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(3000)

        val textInputEditText = onView(
                allOf(withId(R.id.etName),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.lName),
                                        0),
                                0)))
        textInputEditText.perform(scrollTo(), replaceText("u"), closeSoftKeyboard())

        val appCompatImageButton = onView(
                allOf(withContentDescription("Przejdź wyżej"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(`is`("com.google.android.material.appbar.AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()))
        appCompatImageButton.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(2000)

        for (i in (1..30)) {
            val floatingActionButton = onView(
                    allOf(withId(R.id.fab),
                            childAtPosition(
                                    childAtPosition(
                                            withId(R.id.drawer_layout),
                                            0),
                                    2),
                            isDisplayed()))
            floatingActionButton.perform(click())

            val appCompatImageView2 = onView(
                    allOf(withId(R.id.ivAddPhoto),
                            childAtPosition(
                                    childAtPosition(
                                            withId(R.id.scrollView3),
                                            0),
                                    1)))
            appCompatImageView2.perform(scrollTo(), click())

            Thread.sleep(3000)
            val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            device.click(553, 1700)
            Thread.sleep(3000)
            device.click(860, 1700)

            // Added a sleep statement to match the app's execution delay.
            // The recommended way to handle such scenarios is to use Espresso idling resources:
            // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
            Thread.sleep(2000)

            val textInputEditText2 = onView(
                    allOf(withId(R.id.etName),
                            childAtPosition(
                                    childAtPosition(
                                            withId(R.id.lName),
                                            0),
                                    0)))
            textInputEditText2.perform(scrollTo(), replaceText(Date(System.currentTimeMillis()).toLocaleString()), closeSoftKeyboard())

            val appCompatImageButton2 = onView(
                    allOf(withContentDescription("Przejdź wyżej"),
                            childAtPosition(
                                    allOf(withId(R.id.toolbar),
                                            childAtPosition(
                                                    withClassName(`is`("com.google.android.material.appbar.AppBarLayout")),
                                                    0)),
                                    1),
                            isDisplayed()))
            appCompatImageButton2.perform(click())
        }

        for (i in (1..30)) {
            // Added a sleep statement to match the app's execution delay.
            // The recommended way to handle such scenarios is to use Espresso idling resources:
            // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
            Thread.sleep(1000)

            for (swipe in (1..i/8)) {
                Espresso.onView(withId(R.id.recyclerView)).perform(actionWithAssertions(
                        GeneralSwipeAction(
                                Swipe.FAST,
                                GeneralLocation.VISIBLE_CENTER,
                                GeneralLocation.TOP_CENTER,
                                Press.FINGER)))
                Thread.sleep(500)
            }

            val constraintLayout = onView(
                    allOf(childAtPosition(
                            allOf(withId(R.id.recyclerView),
                                    childAtPosition(
                                            withClassName(`is`("android.widget.RelativeLayout")),
                                            0)),
                            i % 8),
                            isDisplayed()))
            constraintLayout.perform(click())

            // Added a sleep statement to match the app's execution delay.
            // The recommended way to handle such scenarios is to use Espresso idling resources:
            // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
            Thread.sleep(1000)

            val floatingActionButton2 = onView(
                    allOf(withId(R.id.fab),
                            childAtPosition(
                                    childAtPosition(
                                            withId(R.id.drawer_layout),
                                            0),
                                    2),
                            isDisplayed()))
            floatingActionButton2.perform(click())

            // Added a sleep statement to match the app's execution delay.
            // The recommended way to handle such scenarios is to use Espresso idling resources:
            // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
            Thread.sleep(1000)

            val appCompatImageButton3 = onView(
                    allOf(withContentDescription("Przejdź wyżej"),
                            childAtPosition(
                                    allOf(withId(R.id.toolbar),
                                            childAtPosition(
                                                    withClassName(`is`("com.google.android.material.appbar.AppBarLayout")),
                                                    0)),
                                    1),
                            isDisplayed()))
            appCompatImageButton3.perform(click())

            // Added a sleep statement to match the app's execution delay.
            // The recommended way to handle such scenarios is to use Espresso idling resources:
            // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
            Thread.sleep(1000)

            val appCompatImageButton4 = onView(
                    allOf(withContentDescription("Przejdź wyżej"),
                            childAtPosition(
                                    allOf(withId(R.id.toolbar),
                                            childAtPosition(
                                                    withClassName(`is`("com.google.android.material.appbar.AppBarLayout")),
                                                    0)),
                                    1),
                            isDisplayed()))
            appCompatImageButton4.perform(click())
        }
    }

    private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
