package io.github.anderscheow.androidutil.paging.remote

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import java.util.concurrent.Executor

abstract class BaseDataSourceFactory<Key, Value>(private val executor: Executor) : DataSource.Factory<Key, Value>() {

    val mutableLiveData: MutableLiveData<BaseItemKeyedDataSource<Key, Value>> = MutableLiveData()

    protected abstract fun getItemKeyedDataSource(executor: Executor): BaseItemKeyedDataSource<Key, Value>

    override fun create(): DataSource<Key, Value> {
        val itemKeyedDataSource = getItemKeyedDataSource(executor)
        mutableLiveData.postValue(itemKeyedDataSource)
        return itemKeyedDataSource
    }
}
