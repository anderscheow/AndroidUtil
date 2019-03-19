package io.github.anderscheow.androidutil.appCompat.viewModel

import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.github.anderscheow.androidutil.constant.NetworkState
import io.github.anderscheow.androidutil.kotlinExt.toast
import io.github.anderscheow.androidutil.paging.remote.BaseDataSourceFactory
import io.github.anderscheow.androidutil.paging.remote.BaseItemKeyedDataSource
import io.github.anderscheow.androidutil.recyclerView.adapters.FoundationPagedListAdapter
import org.jetbrains.anko.toast
import java.util.concurrent.Executor
import java.util.concurrent.Executors

abstract class PagingWithoutLocalAndroidViewModel<in Args, Key, Value>(context: Application) : BaseAndroidViewModel<Args>(context) {

    protected abstract val numberOfThreads: Int

    protected abstract val loadPageSize: Int

    protected abstract fun getDataSourceFactory(executor: Executor): BaseDataSourceFactory<Key, Value>

    var items: LiveData<PagedList<Value>>? = null
    var networkState: LiveData<NetworkState>? = null
    var totalItems: LiveData<Long>? = null

    private var tDataSource: LiveData<BaseItemKeyedDataSource<Key, Value>>? = null

    override fun start(args: Args?) {
        showProgressDialog()
    }

    override fun onRefresh() {
        tDataSource?.value?.invalidate()
    }

    open fun init() {
        val executor = Executors.newFixedThreadPool(numberOfThreads)
        val factory = getDataSourceFactory(executor)
        tDataSource = factory.mutableLiveData

        networkState = Transformations.switchMap(factory.mutableLiveData
        ) { input -> input.networkState }

        totalItems = Transformations.switchMap(factory.mutableLiveData
        ) { input -> input.totalItems }

        val pagedListConfig = PagedList.Config.Builder().setEnablePlaceholders(false)
                .setInitialLoadSizeHint(loadPageSize)
                .setPageSize(loadPageSize).build()

        items = LivePagedListBuilder(factory, pagedListConfig)
                .setFetchExecutor(executor)
                .build()
    }

    open fun retry() {
        tDataSource?.value?.retry()
    }
}

fun <Args, Key, Value> PagingWithoutLocalAndroidViewModel<Args, Key, Value>.observeItems(`object`: Any, adapter: FoundationPagedListAdapter<Value>, customAction: ((PagedList<Value>) -> Unit)? = null) {
    (`object` as? LifecycleOwner)?.let { owner ->
        this.items?.observe(owner, Observer { list ->
            adapter.submitList(list)

            list?.let {
                customAction?.invoke(list)
            }
        })
    }
}

fun <Args, Key, Value> PagingWithoutLocalAndroidViewModel<Args, Key, Value>.observeNetworkState(`object`: Any, adapter: FoundationPagedListAdapter<Value>, showErrorMessage: Boolean = true, customAction: ((NetworkState) -> Unit)? = null) {
    (`object` as? LifecycleOwner)?.let { owner ->
        this.networkState?.observe(owner, Observer { networkState ->
            adapter.networkState = networkState

            networkState?.let {
                if (networkState.status == NetworkState.Status.FAILED) {
                    this.finishLoading()

                    if (showErrorMessage) {
                        if (`object` is Context) {
                            `object`.toast(it.message ?: "")
                        } else if (`object` is Fragment) {
                            `object`.toast(it.message ?: "")
                        }
                    }
                }
                customAction?.invoke(networkState)
            }
        })
    }
}

fun <Args, Key, Value> PagingWithoutLocalAndroidViewModel<Args, Key, Value>.observeTotalItems(`object`: Any, customAction: ((Long) -> Unit)? = null) {
    (`object` as? LifecycleOwner)?.let { owner ->
        this.totalItems?.observe(owner, Observer { totalItems ->
            totalItems?.let {
                this.finishLoading(totalItems)
                customAction?.invoke(totalItems)
            }
        })
    }
}
