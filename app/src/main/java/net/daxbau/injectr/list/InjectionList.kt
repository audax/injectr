package net.daxbau.injectr.list

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.injection_list_fragment.*

import net.daxbau.injectr.R
import org.koin.android.viewmodel.ext.android.viewModel

class InjectionList : Fragment() {

    private val vm: InjectionListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vm.setNavController(findNavController())
        return inflater.inflate(R.layout.injection_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injectFab.setOnClickListener {
            vm.addInjection()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        vm.onDestroy()
    }

}
