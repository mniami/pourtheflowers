package pl.bydgoszcz.guideme.podlewacz


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
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

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun mainActivityTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(3000)

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

            val appCompatImageView = onView(
                    allOf(withId(R.id.ivAddPhoto),
                            childAtPosition(
                                    childAtPosition(
                                            withId(R.id.scrollView3),
                                            0),
                                    1)))
            appCompatImageView.perform(scrollTo(), click())
            Thread.sleep(3000)

            val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            device.click(553, 1700)
            Thread.sleep(2000)
            device.click(860, 1700)
            // Added a sleep statement to match the app's execution delay.
            // The recommended way to handle such scenarios is to use Espresso idling resources:
            // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
            Thread.sleep(2000)

            val textInputEditText = onView(
                    allOf(withId(R.id.etName),
                            childAtPosition(
                                    childAtPosition(
                                            withId(R.id.lName),
                                            0),
                                    0)))
            textInputEditText.perform(scrollTo(), replaceText("hh"), closeSoftKeyboard())

            // Added a sleep statement to match the app's execution delay.
            // The recommended way to handle such scenarios is to use Espresso idling resources:
            // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
            Thread.sleep(160)

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
