package com.example.orderingapp.commons.extensions

import android.app.Activity
import com.firebase.ui.auth.AuthUI

fun Activity.finishSession() {
    AuthUI.getInstance().signOut(this)
    finish()
}