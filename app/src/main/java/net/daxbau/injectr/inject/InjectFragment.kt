package net.daxbau.injectr.inject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_inject.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.daxbau.injectr.R
import net.daxbau.injectr.common.JustLog
import org.jetbrains.anko.support.v4.toast
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class InjectFragment : Fragment(), JustLog {

    private val vm: InjectViewModel by viewModel()
    private val photoManager: PhotoManager by inject()

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
        photoManager.start()
    }

    override fun onStop() {
        super.onStop()
        photoManager.stop()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        photoManager.bindView(camera_view)
        takePhotoButton.setOnClickListener {
            photoManager.takePhoto()
            GlobalScope.launch {
                info("setting image view")
                try {
                    val (bitmap, rotation) = photoManager.toBitmap()
                    launch(Dispatchers.Main) {
                        injection_photo.setImageBitmap(bitmap)
                        injection_photo.rotation = -rotation
                    }
                } catch (e: NoPhotoAvailableError) {
                    toast(R.string.injection_photo_error)
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
            GlobalScope.launch {
                vm.save()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        vm.onDestroy()
    }
}
