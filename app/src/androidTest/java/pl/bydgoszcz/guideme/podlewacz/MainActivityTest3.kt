package pl.bydgoszcz.guideme.podlewacz


import androidx.test.espresso.Espresso
import androidx.test.espresso.action.*
import androidx.test.espresso.action.ViewActions.actionWithAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest3 {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun mainActivityTest3() {
        Thread.sleep(5000)
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView)).perform(actionWithAssertions(
                GeneralSwipeAction(
                        Swipe.FAST,
                        GeneralLocation.VISIBLE_CENTER,
                        GeneralLocation.TOP_CENTER,
                        Press.FINGER)))
        Thread.sleep(5000)
    }
}
