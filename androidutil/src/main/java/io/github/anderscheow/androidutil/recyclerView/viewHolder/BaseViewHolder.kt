package io.github.anderscheow.androidutil.recyclerView.viewHolder

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import io.github.anderscheow.androidutil.BR

abstract class BaseViewHolder<in T>(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    private var item: T? = null

    protected open fun extraBinding(item: T) {
    }

    protected open fun onClick(view: View, item: T?) {
    }

    init {
        init()
    }

    private fun init() {
        this.binding.root.setOnClickListener {
            onClick(binding.root, item)
        }
    }

    fun bind(item: T?) {
        item?.let {
            this.item = it

            binding.setVariable(BR.obj, it)
            binding.executePendingBindings()

            extraBinding(it)
        }
    }
}
