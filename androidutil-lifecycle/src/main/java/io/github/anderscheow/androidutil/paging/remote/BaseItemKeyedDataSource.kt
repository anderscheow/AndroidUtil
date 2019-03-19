package io.github.anderscheow.androidutil.paging.remote

import androidx.annotation.CallSuper
import androidx.lifecycle.MutableLiveData
import androidx.paging.ItemKeyedDataSource
import io.github.anderscheow.androidutil.constant.NetworkState
import java.util.concurrent.Executor

abstract class BaseItemKeyedDataSource<Key, Value>(private val retryExecutor: Executor) : ItemKeyedDataSource<Key, Value>() {

    private var retry: (() -> Any)? = null

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()
    val totalItems: MutableLiveData<Long> = MutableLiveData()

    var pageNumber = 1
        private set

    protected var hasNext = true

    abstract fun loadInitial(success: (List<Value>, Long) -> Unit, failed: (String) -> Unit)

    abstract fun loadAfter(success: (List<Value>) -> Unit, failed: (String) -> Unit)

    @CallSuper
    override fun loadInitial(params: ItemKeyedDataSource.LoadInitialParams<Key>, callback: ItemKeyedDataSource.LoadInitialCallback<Value>) {
        retry = {
            loadInitial(params, callback)
        }

        networkState.postValue(NetworkState.LOADING)

        loadInitial({ items, total ->
            callback.onResult(items)

            postSuccessValue(total)
        }, {
            postFailedValue(it)
        })
    }

    @CallSuper
    override fun loadAfter(params: ItemKeyedDataSource.LoadParams<Key>, callback: ItemKeyedDataSource.LoadCallback<Value>) {
        retry = {
            loadAfter(params, callback)
        }

        networkState.postValue(NetworkState.LOADING)

        loadAfter({ items ->
            callback.onResult(items)

            postSuccessValue()
        }, {
            postFailedValue(it)
        })
    }

    @CallSuper
    override fun loadBefore(params: ItemKeyedDataSource.LoadParams<Key>, callback: ItemKeyedDataSource.LoadCallback<Value>) {
    }

    private fun postSuccessValue() {
        networkState.postValue(NetworkState.LOADED)

        pageNumber += 1

        retry = null
    }

    private fun postSuccessValue(totalOfElements: Long) {
        networkState.postValue(NetworkState.LOADED)
        totalItems.postValue(totalOfElements)

        pageNumber += 1

        retry = null
    }

    private fun postFailedValue(errorMessage: String) {
        networkState.postValue(NetworkState.error(errorMessage))
    }

    fun retry() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            retryExecutor.execute {
                it.invoke()
            }
        }
    }
}
