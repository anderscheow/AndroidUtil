package io.github.anderscheow.library.adapters.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import io.github.anderscheow.androidutil.databinding.ViewNetworkStateBinding
import io.github.anderscheow.androidutil.recyclerView.adapters.FoundationPagedListAdapter
import io.github.anderscheow.androidutil.recyclerView.util.SectionGroup
import io.github.anderscheow.androidutil.recyclerView.viewHolder.BaseViewHolder
import io.github.anderscheow.androidutil.recyclerView.viewHolder.NetworkStateViewHolder

abstract class BaseSectionPagedListAdapter<Key, Value>(
        private val callback: () -> Unit)
    : FoundationPagedListAdapter<SectionGroup>(SectionGroup.DIFF_CALLBACK, callback) {

    @get:LayoutRes
    protected abstract val headerLayout: Int

    protected abstract fun getHeaderViewHolder(binding: ViewDataBinding): RecyclerView.ViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            NETWORK_STATE_LAYOUT -> {
                val binding = DataBindingUtil.inflate<ViewNetworkStateBinding>(
                        layoutInflater, NETWORK_STATE_LAYOUT, parent, false)
                NetworkStateViewHolder.create(binding, callback)
            }

            headerLayout -> {
                val binding = DataBindingUtil.inflate<ViewDataBinding>(
                        layoutInflater, headerLayout, parent, false)
                getHeaderViewHolder(binding)
            }

            else -> {
                val binding = DataBindingUtil.inflate<ViewDataBinding>(
                        layoutInflater, viewType, parent, false)
                getBodyViewHolder(viewType, binding)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemViewType = getItemViewType(position)

        when (itemViewType) {
            NETWORK_STATE_LAYOUT -> (holder as NetworkStateViewHolder).bind(networkState)

            headerLayout -> {
                if (holder !is BaseViewHolder<*>) {
                    throw IllegalStateException("Must inherit MyBaseViewHolder for body view holder")
                }
                (holder as BaseViewHolder<Key>).bind(getItem(position)?.section as Key)
            }

            else -> {
                if (holder !is BaseViewHolder<*>) {
                    throw IllegalStateException("Must inherit MyBaseViewHolder for body view holder")
                }
                (holder as BaseViewHolder<Value>).bind(getItem(position)?.row as Value)
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_STATE_LAYOUT
        } else {
            val item = getItem(position)
            if (item != null && item.isRow) {
                getBodyLayout(position)
            } else {
                headerLayout
            }
        }
    }
}
