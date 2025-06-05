package com.jsborbon.reparalo

import com.jsborbon.reparalo.models.Author
import com.jsborbon.reparalo.models.Material
import com.jsborbon.reparalo.models.Tutorial
import junit.framework.TestCase.assertTrue
import org.junit.Test
import java.util.Date
import kotlin.test.assertEquals

class TutorialModelTest {

    @Test
    fun tutorial_isCreatedCorrectly() {
        val materials = listOf(
            Material("1", "Destornillador", 1, "Herramienta básica", 5.0f),
        )
        val tutorial = Tutorial(
            id = "t1",
            title = "Cómo cambiar un enchufe",
            description = "Guía paso a paso para cambiar un enchufe",
            category = "Electricidad",
            difficultyLevel = "Media",
            estimatedDuration = "20 min",
            materials = materials,
            videoUrl = "https://video.example.com",
            author = Author(
                uid = "a1",
                name = "Juan Pérez",
            ),
            publicationDate = Date(),
            averageRating = 4.5f,
        )

        assertEquals("t1", tutorial.id)
        assertEquals("Electricidad", tutorial.category)
        assertTrue(tutorial.materials.isNotEmpty())
    }
}
