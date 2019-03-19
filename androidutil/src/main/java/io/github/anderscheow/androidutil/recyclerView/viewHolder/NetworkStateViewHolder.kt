package io.github.anderscheow.androidutil.recyclerView.viewHolder

import android.view.View
import androidx.annotation.StringRes
import io.github.anderscheow.androidutil.constant.NetworkState
import io.github.anderscheow.androidutil.databinding.ViewNetworkStateBinding
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_network_state.*

class NetworkStateViewHolder(private val binding: ViewNetworkStateBinding,
                             private val callback: () -> Unit,
                             @StringRes
                             private val errorMessage: Int? = null) : BaseViewHolder<NetworkState>(binding), LayoutContainer {

    override val containerView: View?
        get() = binding.root

    override fun extraBinding(item: NetworkState) {
        button_retry.setOnClickListener {
            callback.invoke()
        }

        errorMessage?.let {
            text_view_error_msg.setText(errorMessage)
        }
    }

    companion object {

        fun create(binding: ViewNetworkStateBinding,
                   callback: () -> Unit): NetworkStateViewHolder {
            return NetworkStateViewHolder(binding, callback)
        }

        fun create(binding: ViewNetworkStateBinding,
                   callback: () -> Unit,
                   @StringRes errorMessage: Int?): NetworkStateViewHolder {
            return NetworkStateViewHolder(binding, callback, errorMessage)
        }
    }
}
