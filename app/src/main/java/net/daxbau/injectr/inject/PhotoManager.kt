package net.daxbau.injectr.inject

import android.content.Context
import android.graphics.Bitmap
import io.fotoapparat.Fotoapparat
import io.fotoapparat.configuration.CameraConfiguration
import io.fotoapparat.configuration.UpdateConfiguration
import io.fotoapparat.log.logcat
import io.fotoapparat.log.loggers
import io.fotoapparat.parameter.ScaleType
import io.fotoapparat.result.PhotoResult
import io.fotoapparat.result.adapter.rxjava2.toSingle
import io.fotoapparat.selector.back
import io.fotoapparat.selector.front
import io.fotoapparat.selector.off
import io.fotoapparat.selector.torch
import io.fotoapparat.view.CameraView
import kotlinx.coroutines.rx2.await
import java.io.File
import java.util.*

interface PhotoManager {
    fun bindView(view: CameraView)
    fun start()
    fun stop()
    fun takePhoto()
    fun switchCamera()
    fun toggleTorch()
    suspend fun toBitmap(): Pair<Bitmap, Float>
    suspend fun save(): String
}

class FotoapparatPhotoManager (private val context: Context): PhotoManager {
    private var fotoapparat: Fotoapparat? = null
    private var result: PhotoResult? = null
    private var frontLensSelected = false
    private var torchEnabled = false

    private var cameraConfiguration = CameraConfiguration(
        pictureResolution = { this.sortedBy { it.area }.find { it.width > 800 } },
        previewResolution = { this.sortedBy { it.area }.find { it.width > 800 } }
    )

    override fun bindView(view: CameraView) {
        result = null
        fotoapparat = Fotoapparat(
            context = context,
            view = view,
            scaleType = ScaleType.CenterInside,
            lensPosition = back(),
            cameraConfiguration = cameraConfiguration,
            logger = loggers(
                logcat()
            )
        )
    }

    override fun start() {
        fotoapparat?.start()
    }

    override fun stop() {
        fotoapparat?.stop()
    }

    override fun switchCamera() {
        fotoapparat?.switchTo(
            lensPosition = if (frontLensSelected) back() else front(),
            cameraConfiguration = cameraConfiguration
        )
        frontLensSelected = !frontLensSelected
    }

    override fun toggleTorch() {
        val flashMode = if (torchEnabled) off() else torch()
        fotoapparat?.updateConfiguration(
            UpdateConfiguration(
                flashMode = flashMode
            )
        )
        torchEnabled = !torchEnabled
        cameraConfiguration = cameraConfiguration.copy(
            flashMode = flashMode
        )
    }


    override fun takePhoto() {
        result = fotoapparat?.takePicture()
    }

    override suspend fun toBitmap(): Pair<Bitmap, Float> {
        return result?.let { photo ->
            val bitmap = photo.toBitmap().toSingle().await()
            return Pair(bitmap.bitmap, bitmap.rotationDegrees.toFloat())
        } ?: throw NoPhotoAvailableError()
    }

    override suspend fun save(): String {
        return result?.let { photo ->
            val randomId = UUID.randomUUID().toString()
            val name = "injection_${randomId}_.jpg"
            val file = File(context.filesDir, name)
            photo.saveToFile(file).toSingle().await()
            return name
        } ?: throw NoPhotoAvailableError()
    }
}

class NoPhotoAvailableError: IllegalStateException()