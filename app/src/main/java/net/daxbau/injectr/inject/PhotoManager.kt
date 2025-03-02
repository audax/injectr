package net.daxbau.injectr.inject

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
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
import java.util.concurrent.Executors

interface PhotoManager {
    fun bindView(view: PreviewView, lifecyleOwner: LifecycleOwner)
    fun start()
    fun stop()
    fun takePhoto()
    fun switchCamera()
    fun toggleTorch()
    suspend fun toBitmap(): Pair<Bitmap, Float>
    suspend fun save(): String
}

class FotoapparatPhotoManager (private val context: Context): PhotoManager {
    private var output: ImageCapture.OutputFileResults? = null
    private var frontLensSelected = false
    private var torchEnabled = false
    private var imageCapture: ImageCapture? = null
    private val cameraExecutor = Executors.newSingleThreadExecutor()


    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)


    private var cameraConfiguration = CameraConfiguration(
        pictureResolution = { this.sortedBy { it.area }.find { it.width > 800 } },
        previewResolution = { this.sortedBy { it.area }.find { it.width > 800 } }
    )

    override fun bindView(view: PreviewView, lifecyleOwner: LifecycleOwner) {
        imageCapture = ImageCapture.Builder().build()
        cameraProviderFuture.addListener({
           val cameraProvider = cameraProviderFuture.get()

           val preview = Preview.Builder().build().also {
               it.surfaceProvider = view.surfaceProvider
           }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                   lifecyleOwner, cameraSelector, preview, imageCapture
                )
            } catch (e: Exception) {
                Log.e("PhotoManager", "Use case binding failed", e)
            }
        }, context.mainExecutor)

    }

    override fun start() {
    }

    override fun stop() {
    }

    override fun switchCamera() {
        frontLensSelected = !frontLensSelected
    }

    override fun toggleTorch() {
        val flashMode = if (torchEnabled) off() else torch()
        torchEnabled = !torchEnabled
        cameraConfiguration = cameraConfiguration.copy(
            flashMode = flashMode
        )
    }


    override fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time stamped name and MediaStore entry.
        val name = "injection_" + SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(context.contentResolver,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                contentValues)
            .build()


        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            context.mainExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun
                        onImageSaved(output: ImageCapture.OutputFileResults){
                    val msg = "Photo capture succeeded: ${output.savedUri}"
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)
                    this@FotoapparatPhotoManager.output = output
                }
            }
        )
    }


    override suspend fun toBitmap(): Pair<Bitmap, Float> {
        return output?.let { photo ->
            val bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(
                context.contentResolver, photo.savedUri!!))

            return Pair(bitmap, 0f)
        } ?: throw NoPhotoAvailableError()
    }

    override suspend fun save(): String {
        return output?.savedUri?.toString() ?: throw NoPhotoAvailableError()
    }

    companion object {
        private const val TAG = "PhotoManager"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"

    }

}

class NoPhotoAvailableError: IllegalStateException()