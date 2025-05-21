package com.jsborbon.reparalo

import com.jsborbon.reparalo.models.ServiceHistoryItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun serviceHistoryItem_isCreatedCorrectly() {
        val timestamp = System.currentTimeMillis()
        val item = ServiceHistoryItem(
            title = "Cambio de grifo",
            description = "Se cambió el grifo de la cocina por fuga",
            date = timestamp,
        )

        assertEquals("Cambio de grifo", item.title)
        assertEquals("Se cambió el grifo de la cocina por fuga", item.description)
        assertEquals(timestamp, item.date)
    }

    @Test
    fun serviceHistoryItem_hasValidDate() {
        val currentTime = System.currentTimeMillis()
        val item = ServiceHistoryItem("Prueba", "Descripción", currentTime)
        // Check if the date is not in the future
        assertTrue(item.date <= currentTime)
    }
}
