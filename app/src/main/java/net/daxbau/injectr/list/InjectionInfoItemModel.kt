package net.daxbau.injectr.list

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import net.daxbau.injectr.R
import net.daxbau.injectr.common.KotlinEpoxyHolder
import java.util.*

@EpoxyModelClass(layout = R.layout.injection_info_item)
abstract class InjectionInfoItemModel : EpoxyModelWithHolder<InjectionInfoItemModel.Holder>() {

    @EpoxyAttribute var depth: Int = 0
    @EpoxyAttribute lateinit var date: Date

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.dateView.text = date.toLocaleString()
        holder.depthView.text = depth.toString()
    }

    class Holder : KotlinEpoxyHolder() {
        val depthView by bind<TextView>(R.id.depth_item)
        val dateView by bind<TextView>(R.id.date_item)
    }
}