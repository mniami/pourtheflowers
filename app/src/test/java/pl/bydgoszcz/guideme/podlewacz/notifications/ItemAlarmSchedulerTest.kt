package pl.bydgoszcz.guideme.podlewacz.notifications

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import pl.bydgoszcz.guideme.podlewacz.utils.ContentProvider
import pl.bydgoszcz.guideme.podlewacz.utils.NotificationTime
import pl.bydgoszcz.guideme.podlewacz.utils.SystemTime
import pl.bydgoszcz.guideme.podlewacz.utils.TimeProvider
import java.text.SimpleDateFormat

class ItemAlarmSchedulerTest {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH")

    @Test
    fun `test in the middle of pour period, current time afternoon`() {
        checkIt(expectedDelayedInHours = 14, currentTime = "2018-02-01T18:00:00")
    }

    @Test
    fun `test beginning of pour period, current time afternoon`() {
        checkIt(expectedDelayedInHours = 14, currentTime = "2018-02-01T18:00:00")
    }

    @Test
    fun `test ending of pour period, current time morning`() {
        checkIt(expectedDelayedInHours = 2, currentTime = "2018-02-01T06:00:00")
    }

    private fun checkIt(expectedDelayedInHours: Int, currentTime: String) {
        SystemTime.timeProvider = object : TimeProvider {
            override fun current(): Long {
                return dateFormat.parse(currentTime).time
            }
        }
        checkIt(expectedDelay = NotificationTime.fromHours(expectedDelayedInHours).toMillis(),
                expectedRepeat = NotificationTime.fromHours(24).toMillis())
    }

    private fun checkIt(expectedDelay: Long,
                        expectedRepeat: Long) {
        val contentProvider = mockk<ContentProvider>()
        val notificationScheduler = mockk<AlarmScheduler>()

        every { contentProvider.getString(any()) } returns ""
        every { notificationScheduler.schedule(any(), any()) } returns Unit

        val itemsNotifications = ItemAlarmScheduler(notificationScheduler)
        itemsNotifications.schedule()

        verify {
            notificationScheduler.schedule(eq(expectedDelay), expectedRepeat)
        }
    }
}