package io.github.anderscheow.androidutil.recyclerView.util

import androidx.recyclerview.widget.DiffUtil

class SectionGroup {

    var section: Any? = null
        private set

    var row: Any? = null
        private set

    var isRow: Boolean = false
        private set

    var requiredFooter: Boolean = false
        private set

    override fun equals(other: Any?): Boolean {
        (other as? SectionGroup)?.let {
            return section == other.section &&
                    row == other.row &&
                    isRow == other.isRow &&
                    requiredFooter == other.requiredFooter
        }
        return false
    }

    companion object {

        var DIFF_CALLBACK: DiffUtil.ItemCallback<SectionGroup> = object : DiffUtil.ItemCallback<SectionGroup>() {
            override fun areItemsTheSame(oldItem: SectionGroup, newItem: SectionGroup): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: SectionGroup, newItem: SectionGroup): Boolean {
                return oldItem == newItem
            }
        }

        fun createSection(section: Any): SectionGroup {
            return SectionGroup().apply {
                this.section = section
                this.isRow = false
                this.requiredFooter = false
            }
        }

        fun createRow(row: Any): SectionGroup {
            return SectionGroup().apply {
                this.row = row
                this.isRow = true
                this.requiredFooter = false
            }
        }

        fun createFooter(): SectionGroup {
            return SectionGroup().apply {
                this.isRow = true
                this.requiredFooter = true
            }
        }
    }
}