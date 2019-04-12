package net.daxbau.injectr.inject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_inject.*
import net.daxbau.injectr.R
import net.daxbau.injectr.common.JustLog
import org.koin.android.viewmodel.ext.android.viewModel

class InjectFragment : Fragment(), JustLog {

    private val vm: InjectViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vm.setNavController(findNavController())
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inject, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
