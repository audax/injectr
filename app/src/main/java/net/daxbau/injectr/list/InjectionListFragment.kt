package net.daxbau.injectr.list

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import net.daxbau.injectr.common.JustLog
import net.daxbau.injectr.common.observe
import net.daxbau.injectr.data.InjectionInfo
import net.daxbau.injectr.databinding.InjectionListFragmentBinding
import org.koin.android.viewmodel.ext.android.viewModel


class InjectionListFragment : Fragment() {

    private val vm: InjectionListViewModel by viewModel()

    private var _binding: InjectionListFragmentBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vm.setNavController(findNavController())
        _binding = InjectionListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.injectFab.setOnClickListener {
            vm.addInjection()
        }
        val injectionInfoListController = InjectionInfoListController(
            requireContext().filesDir.absolutePath + '/'
        )
        binding.injectionListRecyclerView.setController(injectionInfoListController)
        observe(vm.injectionList) {
            injectionInfoListController.submitList(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        vm.onDestroy()
    }

    class InjectionInfoListController(private val imageDir: String) : PagedListEpoxyController<InjectionInfo>(),
        JustLog {

        override fun buildItemModel(currentPosition: Int, item: InjectionInfo?): EpoxyModel<*> {
            return item!!.let {
                val drawable = if (it.photoFileName != null) {
                    val path = imageDir + it.photoFileName
                    info("photo path $path")
                    Drawable.createFromPath(path)
                } else {
                    info("no photo")
                    null
                }
                InjectionInfoItemModel_().apply {
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
