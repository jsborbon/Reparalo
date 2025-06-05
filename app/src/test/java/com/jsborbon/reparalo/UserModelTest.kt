package com.jsborbon.reparalo

import com.jsborbon.reparalo.models.User
import com.jsborbon.reparalo.models.UserType
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Date

class UserModelTest {

    @Test
    fun user_isCreatedCorrectly() {
        val user = User(
            uid = "12345",
            name = "Javier Borbón",
            email = "javier@example.com",
            phone = "601234567",
            userType = UserType.CLIENT,
            availability = "Lunes a viernes",
            registrationDate = Date(),
        )

        assertEquals("12345", user.uid)
        assertEquals("Javier Borbón", user.name)
        assertEquals(UserType.CLIENT, user.userType)
    }
}
