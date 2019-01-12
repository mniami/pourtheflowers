package guideme.bydgoszcz.pl.pourtheflower

import guideme.bydgoszcz.pl.pourtheflower.model.Notification
import guideme.bydgoszcz.pl.pourtheflower.notifications.getRemainingTime
import guideme.bydgoszcz.pl.pourtheflower.utils.NotificationTime
import guideme.bydgoszcz.pl.pourtheflower.utils.SystemTime
import guideme.bydgoszcz.pl.pourtheflower.utils.TimeHelper
import org.junit.Assert.assertEquals
import org.junit.Test

class NotificationTimeTest {

    @Test
    fun testRemainingDays() {
        listOf(
                TestData(3, 2, 1),
                TestData(3, 3, 0),
                TestData(3, -2, 5)
        ).forEach {
            testData(it)
        }
    }

    private fun testData(testData: TestData) {
        val currentTime = SystemTime.current()
        val lastShown = currentTime - SystemTime.fromDays(testData.lastShownDays)
        val notification = Notification(true, NotificationTime.fromDays(testData.repeatDays), lastShown)
        val actual = notification.getRemainingTime(currentTime).seconds
        val expected = testData.expectedRemainingDays * TimeHelper.secondsInDay

        assertEquals(expected, actual)
    }

    class TestData(val repeatDays: Int, val expectedRemainingDays: Int, val lastShownDays: Int)
}