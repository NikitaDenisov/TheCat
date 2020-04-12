package com.denisov.cat.presentation.utils

import android.Manifest
import android.app.DownloadManager
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.denisov.cat.data.dto.Cat
import com.denisov.cat.di.scope.PerFragment
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@PerFragment
class ImageDownloadManager @Inject constructor(private val fragment: Fragment) {

    private var pair: Pair<Cat, Bitmap>? = null

    fun download(pair: Pair<Cat, Bitmap>) {
        this.pair = pair
        if (writeStoragePermissionAllowed()) {
            download()
        } else {
            requestPermission()
        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (
            requestCode == WRITE_EXTERNAL_STORAGE_REQUEST &&
            permissionHasGranted(grantResults)
        ) {
            download()
        } else {
            pair = null
        }
    }

    private fun writeStoragePermissionAllowed() =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
            ContextCompat.checkSelfPermission(
                fragment.requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED

    private fun requestPermission() {
        fragment.requestPermissions(
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            WRITE_EXTERNAL_STORAGE_REQUEST
        )
    }

    private fun permissionHasGranted(grantResults: IntArray) =
        grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED

    private fun download() {
        pair?.let { pair ->
            val newFile = File(DOWNLOADS_DIR, "${pair.first.id}.jpeg")
            if (newFile.exists()) {
                newFile.delete()
            }
            FileOutputStream(newFile).use {
                pair.second.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }
            Intent(DownloadManager.ACTION_VIEW_DOWNLOADS)
                .apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
                .let {
                    fragment.startActivity(it)
                }
            this.pair = null
        }
    }

    private companion object {
        private const val WRITE_EXTERNAL_STORAGE_REQUEST = 1
        private val DOWNLOADS_DIR =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    }
}