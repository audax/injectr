package net.daxbau.injectr.inject

import android.app.AlertDialog
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
import androidx.annotation.VisibleForTesting
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import net.daxbau.injectr.R
import net.daxbau.injectr.common.JustLog
import net.daxbau.injectr.common.observe
import net.daxbau.injectr.databinding.BottomSheetInjectBinding
import net.daxbau.injectr.databinding.FragmentInjectBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import splitties.alertdialog.alertDialog
import splitties.alertdialog.cancelButton
import splitties.alertdialog.okButton
import splitties.toast.toast
import splitties.views.onClick


class InjectFragment : Fragment(), JustLog {

    private val vm: InjectViewModel by viewModel()
    private val photoManager: PhotoManager by inject()

    private var _binding: FragmentInjectBinding? = null
    private val binding: FragmentInjectBinding get() = _binding!!

    private var _bottomSheetBinding: BottomSheetInjectBinding? = null
    private val bottomSheetBinding: BottomSheetInjectBinding get() = _bottomSheetBinding!!

    private val scope = MainScope()

    @VisibleForTesting
    internal lateinit var bottomSheetBehavior: BottomSheetBehavior<CoordinatorLayout>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vm.setNavController(findNavController())
        _binding = FragmentInjectBinding.inflate(inflater, container, false)
        _bottomSheetBinding = binding.bottomSheetInclude
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        photoManager.stop()
    }

    override fun onResume() {
        super.onResume()
        photoManager.start()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            val bottomSheet = BottomSheetBehavior.from(bottomSheetBinding.root)
            bottomSheetBehavior = bottomSheet
            bottomSheetBinding.bottomSheetInject.setOnClickListener {
                bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
            }
            photoManager.bindView(bottomSheetBinding.cameraView)
            bottomSheetBinding.takePhotoButton.setOnClickListener {
                photoManager.takePhoto()
                GlobalScope.launch {
                    info("setting image view")
                    try {
                        val (bitmap, rotation) = photoManager.toBitmap()
                        scope.launch {
                            injectionPhoto.setImageBitmap(bitmap)
                            injectionPhoto.rotation = -rotation
                            bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
                        }
                    } catch (e: NoPhotoAvailableError) {
                        this@InjectFragment.activity?.runOnUiThread {
                            toast(getString(R.string.injection_photo_error))
                        }
                    }
                }
            }
            depthSeekBar.progress = vm.depth
            injectionSliderLabel.text = getString(R.string.injection_depth_label, vm.depth)
            depthSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    vm.depth = progress
                    injectionSliderLabel.text =
                        getString(R.string.injection_depth_label, progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {}
            })

            saveInjectionButton.setOnClickListener {
                scope.launch {
                    vm.save()
                }
            }
            injectionPositionLimb.minValue = 0
            injectionPositionLimb.maxValue = 7
            injectionPositionLimb.value = vm.limb
            injectionPositionLimb.setOnValueChangedListener { _, _, newVal -> vm.limb = newVal }
            injectionPositionNr.minValue = 0
            injectionPositionNr.maxValue = 24
            injectionPositionNr.setFormatter {
                ('A' + it).toString()
            }

            numberPickerFormatWorkaround()

            injectionPositionNr.value = vm.position
            injectionPositionNr.setOnValueChangedListener { _, _, newVal -> vm.position = newVal }
            injectionComment.setText(vm.comment)
            injectionComment.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    vm.comment = s.toString()
                }
            })

            bottomSheetBinding.switchCameraButton.onClick {
                vm.switchCamera()
            }

            bottomSheetBinding.toggleFlashButton.onClick {
                vm.toggleTorch()
            }

        }

        var alertDialog: AlertDialog? = null
        observe(vm.confirmationRequired) {
            if (it == true) {
                alertDialog = context?.alertDialog(getString(R.string.injection_confirm)) {
                    okButton {
                        scope.launch {
                            vm.confirmSave()
                        }
                    }
                    cancelButton { }
                }
                alertDialog?.show()
            } else {
                alertDialog?.cancel()
            }
        }
    }

    private fun numberPickerFormatWorkaround() {
        // see https://issuetracker.google.com/issues/36952035
        val f = NumberPicker::class.java.getDeclaredField("mInputText")
        f.isAccessible = true
        val inputText = f.get(binding.injectionPositionNr) as EditText
        inputText.filters = arrayOfNulls<InputFilter>(0)
    }

    override fun onDestroy() {
        super.onDestroy()
        vm.onDestroy()
    }

    companion object {
        const val TAG = "Inject"
    }
}
