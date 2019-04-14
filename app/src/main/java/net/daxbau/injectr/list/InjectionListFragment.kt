package net.daxbau.injectr.list

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
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
    private var handler: Handler? = null

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
        val handlerThread= HandlerThread("epoxy")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        this.handler = handler
        val injectionInfoListController = InjectionInfoListController(
            requireContext().filesDir.absolutePath + '/', handler)
        injectionListRecyclerView.setController(injectionInfoListController)
        observe(vm.injectionList) {
            injectionInfoListController.setData(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        vm.onDestroy()
        handler?.removeCallbacksAndMessages(null)
        handler?.looper?.quit()
    }

    class InjectionInfoListController (private val imageDir: String, handler: Handler)
        : TypedEpoxyController<List<InjectionInfo>>(handler, handler), JustLog {
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
