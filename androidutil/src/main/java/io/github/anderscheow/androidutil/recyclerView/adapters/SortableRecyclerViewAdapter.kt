package io.github.anderscheow.androidutil.recyclerView.adapters

import android.content.Context
import java.util.*

abstract class SortableRecyclerViewAdapter<T>(context: Context) : BaseRecyclerViewAdapter<T>(context) {

    fun setItems(items: MutableList<T>?, comparator: Comparator<T>) {
        items?.let {
            setItemsWithoutNotify(it)
            sort(comparator)
        }
    }

    private fun sort(comparator: Comparator<T>) {
        items.let {
            Collections.sort(it, comparator)

            notifyDataSetChanged()
        }
    }
}
