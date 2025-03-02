package net.daxbau.injectr.inject

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CompletableDeferred
import java.io.File
import java.util.*

interface PhotoManager {
    fun bindView(view: PreviewView, lifecycleOwner: LifecycleOwner)
    fun start()
    fun stop()
    fun takePhoto()
    fun switchCamera()
    fun toggleTorch()
    suspend fun toBitmap(): Pair<Bitmap, Float>
    suspend fun save(): String
}

class FotoapparatPhotoManager (private val context: Context): PhotoManager {
    private var output: CompletableDeferred<ImageProxy>? = null
    private var cameraControl: CameraControl? = null
    private var frontLensSelected = false
    private var torchEnabled = false
    private var imageCapture: ImageCapture? = null

    private var view: PreviewView? = null
    private var lifecycleOwner: LifecycleOwner? = null


    private val cameraProviderFuture = ProcessCameraProvider.getInstance(context)


    override fun bindView(view: PreviewView, lifecycleOwner: LifecycleOwner) {
        this.view = view
        this.lifecycleOwner = lifecycleOwner
        imageCapture = ImageCapture.Builder().build()
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
        val v = view ?: return
        val l = lifecycleOwner ?: return
        bindView(v, l)
    }

    override fun toggleTorch() {
        torchEnabled = !torchEnabled
        cameraControl?.enableTorch(torchEnabled)
    }


    // make suspend and directly return bitmap
    override fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Set up image capture listener, which is triggered after photo has
        // been taken
        val deferred = CompletableDeferred<ImageProxy>()
        output = deferred
        imageCapture.takePicture(
            context.mainExecutor,
            object : OnImageCapturedCallback() {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)
                    deferred.complete(image)
                }

            }
        )
    }


    override suspend fun toBitmap(): Pair<Bitmap, Float> {
        val photoDeferred = output ?: throw NoPhotoAvailableError()
        val photo = photoDeferred.await()
        return Pair(photo.toBitmap(), photo.imageInfo.rotationDegrees.toFloat())
    }

    override suspend fun save(): String {
        val photoDeferred = output ?: throw NoPhotoAvailableError()
        val photo = photoDeferred.await()
        val randomId = UUID.randomUUID().toString()
        val name = "injection_${randomId}_.jpg"
        val file = File(context.filesDir, name)
        photo.toBitmap().compress(Bitmap.CompressFormat.PNG, 95, file.outputStream())
        return name
    }

    companion object {
        private const val TAG = "PhotoManager"
    }

}

class NoPhotoAvailableError: IllegalStateException()