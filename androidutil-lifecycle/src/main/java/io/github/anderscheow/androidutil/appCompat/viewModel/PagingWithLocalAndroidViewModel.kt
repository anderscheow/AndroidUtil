package io.github.anderscheow.androidutil.appCompat.viewModel

import android.app.Application
import androidx.lifecycle.*
import io.github.anderscheow.androidutil.constant.NetworkState
import io.github.anderscheow.androidutil.paging.remote.BaseDataSourceFactory
import io.github.anderscheow.androidutil.paging.remoteWithLocal.PagingModel
import io.github.anderscheow.androidutil.paging.util.Listing
import java.util.concurrent.Executor

abstract class PagingWithLocalAndroidViewModel<in Args, Value : PagingModel>(context: Application) : PagingWithoutLocalAndroidViewModel<Args, Void, Value>(context) {

    protected val repoResult = MutableLiveData<Listing<Value>>()

    var refreshState: LiveData<NetworkState>? = null

    override fun start(args: Args?) {
        showProgressDialog()
    }

    override fun onRefresh() {
        repoResult.value?.refresh?.invoke()
    }

    override fun init() {
        items = Transformations.switchMap(repoResult) { it.pagedList }
        networkState = Transformations.switchMap(repoResult) { it.networkState }
        totalItems = Transformations.switchMap(repoResult) { it.totalItems }
        refreshState = Transformations.switchMap(repoResult) { it.refreshState }
    }

    override fun retry() {
        val listing = repoResult.value
        listing?.retry?.invoke()
    }

    override val numberOfThreads: Int
        get() {
            throw IllegalStateException("Do not use this field (numberOfThreads)")
        }

    override val loadPageSize: Int
        get() {
            throw IllegalStateException("Do not use this field (loadPageSize)")
        }

    override fun getDataSourceFactory(executor: Executor): BaseDataSourceFactory<Void, Value> {
        throw IllegalStateException("Do not use this method (getDataSourceFactory)")
    }
}

fun <T, Value : PagingModel> PagingWithLocalAndroidViewModel<T, Value>.observeRefreshState(`object`: Any, customAction: ((NetworkState) -> Unit)? = null) {
    (`object` as? LifecycleOwner)?.let { owner ->
        this.refreshState?.observe(owner, Observer { refreshState ->
            refreshState?.let {
                customAction?.invoke(refreshState)
            }
        })
    }
}