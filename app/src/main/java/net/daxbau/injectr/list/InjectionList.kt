package net.daxbau.injectr.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import net.daxbau.injectr.R
import org.koin.android.viewmodel.ext.android.viewModel

class InjectionList : Fragment() {

    private val vm: InjectionListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.injection_list_fragment, container, false)
    }

}
