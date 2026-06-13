package com.example.kellera.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GlobalStateManagerTest {

    @Before
    fun setup() {
        GlobalStateManager.clearLastAnnouncement()
    }

    @Test
    fun testUpdateUserName() {
        GlobalStateManager.updateUserName("TestUser")
        assertEquals("TestUser", GlobalStateManager.userName)
    }

    @Test
    fun testShouldAnnouncePreventsRepeatedMessages() {
        val msg = "Hello"
        assertTrue(GlobalStateManager.shouldAnnounce(msg))
        assertFalse(GlobalStateManager.shouldAnnounce(msg))

        assertTrue(GlobalStateManager.shouldAnnounce("World"))
        assertTrue(GlobalStateManager.shouldAnnounce(msg))
    }

    @Test
    fun testUpdateCurrentApp() {
        GlobalStateManager.updateCurrentApp("com.test.app")
        assertEquals("com.test.app", GlobalStateManager.currentApp)
    }
}
