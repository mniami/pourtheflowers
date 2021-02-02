package pl.bydgoszcz.guideme.podlewacz

import android.view.Gravity
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pl.bydgoszcz.guideme.podlewacz.views.fragments.FlowerRecyclerViewAdapter

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class InstrumentationTests {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun new_item_and_navigation_open() {
        onView(withId(R.id.fab))
                .perform(click())
        onView(withId(R.id.etName))
                .perform(click())
                .perform(ViewActions.typeTextIntoFocusedView("test name"))
        pressBack()
        onView(withId(R.id.etDescription))
                .perform(click())
                .perform(ViewActions.typeTextIntoFocusedView("test description"))
        pressBack()
        onView(withId(R.id.chipSun))
                .perform(click())
        onView(withId(R.id.turnNotificationSwitch))
                .check(ViewAssertions.matches(ViewMatchers.isFocusable()))
        pressBack()
        onView(withId(R.id.drawer_layout))
                .check(ViewAssertions.matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.navSettings))
    }
    @Test
    fun edit_item() {
        onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition<FlowerRecyclerViewAdapter.ViewHolder>(0, click()))
        onView(withId(R.id.fab))
                .perform(click())
        onView(withId(R.id.etName))
                .perform(click())
                .perform(ViewActions.typeTextIntoFocusedView("2"))
        pressBack()
    }
}
