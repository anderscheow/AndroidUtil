package io.github.anderscheow.androidutil.viewModel.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

class ToastMessage : SingleLiveEvent<String>() {

    fun observe(owner: LifecycleOwner, observer: ToastObserver) {
        super.observe(owner, Observer { s ->
            if (s == null) {
                return@Observer
            }
            observer.onNewMessage(s)
        })
    }

    interface ToastObserver {
        /**
         * Called when there is a new message to be shown.
         *
         * @param message The new message, non-null.
         */
        fun onNewMessage(message: String?)
    }
}
