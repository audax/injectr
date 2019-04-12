package net.daxbau.injectr.inject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import net.daxbau.injectr.R
import org.koin.android.viewmodel.ext.android.viewModel

class Inject : Fragment() {

    private val vm: InjectViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inject, container, false)
    }

}
