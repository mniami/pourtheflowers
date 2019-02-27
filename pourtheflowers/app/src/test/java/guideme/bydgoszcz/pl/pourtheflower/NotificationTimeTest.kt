package guideme.bydgoszcz.pl.pourtheflower

import guideme.bydgoszcz.pl.pourtheflower.model.Notification
import guideme.bydgoszcz.pl.pourtheflower.notifications.getRemainingTime
import guideme.bydgoszcz.pl.pourtheflower.utils.NotificationTime
import guideme.bydgoszcz.pl.pourtheflower.utils.SystemTime
import guideme.bydgoszcz.pl.pourtheflower.utils.SystemTime.Companion.current
import guideme.bydgoszcz.pl.pourtheflower.utils.TimeHelper
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class NotificationTimeTest {

    @Before
    fun before() {
        SystemTime.timeProvider = object : SystemTime.TimeProvider {
            override fun current(): Long {
                return TimeHelper.millisInDay * 10L
            }
        }
    }

    @After
    fun after() {
        SystemTime.timeProvider = SystemTime.DefaultTimeProvider()
    }

    @Test
    fun test_getRemainingTime_one_day() {
        testData(TestData(3, NotificationTime.fromDays(2), current().minus(NotificationTime.fromDays(1))))
    }
    @Test
    fun test_getRemainingTime_zero_days() {
        testData(TestData(1, NotificationTime.fromDays(1).minus(NotificationTime.fromSeconds(1)), current().minus(NotificationTime.fromSeconds(1))))
    }

    @Test
    fun test_getRemainingTime_five_days() {
        testData(TestData(3, NotificationTime.fromDays(-2), current().minus(NotificationTime.fromDays(5))))
    }

    private fun testData(testData: TestData) {
        val currentTime = SystemTime.current()
        val notification = Notification(true, NotificationTime.fromDays(testData.repeatDays), testData.lastShownTime)
        val actual = notification.getRemainingTime(currentTime)
        val expected = testData.expectedRemainingTime

        assertEquals(expected, actual)
    }

    class TestData(val repeatDays: Int, val expectedRemainingTime: NotificationTime, val lastShownTime: SystemTime)
}