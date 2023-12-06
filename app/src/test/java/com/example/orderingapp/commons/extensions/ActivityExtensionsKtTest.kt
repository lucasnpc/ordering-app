package com.example.orderingapp.commons.extensions

import android.app.Activity
import com.firebase.ui.auth.AuthUI
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class ActivityExtensionsKtTest {

    private lateinit var activity: Activity
    private lateinit var authUI: AuthUI

    @Before
    fun setUp() {
        activity = mockk(relaxed = true)
        authUI = mockk(relaxed = true)
        mockkStatic(AuthUI::class)
        every { AuthUI.getInstance() } returns authUI
    }

    @Test
    fun finishSession() {
        activity.finishSession()

        verify {
            authUI.signOut(activity)
            activity.finish()
        }
    }
}