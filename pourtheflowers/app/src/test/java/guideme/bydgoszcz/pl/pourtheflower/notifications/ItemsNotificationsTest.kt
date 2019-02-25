package guideme.bydgoszcz.pl.pourtheflower.notifications

import guideme.bydgoszcz.pl.pourtheflower.model.Item
import guideme.bydgoszcz.pl.pourtheflower.model.Notification
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.utils.ContentProvider
import guideme.bydgoszcz.pl.pourtheflower.utils.NotificationTime
import guideme.bydgoszcz.pl.pourtheflower.utils.SystemTime
import guideme.bydgoszcz.pl.pourtheflower.utils.TimeHelper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class ItemsNotificationsTest {
    @Before
    fun before() {
        SystemTime.timeProvider = object : SystemTime.TimeProvider {
            override fun current(): Long {
                return TimeHelper.millisInDay * 10L
            }
        }
    }

    @Test
    fun `test 2 days notification and 1 day passed`() {
        checkIt(repeatDays = 2, lastDayNotification = 1)
    }

    @Test
    fun `test 2 days notification and 0 day passed`() {
        checkIt(repeatDays = 2, lastDayNotification = 0)
    }

    @Test
    fun `test 3 days notification and 1 day passed`() {
        checkIt(repeatDays = 3, lastDayNotification = 1)
    }

    @Test
    fun `test 7 days notification and 5 day passed`() {
        checkIt(repeatDays = 7, lastDayNotification = 5)
    }

    private fun checkIt(repeatDays: Int, lastDayNotification: Int) {
        val delayDays = repeatDays - lastDayNotification
        checkIt(repeatInTime = NotificationTime.fromDays(repeatDays),
                lastNotificationTime = SystemTime.current().minus(NotificationTime.fromDays(lastDayNotification)),
                expectedDelay = NotificationTime.fromDays(delayDays).toMillis(),
                expectedRepeat = NotificationTime.fromDays(repeatDays).toMillis())
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
            notificationScheduler.scheduleJob(any(), any(), any(), expectedDelay, expectedRepeat)
        }
    }
}