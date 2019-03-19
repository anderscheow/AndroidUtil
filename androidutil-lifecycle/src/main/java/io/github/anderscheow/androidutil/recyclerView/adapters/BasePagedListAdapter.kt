package io.github.anderscheow.androidutil.recyclerView.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.github.anderscheow.androidutil.databinding.ViewNetworkStateBinding
import io.github.anderscheow.androidutil.recyclerView.viewHolder.BaseViewHolder
import io.github.anderscheow.androidutil.recyclerView.viewHolder.NetworkStateViewHolder

abstract class BasePagedListAdapter<T>(
        diffCallback: DiffUtil.ItemCallback<T>,
        private val callback: () -> Unit)
    : FoundationPagedListAdapter<T>(diffCallback, callback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            NETWORK_STATE_LAYOUT -> {
                val binding = DataBindingUtil.inflate<ViewNetworkStateBinding>(
                        layoutInflater, NETWORK_STATE_LAYOUT, parent, false)
                NetworkStateViewHolder.create(binding, callback)
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

        if (itemViewType == getBodyLayout(position)) {
            if (holder !is BaseViewHolder<*>) {
                throw IllegalStateException("Must inherit MyBaseViewHolder for body view holder")
            }
            (holder as? BaseViewHolder<T>)?.bind(getItem(position))
        } else if (itemViewType == NETWORK_STATE_LAYOUT) {
            (holder as NetworkStateViewHolder).bind(networkState)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_STATE_LAYOUT
        } else {
            getBodyLayout(position)
        }
    }
}
