package guideme.bydgoszcz.pl.pourtheflower.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class NotificationTimeTest {

    @Test
    fun `to string, one day`() {
        val actual = NotificationTime.fromDays(1).toString()
        val expected = "1 0:0:0"
        assertEquals(expected, actual)
    }

    @Test
    fun `to string, one day, one hour, one minute, one second`() {
        val actual = NotificationTime.fromDays(1)
                .plus(NotificationTime.fromHours(1))
                .plus(NotificationTime.fromMinutes(1))
                .plus(NotificationTime.fromSeconds(1)).toString()
        val expected = "1 1:1:1"
        assertEquals(expected, actual)
    }
}