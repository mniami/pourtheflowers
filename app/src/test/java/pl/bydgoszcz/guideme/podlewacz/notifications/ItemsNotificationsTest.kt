package pl.bydgoszcz.guideme.podlewacz.notifications

import pl.bydgoszcz.guideme.podlewacz.model.Item
import pl.bydgoszcz.guideme.podlewacz.model.Notification
import pl.bydgoszcz.guideme.podlewacz.views.model.UiItem
import pl.bydgoszcz.guideme.podlewacz.utils.ContentProvider
import pl.bydgoszcz.guideme.podlewacz.utils.NotificationTime
import pl.bydgoszcz.guideme.podlewacz.utils.SystemTime
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import java.text.SimpleDateFormat

class ItemsNotificationsTest {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH")

    @Test
    fun `test in the middle of pour period, current time afternoon`() {
        checkIt(repeatInEveryHours = 24, lastNotificationInHoursAgo = 10, expectedDelayedInHours = 14, currentTime = "2018-02-01T18:00:00")
    }

    @Test
    fun `test beginning of pour period, current time afternoon`() {
        checkIt(repeatInEveryHours = 24, lastNotificationInHoursAgo = 0, expectedDelayedInHours = 14, currentTime = "2018-02-01T18:00:00")
    }

    @Test
    fun `test ending of pour period, current time morning`() {
        checkIt(repeatInEveryHours = 24, lastNotificationInHoursAgo = 8, expectedDelayedInHours = 2, currentTime = "2018-02-01T06:00:00")
    }

    @Test
    fun `test over two days ago, current time morning`() {
        checkIt(repeatInEveryHours = 48, lastNotificationInHoursAgo = 60, expectedDelayedInHours = 26, currentTime = "2018-02-01T06:00:00")
    }

    @Test
    fun `test over two days ago, current time afternoon`() {
        checkIt(repeatInEveryHours = 48, lastNotificationInHoursAgo = 60, expectedDelayedInHours = 37, currentTime = "2018-02-01T19:00:00")
    }

    private fun checkIt(repeatInEveryHours: Int, lastNotificationInHoursAgo: Int, expectedDelayedInHours: Int, currentTime: String) {
        SystemTime.timeProvider = object : SystemTime.TimeProvider {
            override fun current(): Long {
                return dateFormat.parse(currentTime).time
            }
        }
        checkIt(repeatInTime = NotificationTime.fromHours(repeatInEveryHours),
                lastNotificationTime = SystemTime.current().minus(NotificationTime.fromHours(lastNotificationInHoursAgo)),
                expectedDelay = NotificationTime.fromHours(expectedDelayedInHours).toMillis(),
                expectedRepeat = NotificationTime.fromHours(repeatInEveryHours).toMillis())
    }

    private fun checkIt(repeatInTime: NotificationTime,
                        lastNotificationTime: SystemTime,
                        expectedDelay: Long,
                        expectedRepeat: Long) {
        val item = Item(notification = Notification(enabled = true, repeatInTime = repeatInTime, lastNotificationTime = lastNotificationTime))
        val uiItem = UiItem(item, true, NotificationTime.ZERO, "Short description")
        val contentProvider = mockk<ContentProvider>()
        val notificationScheduler = mockk<NotificationScheduler>()

        every { contentProvider.getString(any()) } returns ""
        every { notificationScheduler.scheduleJob(any(), any(), any(), any(), any()) } returns Unit

        val itemsNotifications = ItemsNotifications(contentProvider, notificationScheduler)
        itemsNotifications.setUpNotification(uiItem)

        verify {
            notificationScheduler.scheduleJob(any(), any(), any(), eq(expectedDelay), expectedRepeat)
        }
    }
}