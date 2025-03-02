package net.daxbau.injectr.common

import android.graphics.Bitmap
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import net.daxbau.injectr.inject.NoPhotoAvailableError
import net.daxbau.injectr.inject.PhotoManager

open class StubPhotoManager : PhotoManager {

    var fileName = "stub_file"
    var rotation = 1f
    var bitmap: Bitmap? = null
    var frontCameraSelected = true
    var torchEnabled = false

    override fun bindView(view: PreviewView, lifecycleOwner: LifecycleOwner) { }

    override fun start() { }

    override fun stop() { }

    override fun takePhoto() { }

    override suspend fun toBitmap(): Pair<Bitmap, Float> {
        return bitmap?.let {
            return Pair(it, rotation)
        } ?: throw NoPhotoAvailableError()
    }

    override suspend fun save(): String {
        if (bitmap == null) {
            throw NoPhotoAvailableError()
        }
        return fileName
    }

    override fun switchCamera() {
        frontCameraSelected = !frontCameraSelected
    }

    override fun toggleTorch() {
        torchEnabled = !torchEnabled
    }
}