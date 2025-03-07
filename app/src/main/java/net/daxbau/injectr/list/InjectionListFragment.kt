package net.daxbau.injectr.list

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyTouchHelper
import com.airbnb.epoxy.EpoxyTouchHelper.SwipeCallbacks
import com.airbnb.epoxy.paging.PagedListEpoxyController
import net.daxbau.injectr.R
import net.daxbau.injectr.common.JustLog
import net.daxbau.injectr.common.observe
import net.daxbau.injectr.data.InjectionInfo
import net.daxbau.injectr.databinding.InjectionListFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.max


class InjectionListFragment : Fragment(), JustLog {

    private val vm: InjectionListViewModel by viewModel()

    private var _binding: InjectionListFragmentBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            backFromZoom()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
            requireContext().filesDir.absolutePath + '/',
            ::zoomPhoto
        )
        binding.injectionListRecyclerView.setController(injectionInfoListController)
        EpoxyTouchHelper
            .initSwiping(binding.injectionListRecyclerView)
            .right()
            .withTarget(InjectionInfoItemModel::class.java)
            .andCallbacks(object: SwipeCallbacks<InjectionInfoItemModel>() {
                override fun onSwipeCompleted(
                    model: InjectionInfoItemModel?,
                    itemView: View?,
                    position: Int,
                    direction: Int
                ) {
                    val injectionInfo = model?.injectionInfo ?: return
                    vm.deleteInjection(injectionInfo)
                }

                override fun onSwipeProgressChanged(
                    model: InjectionInfoItemModel?,
                    itemView: View?,
                    swipeProgress: Float,
                    canvas: Canvas?
                ) {
                    itemView?.findViewById<View>(R.id.swipe_to_delete)?.apply {
                        translationX = max(-itemView.translationX, -measuredWidth.toFloat())
                    }
                }

            })
        observe(vm.injectionList) {
            injectionInfoListController.submitList(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        vm.onDestroy()
    }

    fun zoomPhoto(drawable: Drawable) {
        binding.expandedImage.setImageDrawable(drawable)
        binding.expandedImage.visibility = VISIBLE
        info("Zoom photo callback")
    }

    private fun backFromZoom() {
        binding.expandedImage.visibility = VISIBLE
        binding.expandedImage.setImageDrawable(null)
    }

    class InjectionInfoListController(private val imageDir: String, private val zoomCallback: (Drawable) -> Unit) : PagedListEpoxyController<InjectionInfo>(),
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
                val handler: OnClickListener = object: OnClickListener {
                    override fun onClick(v: View?) {
                        drawable ?: return
                        zoomCallback(drawable)
                    }

                }
                InjectionInfoItemModel_().apply {
                    id(it.id)
                    depth(it.depth)
                    date(it.date)
                    position(it.position())
                    comment(it.comment)
                    photo(drawable)
                    injectionInfo(item)
                    clickListener = handler
                }
            }
        }

    }

}
