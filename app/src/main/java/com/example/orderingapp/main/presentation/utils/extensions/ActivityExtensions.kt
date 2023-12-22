package com.example.orderingapp.main.presentation.utils.extensions

import android.app.Activity
import androidx.navigation.NavHostController
import com.firebase.ui.auth.AuthUI

fun Activity.finishSession() {
    AuthUI.getInstance().signOut(this)
    finish()
}

fun Activity.handleBackEvent(
    navController: NavHostController,
    backEventCallback: () -> Unit
) {
    if (!navController.popBackStack()) {
        finishSession()
        return
    }
    backEventCallback()
}