package com.jsborbon.reparalo

import com.jsborbon.reparalo.models.Author
import com.jsborbon.reparalo.models.Comment
import junit.framework.TestCase.assertTrue
import org.junit.Test
import java.util.Date
import kotlin.test.assertEquals

class CommentModelTest {

    @Test
    fun comment_isValid() {
        val comment = Comment(
            id = "c1",
            tutorialId = "t1",
            author = Author(
                uid = "u1",
                name = "Ana Gómez",
            ),
            content = "Muy útil y fácil de seguir.",
            date = Date(),
            rating = 5,
        )

        assertEquals("t1", comment.tutorialId)
        assertEquals("Ana Gómez", comment.author.name)
        assertTrue(comment.rating in 1..5)
        assertTrue(comment.content.isNotBlank())
    }
}
