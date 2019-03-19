package io.github.anderscheow.androidutil.appCompat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import io.github.anderscheow.androidutil.BR
import io.github.anderscheow.androidutil.appCompat.fragment.FoundationFragment
import io.github.anderscheow.androidutil.appCompat.util.ProgressDialogMessage
import io.github.anderscheow.androidutil.appCompat.util.ToastMessage
import io.github.anderscheow.androidutil.appCompat.viewModel.BaseAndroidViewModel
import io.github.anderscheow.androidutil.kotlinExt.toast

abstract class LifecycleFragment<VM : BaseAndroidViewModel<*>> : FoundationFragment() {

    protected val viewModel by lazy {
        setupViewModel()
    }

    abstract fun setupViewModel(): VM

    abstract fun setupViewModelObserver(lifecycleOwner: LifecycleOwner)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupProgressDialog()
        setupToast()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, getResLayout(), container, false)
        val view = binding.root

        binding.setVariable(BR.obj, viewModel)

        return view
    }

    override fun onActivityCreated(savedInstaceState: Bundle?) {
        super.onActivityCreated(savedInstaceState)
        setupViewModelObserver(viewLifecycleOwner)
    }

    private fun setupProgressDialog() {
        viewModel.progressDialogMessage.observe(this, object : ProgressDialogMessage.ProgressDialogObserver {
            override fun onNewMessage(message: Int) {
                showProgressDialog(message)
            }

            override fun dismiss() {
                dismissProgressDialog()
            }
        })
    }

    private fun setupToast() {
        viewModel.toastMessage.observe(this, object : ToastMessage.ToastObserver {
            override fun onNewMessage(message: String?) {
                message?.let {
                    toast(it)
                }
            }
        })
    }
}
