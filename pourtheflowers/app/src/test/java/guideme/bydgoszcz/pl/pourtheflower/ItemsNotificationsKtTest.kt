package guideme.bydgoszcz.pl.pourtheflower

import guideme.bydgoszcz.pl.pourtheflower.model.Notification
import guideme.bydgoszcz.pl.pourtheflower.notifications.getRemainingTime
import guideme.bydgoszcz.pl.pourtheflower.utils.SystemTime
import guideme.bydgoszcz.pl.pourtheflower.utils.TimeHelper
import guideme.bydgoszcz.pl.pourtheflower.utils.NotificationTime
import org.junit.Assert.assertEquals
import org.junit.Test

class ItemsNotificationsKtTest {

    @Test
    fun testTakeRemainingTimeInSeconds_firstTime() {
        val repeatDays = 3
        val notification = Notification(true, NotificationTime.fromDays(repeatDays), SystemTime(0))
        val actual = notification.getRemainingTime(SystemTime()).value
        val expected = repeatDays * TimeHelper.timeUnit

        assertEquals(expected, actual)
    }

    @Test
    fun calculateDelay_alreadyShownPreviously() {
        val repeatDays = 3
        val expectedRemainingDays = 2
        val currentTime = SystemTime()
        val lastShown = currentTime - NotificationTime.fromDays(1)
        val notification = Notification(true, NotificationTime.fromDays(repeatDays), lastShown)
        val actual = notification.getRemainingTime(currentTime).value
        val expected = expectedRemainingDays * TimeHelper.timeUnit

        assertEquals(expected, actual)
    }
}