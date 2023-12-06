package com.example.orderingapp.commons.permissions

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.orderingapp.commons.permissions.PermissionsUtil.STORAGE_REQUEST_CODE
import com.example.orderingapp.commons.permissions.PermissionsUtil.checkPermissionsEnabled
import com.example.orderingapp.commons.permissions.PermissionsUtil.checkPermissionsRationale
import com.example.orderingapp.commons.permissions.PermissionsUtil.requestStoragePermission
import com.example.orderingapp.commons.permissions.PermissionsUtil.storagePermissionNameList
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class PermissionsUtilTest {
    private lateinit var activity: Activity

    @Before
    fun setUp() {
        activity = mockk(relaxed = true)
        mockkStatic(ContextCompat::class)
        mockkStatic(ActivityCompat::class)
    }

    @Test
    fun checkPermissionsEnabled() {
        every {
            ContextCompat.checkSelfPermission(
                activity,
                any()
            )
        } returns PackageManager.PERMISSION_GRANTED

        assertThat(activity.checkPermissionsEnabled()).isTrue()
    }

    @Test
    fun checkPermissionsNotEnabled() {
        every {
            ContextCompat.checkSelfPermission(
                activity,
                any()
            )
        } returns PackageManager.PERMISSION_DENIED

        assertThat(activity.checkPermissionsEnabled()).isFalse()
    }

    @Test
    fun checkPermissionsRationale() {
        every { ActivityCompat.shouldShowRequestPermissionRationale(activity, any()) } returns true

        assertThat(activity.checkPermissionsRationale()).isTrue()
    }

    @Test
    fun checkPermissionsNotRationale() {
        every { ActivityCompat.shouldShowRequestPermissionRationale(activity, any()) } returns false

        assertThat(activity.checkPermissionsRationale()).isFalse()
    }

    @Test
    fun requestStoragePermission() {
        activity.requestStoragePermission()

        verify {
            activity.requestPermissions(
                storagePermissionNameList.toTypedArray(),
                STORAGE_REQUEST_CODE
            )
        }
    }
}