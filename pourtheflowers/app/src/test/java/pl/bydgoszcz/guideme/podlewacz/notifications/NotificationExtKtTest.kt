package pl.bydgoszcz.guideme.podlewacz.notifications

import org.junit.Assert.assertEquals
import org.junit.Test
import pl.bydgoszcz.guideme.podlewacz.model.Notification
import pl.bydgoszcz.guideme.podlewacz.utils.NotificationTime
import pl.bydgoszcz.guideme.podlewacz.utils.SystemTime

class NotificationExtKtTest {

    @Test
    fun `one day`() {
        getElapsedTime(NotificationTime.fromDays(3), NotificationTime.fromDays(2), NotificationTime.fromDays(1))
    }

    @Test
    fun `minus one day`() {
        getElapsedTime(
                repeatTime = NotificationTime.fromDays(3),
                lastTimeMinusCurrentTime = NotificationTime.fromDays(5),
                expectedTime = NotificationTime.fromDays(-2))
    }

    @Test
    fun `two days`() {
        getElapsedTime(NotificationTime.fromDays(3), NotificationTime.fromDays(1), NotificationTime.fromDays(2))
    }

    private fun getElapsedTime(repeatTime: NotificationTime, lastTimeMinusCurrentTime: NotificationTime, expectedTime: NotificationTime) {
        val currentTime = SystemTime.current()

        cleanNotificationTimeToFixedTime(currentTime)

        val notification = Notification(true,
                repeatInTime = repeatTime,
                lastNotificationTime = currentTime.minus(lastTimeMinusCurrentTime))
        val actual = notification.getRemainingNotificationTime(currentTime)

        assertEquals(expectedTime, actual)
    }
}