package net.daxbau.injectr.inject

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_inject.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.daxbau.injectr.R
import net.daxbau.injectr.common.JustLog
import net.daxbau.injectr.common.observe
import org.jetbrains.anko.noButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.runOnUiThread
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.yesButton
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
                    runOnUiThread {
                        toast(R.string.injection_photo_error)
                    }
                }
            }
        }
        depthSeekBar.progress = vm.depth
        injection_slider_label.text = getString(R.string.injection_depth_label, vm.depth)
        depthSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                vm.depth = progress
                injection_slider_label.text = getString(net.daxbau.injectr.R.string.injection_depth_label, progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) { }
            override fun onStopTrackingTouch(seekBar: SeekBar) { }
        })

        saveInjectionButton.setOnClickListener {
            GlobalScope.launch {
                vm.save()
            }
        }
        injection_position_limb.minValue = 0
        injection_position_limb.maxValue = 7
        injection_position_limb.value = vm.limb
        injection_position_limb.setOnValueChangedListener { _, _, newVal -> vm.limb = newVal }
        injection_position_nr.minValue = 0
        injection_position_nr.maxValue = 24
        injection_position_nr.setFormatter {
            ('A' + it).toString()
        }

        numberPickerFormatWorkaround()

        injection_position_nr.value = vm.position
        injection_position_nr.setOnValueChangedListener { _, _, newVal -> vm.position = newVal }
        injection_comment.setText(vm.comment)
        injection_comment.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) { }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                vm.comment = s.toString()
            }
        })

        var alertDialog: DialogInterface? = null
        observe(vm.confirmationRequired) {
            if (it == true) {
                // dialog
                alertDialog = alert(R.string.injection_confirm) {
                    yesButton {
                        GlobalScope.launch {
                            vm.confirmSave()
                        }
                    }
                    noButton {  }
                }.show()
            } else {
                alertDialog?.cancel()
            }
        }
    }

    private fun numberPickerFormatWorkaround() {
        // see https://issuetracker.google.com/issues/36952035
        val f = NumberPicker::class.java.getDeclaredField("mInputText")
        f.isAccessible = true
        val inputText = f.get(injection_position_nr) as EditText
        inputText.filters = arrayOfNulls<InputFilter>(0)
    }

    override fun onDestroy() {
        super.onDestroy()
        vm.onDestroy()
    }
}
