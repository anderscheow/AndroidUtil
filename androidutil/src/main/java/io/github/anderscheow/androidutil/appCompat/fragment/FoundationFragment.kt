package io.github.anderscheow.androidutil.appCompat.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.kaopiz.kprogresshud.KProgressHUD
import com.orhanobut.logger.Logger
import io.github.anderscheow.androidutil.R
import io.github.anderscheow.androidutil.constant.EventBusType
import io.github.anderscheow.androidutil.kotlinExt.isConnectedToInternet
import io.github.anderscheow.androidutil.kotlinExt.isNotThere
import io.github.anderscheow.androidutil.kotlinExt.toast
import io.github.anderscheow.androidutil.kotlinExt.withActivity
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.okButton
import org.jetbrains.anko.yesButton

abstract class FoundationFragment : Fragment() {

    @LayoutRes
    abstract fun getResLayout(): Int

    open fun getEventBusType(): EventBusType? = null

    open fun initAfterOnAttach() {
    }

    open fun init() {
    }

    var isDestroy = false
        private set

    var progressDialog: KProgressHUD? = null
        private set

    override fun onAttach(context: Context?) {
        Logger.v("Fragment ATTACHED")
        super.onAttach(context)
        if (EventBusType.isOnAttach(getEventBusType())) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this)
            }
        }
        initAfterOnAttach()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Logger.v("Fragment CREATED")
        super.onCreate(savedInstanceState)
        if (EventBusType.isOnCreate(getEventBusType())) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            init()
        }
    }

    override fun onActivityCreated(savedInstaceState: Bundle?) {
        Logger.v("Fragment ACTIVITY CREATED")
        super.onActivityCreated(savedInstaceState)
    }

    override fun onStart() {
        Logger.v("Fragment STARTED")
        super.onStart()
        if (EventBusType.isOnStart(getEventBusType())) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this)
            }
        }
    }

    override fun onResume() {
        Logger.v("Fragment RESUMED")
        super.onResume()
        if (EventBusType.isOnResume(getEventBusType())) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this)
            }
        }
    }

    override fun onPause() {
        Logger.v("Fragment PAUSED")
        if (EventBusType.isOnResume(getEventBusType())) {
            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this)
            }
        }
        super.onPause()
    }

    override fun onStop() {
        Logger.v("Fragment STOPPED")
        if (EventBusType.isOnStart(getEventBusType())) {
            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this)
            }
        }
        super.onStop()
    }

    override fun onDestroy() {
        Logger.v("Fragment DESTROYED")
        isDestroy = true
        if (EventBusType.isOnCreate(getEventBusType())) {
            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this)
            }
        }
        super.onDestroy()
    }

    override fun onDestroyView() {
        Logger.v("Fragment VIEW DESTROYED")
        super.onDestroyView()
    }

    override fun onDetach() {
        Logger.v("Fragment DETACHED")
        if (EventBusType.isOnAttach(getEventBusType())) {
            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this)
            }
        }
        super.onDetach()
    }

    fun showProgressDialog(message: Int) {
        if (isNotThere()) return

        if (progressDialog == null) {
            withActivity {
                progressDialog = KProgressHUD.create(it).apply {
                    this.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    this.setCancellable(false)
                    this.setDimAmount(0.3f)
                }
            }
        }

        if (message != 0) {
            progressDialog?.setLabel(getString(message))
        }
        progressDialog?.show()
    }

    fun dismissProgressDialog() {
        if (isNotThere()) return

        progressDialog?.dismiss()
    }

    open fun toastInternetRequired() {
        toast(R.string.prompt_internet_required)
    }

    fun isConnectedToInternet(action: () -> Unit) {
        context?.let {
            if (it.isConnectedToInternet()) {
                action.invoke()
            } else {
                toastInternetRequired()
            }
        }
    }

    fun checkLoadingIndicator(active: Boolean, message: Int) {
        if (active) {
            showProgressDialog(message)
        } else {
            dismissProgressDialog()
        }
    }

    inline fun showOkAlertDialog(message: CharSequence,
                                 title: CharSequence? = null,
                                 buttonText: Int = 0,
                                 cancellable: Boolean = false,
                                 crossinline action: () -> Unit) {
        if (isNotThere()) return

        activity?.alert(message, title) {
            isCancelable = cancellable

            if (buttonText == 0) {
                okButton {
                    action.invoke()
                    it.dismiss()
                }
            } else {
                positiveButton(buttonText) {
                    action.invoke()
                    it.dismiss()
                }
            }
        }?.show()
    }

    inline fun showYesAlertDialog(message: CharSequence,
                                  title: CharSequence? = null,
                                  buttonText: Int = 0,
                                  cancellable: Boolean = false,
                                  crossinline action: () -> Unit) {
        if (isNotThere()) return

        activity?.alert(message, title) {
            isCancelable = cancellable

            if (buttonText == 0) {
                yesButton {
                    action.invoke()
                    it.dismiss()
                }
            } else {
                positiveButton(buttonText) {
                    action.invoke()
                    it.dismiss()
                }
            }
        }?.show()
    }

    inline fun showNoAlertDialog(message: CharSequence,
                                 title: CharSequence? = null,
                                 buttonText: Int = 0,
                                 cancellable: Boolean = false,
                                 crossinline action: () -> Unit) {
        if (isNotThere()) return

        activity?.alert(message, title) {
            isCancelable = cancellable

            if (buttonText == 0) {
                noButton {
                    action.invoke()
                    it.dismiss()
                }
            } else {
                negativeButton(buttonText) {
                    action.invoke()
                    it.dismiss()
                }
            }
        }?.show()
    }

    inline fun showYesNoAlertDialog(message: CharSequence,
                                    title: CharSequence? = null,
                                    yesButtonText: Int = 0,
                                    noButtonText: Int = 0,
                                    cancellable: Boolean = false,
                                    crossinline yesAction: () -> Unit,
                                    crossinline noAction: () -> Unit) {
        if (isNotThere()) return

        activity?.alert(message, title) {
            isCancelable = cancellable

            if (yesButtonText == 0) {
                yesButton {
                    yesAction.invoke()
                    it.dismiss()
                }
            } else {
                positiveButton(yesButtonText) {
                    yesAction.invoke()
                    it.dismiss()
                }
            }

            if (noButtonText == 0) {
                noButton {
                    noAction.invoke()
                    it.dismiss()
                }
            } else {
                negativeButton(noButtonText) {
                    noAction.invoke()
                    it.dismiss()
                }
            }
        }?.show()
    }
}
