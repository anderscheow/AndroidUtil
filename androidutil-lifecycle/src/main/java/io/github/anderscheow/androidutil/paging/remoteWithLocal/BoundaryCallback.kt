package io.github.anderscheow.androidutil.paging.remoteWithLocal

import androidx.annotation.MainThread
import androidx.paging.PagedList
import io.github.anderscheow.androidutil.paging.util.PagingRequestHelper
import io.github.anderscheow.androidutil.paging.util.createStatusLiveData
import java.util.concurrent.Executor

abstract class BoundaryCallback<T : PagingModel>(private val handleResponse: (List<T>?) -> Unit,
                                                 private val executor: Executor)
    : PagedList.BoundaryCallback<T>() {

    val helper = PagingRequestHelper(executor)
    val networkState = helper.createStatusLiveData()

    private var pageNumber = 1

    protected var hasNext = true

    abstract fun loadInitial(pageNumber: Int = 1, success: (List<T>) -> Unit, failed: (String) -> Unit)

    abstract fun loadAfter(pageNumber: Int, success: (List<T>) -> Unit, failed: (String) -> Unit)

    fun refresh() {
        pageNumber = 1
        hasNext = true

        onZeroItemsLoaded()
    }

    /**
     * Database returned 0 items. We should query the backend for more items.
     */
    @MainThread
    override fun onZeroItemsLoaded() {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) { callback ->
            loadInitial(pageNumber, { items ->
                successCallback(items, callback)
            }, {
                failedCallback(it, callback)
            })
        }
    }

    /**
     * User reached to the end of the list.
     */
    @MainThread
    override fun onItemAtEndLoaded(itemAtEnd: T) {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) { callback ->
            if (hasNext) {
                loadAfter(pageNumber, { items ->
                    successCallback(items, callback)
                }, {
                    failedCallback(it, callback)
                })
            } else {
                successCallback(emptyList(), callback)
            }
        }
    }

    override fun onItemAtFrontLoaded(itemAtFront: T) {
        // ignored, since we only ever append to what's in the DB
    }

    private fun successCallback(items: List<T>, callback: PagingRequestHelper.Request.Callback) {
        insertItemsIntoDb(items, callback)
    }

    private fun failedCallback(errorMessage: String, callback: PagingRequestHelper.Request.Callback) {
        callback.recordFailure(errorMessage)
    }

    /**
     * every time callback gets new items, boundary callback simply inserts them into the database and
     * paging library takes care of refreshing the list if necessary.
     */
    private fun insertItemsIntoDb(response: List<T>, callback: PagingRequestHelper.Request.Callback) {
        executor.execute {
            handleResponse(response)
            callback.recordSuccess()

            pageNumber += 1
        }
    }
}