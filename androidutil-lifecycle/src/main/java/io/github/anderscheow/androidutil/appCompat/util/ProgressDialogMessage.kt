package io.github.anderscheow.androidutil.appCompat.util

import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

class ProgressDialogMessage : SingleLiveEvent<Int>() {

    fun observe(owner: LifecycleOwner, observer: ProgressDialogObserver) {
        super.observe(owner, Observer { t ->
            if (t == null) {
                return@Observer
            }

            if (t == -1) {
                observer.dismiss()
            } else {
                observer.onNewMessage(t)
            }
        })
    }

    interface ProgressDialogObserver {
        /**
         * Called when there is a new message to be shown.
         *
         * @param message The new message, non-null.
         */
        fun onNewMessage(@StringRes message: Int)

        /**
         * Called when dismiss is required.
         */
        fun dismiss()
    }
}
