package net.daxbau.injectr.list

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.airbnb.epoxy.TypedEpoxyController
import kotlinx.android.synthetic.main.injection_list_fragment.*
import net.daxbau.injectr.R
import net.daxbau.injectr.common.JustLog
import net.daxbau.injectr.common.observe
import net.daxbau.injectr.data.InjectionInfo
import org.koin.android.viewmodel.ext.android.viewModel

class InjectionListFragment : Fragment() {

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
        val injectionInfoListController = InjectionInfoListController(requireContext().filesDir.absolutePath + '/')
        injectionListRecyclerView.setController(injectionInfoListController)
        observe(vm.injectionList) {
            injectionInfoListController.setData(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        vm.onDestroy()
    }

    class InjectionInfoListController (private val imageDir: String): TypedEpoxyController<List<InjectionInfo>>(), JustLog {
        override fun buildModels(data: List<InjectionInfo>) {
            data.forEach {
                val drawable = if (it.photoFileName != null) {
                    val path = imageDir + it.photoFileName
                    info("photo path $path")
                    Drawable.createFromPath(path)
                } else {
                    info("no photo")
                    null
                }
                injectionInfoItem {
                    id(it.id)
                    depth(it.depth)
                    date(it.date)
                    position(it.position())
                    comment(it.comment)
                    photo(drawable)
                }
            }
        }

    }

}
