package com.jsborbon.reparalo

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DashboardScreenUITest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun dashboardScreen_displaysMainSections() {
        composeTestRule.onNodeWithText("Bienvenido").assertIsDisplayed()
        composeTestRule.onNodeWithText("Categorías").assertIsDisplayed()
        composeTestRule.onNodeWithText("Tutoriales Destacados").assertIsDisplayed()
        composeTestRule.onNodeWithText("Técnicos Destacados").assertIsDisplayed()
    }
}
