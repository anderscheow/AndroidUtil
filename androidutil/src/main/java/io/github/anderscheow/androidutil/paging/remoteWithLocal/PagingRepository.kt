package io.github.anderscheow.androidutil.paging.remoteWithLocal

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.anderscheow.androidutil.constant.NetworkState
import io.github.anderscheow.androidutil.paging.util.Listing

abstract class PagingRepository<T : PagingModel> {

    protected val totalItems = MutableLiveData<Long>()

    abstract fun insertResultIntoDb(items: List<T>?)

    abstract fun getFirstPageItemsFromApi()

    @MainThread
    abstract fun getItems(pageSize: Int): Listing<T>

    @MainThread
    protected fun refresh(): LiveData<NetworkState> {
        val networkState = MutableLiveData<NetworkState>()
        networkState.value = NetworkState.LOADING

        getFirstPageItemsFromApi()

        return networkState
    }
}