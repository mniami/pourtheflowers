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
import org.junit.After
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

    @After
    fun after() {
        SystemTime.timeProvider = SystemTime.DefaultTimeProvider()
    }

    @Test
    fun `test 2 days notification when 1 day passed`() {
        checkIt(repeatDays = 2, lastDayNotification = 1, expectedDelayDays = 1)
    }

    @Test
    fun `test 2 days notification when 0 day passed`() {
        checkIt(repeatDays = 2, lastDayNotification = 0, expectedDelayDays = 2)
    }

    @Test
    fun `test 3 days notification when 1 day passed`() {
        checkIt(repeatDays = 3, lastDayNotification = 1, expectedDelayDays = 2)
    }

    @Test
    fun `test 3 days notification when 4 day passed`() {
        checkIt(repeatDays = 3, lastDayNotification = 4, expectedDelayDays = 2)
    }

    @Test
    fun `test 3 days notification when 8 day passed`() {
        checkIt(repeatDays = 3, lastDayNotification = 8, expectedDelayDays = 1)
    }

    private fun checkIt(repeatDays: Int, lastDayNotification: Int, expectedDelayDays: Int) {
        checkIt(repeatInTime = NotificationTime.fromDays(repeatDays),
                lastNotificationTime = SystemTime.current().minus(NotificationTime.fromDays(lastDayNotification)),
                expectedDelay = NotificationTime.fromDays(expectedDelayDays).toMillis(),
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