package net.daxbau.injectr.list

import android.graphics.drawable.Drawable
import android.widget.ImageView
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
    @EpoxyAttribute var comment: String = ""
    @EpoxyAttribute var photo: Drawable? = null
    @EpoxyAttribute var position: String = ""
    @EpoxyAttribute lateinit var date: Date

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.dateView.text = date.toLocaleString()
        holder.depthView.text = depth.toString()
        holder.commentView.text = comment
        holder.positionView.text = position
        holder.immageView.setImageDrawable(photo)
    }

    class Holder : KotlinEpoxyHolder() {
        val depthView by bind<TextView>(R.id.depth_item)
        val dateView by bind<TextView>(R.id.date_item)
        val positionView by bind<TextView>(R.id.position_item)
        val commentView by bind<TextView>(R.id.comment_item)
        val immageView by bind<ImageView>(R.id.photo_item)
    }
}