package net.daxbau.injectr.inject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.fotoapparat.Fotoapparat
import io.fotoapparat.log.logcat
import io.fotoapparat.log.loggers
import io.fotoapparat.parameter.ScaleType
import kotlinx.android.synthetic.main.fragment_inject.*
import net.daxbau.injectr.R
import net.daxbau.injectr.common.JustLog
import org.koin.android.viewmodel.ext.android.viewModel

class InjectFragment : Fragment(), JustLog {

    private val vm: InjectViewModel by viewModel()
    private lateinit var fotoapparat: Fotoapparat

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vm.setNavController(findNavController())
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inject, container, false)
    }

    override fun onStart() {
        super.onStart()
        fotoapparat.start()
    }

    override fun onStop() {
        super.onStop()
        fotoapparat.stop()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fotoapparat = Fotoapparat(
            context = this.requireContext(),
            view = camera_view,                   // view which will draw the camera preview
            scaleType = ScaleType.CenterInside,    // (optional) we want the preview to fill the view
            logger = loggers(                    // (optional) we want to log camera events in 2 places at once
                logcat()                // ... in logcat
            )
        )
        takePhotoButton.setOnClickListener {
            val pic = fotoapparat.takePicture()
            vm.photo = pic
            pic.toBitmap()
                .whenAvailable {
                    info("bitmap available")
                    it?.let { bitmapPhoto ->
                        info("setting image view")
                        injection_photo.setImageBitmap(bitmapPhoto.bitmap)
                        injection_photo.rotation = -bitmapPhoto.rotationDegrees.toFloat()
                    }
                }
        }
        depthSeekBar.progress = vm.depth
        depthSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                vm.depth = progress
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) { }
            override fun onStopTrackingTouch(seekBar: SeekBar) { }
        })

        saveInjectionButton.setOnClickListener {
            vm.save()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        vm.onDestroy()
    }
}
