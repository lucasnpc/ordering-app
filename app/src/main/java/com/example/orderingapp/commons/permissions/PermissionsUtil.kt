package com.example.orderingapp.commons.permissions

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionsUtil {
    private const val STORAGE_REQUEST_CODE = 123
    private val storagePermissionNameList = if (Build.VERSION.SDK_INT >= 33) {
        arrayListOf(
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_IMAGES
        )
    } else {
        arrayListOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
    }

    fun Activity.checkPermissionsEnabled(): Boolean {
        return storagePermissionNameList.any {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun Activity.checkPermissionsRationale(): Boolean {
        return storagePermissionNameList.any {
            ActivityCompat.shouldShowRequestPermissionRationale(this, it)
        }
    }

    fun Activity.requestStoragePermission() {
        requestPermissions(
            storagePermissionNameList.toTypedArray(), STORAGE_REQUEST_CODE
        )
    }
}