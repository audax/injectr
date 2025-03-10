package net.daxbau.injectr.list

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import net.daxbau.injectr.R
import net.daxbau.injectr.common.KotlinEpoxyHolder
import net.daxbau.injectr.data.InjectionInfo
import java.text.DateFormat
import java.util.*

@SuppressLint("NonConstantResourceId")
@EpoxyModelClass(layout = R.layout.injection_info_item)
abstract class InjectionInfoItemModel : EpoxyModelWithHolder<InjectionInfoItemModel.Holder>() {

    @EpoxyAttribute var depth: Int = 0
    @EpoxyAttribute var comment: String = ""
    @EpoxyAttribute var photo: Drawable? = null
    @EpoxyAttribute var position: String = ""
    @EpoxyAttribute lateinit var date: Date
    @EpoxyAttribute var injectionInfo: InjectionInfo? = null

    @CallbackProp var clickListener: View.OnClickListener? = null


    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.dateView.text = DateFormat.getInstance().format(date)
        holder.depthView.text = holder.depthView.context.getString(R.string.injection_depth_value, depth)
        holder.commentView.text = comment
        holder.positionView.text = position
        holder.imageView.setImageDrawable(photo)
        holder.imageView.setOnClickListener(clickListener)
    }

    class Holder : KotlinEpoxyHolder() {
        val depthView by bind<TextView>(R.id.depth_item)
        val dateView by bind<TextView>(R.id.date_item)
        val positionView by bind<TextView>(R.id.position_item)
        val commentView by bind<TextView>(R.id.comment_item)
        val imageView by bind<ImageView>(R.id.photo_item)
    }
}