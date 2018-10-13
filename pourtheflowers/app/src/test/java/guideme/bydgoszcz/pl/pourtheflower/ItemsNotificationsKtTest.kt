package guideme.bydgoszcz.pl.pourtheflower

import guideme.bydgoszcz.pl.pourtheflower.model.Notification
import guideme.bydgoszcz.pl.pourtheflower.notifications.calculateDelay
import guideme.bydgoszcz.pl.pourtheflower.utils.TimeHelper
import org.junit.Assert.assertEquals
import org.junit.Test

class ItemsNotificationsKtTest {

    @Test
    fun calculateDelay() {
        val repeatDays = 3
        val notification = Notification(true, repeatDays, 0)
        val actual = notification.calculateDelay(System.currentTimeMillis())
        val expected = repeatDays * TimeHelper.countMillisInDay

        assertEquals(actual, expected)
    }

    @Test
    fun calculateDelay_alreadyShownPreviously() {
        val repeatDays = 3
        val expectedRemainingDays = 2
        val currentTime = System.currentTimeMillis()
        val lastShown = currentTime - TimeHelper.countMillisInDay * 1
        val notification = Notification(true, repeatDays, lastShown)
        val actual = notification.calculateDelay(currentTime)
        val expected = expectedRemainingDays * TimeHelper.countMillisInDay

        assertEquals(actual, expected)
    }
}