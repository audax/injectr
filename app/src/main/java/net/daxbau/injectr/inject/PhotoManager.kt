package net.daxbau.injectr.inject

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Size
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.OnImageSavedCallback
import androidx.camera.core.ImageCapture.OutputFileOptions
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.daxbau.injectr.common.JustLog
import java.io.File
import java.util.*

interface PhotoManager {
    fun bindView(view: PreviewView, lifecycleOwner: LifecycleOwner)
    fun start()
    fun stop()
    fun takePhoto()
    fun switchCamera()
    fun toggleTorch()
    suspend fun asDrawable(): Drawable?
    suspend fun save(): String
}

class FotoapparatPhotoManager (private val context: Context): PhotoManager, JustLog {
    private var output: CompletableDeferred<String>? = null
    private var cameraControl: CameraControl? = null
    private var frontLensSelected = false
    private var torchEnabled = false
    private var imageCapture: ImageCapture? = null

    private var view: PreviewView? = null
    private var lifecycleOwner: LifecycleOwner? = null


    private val cameraProviderFuture by lazy { ProcessCameraProvider.getInstance(context) }


    override fun bindView(view: PreviewView, lifecycleOwner: LifecycleOwner) {
        this.view = view
        this.lifecycleOwner = lifecycleOwner
        val maxSize = Size(1200, 1200)
        imageCapture = ImageCapture.Builder()
            .setResolutionSelector(ResolutionSelector.Builder().setResolutionStrategy(
                ResolutionStrategy(maxSize, ResolutionStrategy.FALLBACK_RULE_CLOSEST_HIGHER_THEN_LOWER)
                ).build())
            .build()
        cameraProviderFuture.addListener({
           val cameraProvider = cameraProviderFuture.get()

           val preview = Preview.Builder().build().also {
               it.surfaceProvider = view.surfaceProvider
           }

            val cameraSelector = if (frontLensSelected)
                CameraSelector.DEFAULT_FRONT_CAMERA else CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                val camera = cameraProvider.bindToLifecycle(
                    lifecycleOwner, cameraSelector, preview, imageCapture
                )
                cameraControl = camera.cameraControl

            } catch (e: Exception) {
                error("Use case binding failed", e)
            }
        }, context.mainExecutor)

    }

    override fun start() {
    }

    override fun stop() {
    }

    override fun switchCamera() {
        frontLensSelected = !frontLensSelected
        val v = view ?: return
        val l = lifecycleOwner ?: return
        bindView(v, l)
    }

    override fun toggleTorch() {
        torchEnabled = !torchEnabled
        cameraControl?.enableTorch(torchEnabled)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun deleteUnsavedPhoto() {
        val fileName = try {
            output?.getCompleted()
        } catch (e: IllegalStateException) {
            // ignore
            return
        } ?: return
        val file = File(context.filesDir, fileName)
        file.delete()
    }


    // make suspend and directly return bitmap
    override fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        deleteUnsavedPhoto()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        val randomId = UUID.randomUUID().toString()
        val name = "injection_${randomId}_.jpg"
        val outputFile = File(context.filesDir, name)
        val deferred = CompletableDeferred<String>()
        val outputFileOptions = OutputFileOptions.Builder(outputFile).build()
        output = deferred
        imageCapture.takePicture(
            outputFileOptions,
            context.mainExecutor,
            object : OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    error("Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val uri = outputFileResults.savedUri
                    if (uri == null) {
                        error("Photo save did not produce uri")
                    } else {
                        deferred.complete(name)
                    }
                }

            }
        )
    }


    override suspend fun asDrawable(): Drawable? {
        val photoDeferred = output ?: return null
        val photo = photoDeferred.await()
        return Drawable.createFromPath(File(context.filesDir, photo).path)
    }

    override suspend fun save(): String {
        val fileName = output?.await() ?: throw NoPhotoAvailableError()
        output = null
        return fileName
    }

}

class NoPhotoAvailableError: IllegalStateException()