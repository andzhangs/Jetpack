package io.dushu.room

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.dushu.room.databinding.ItemMvvmBinding
import io.dushu.room.entity.relation.StudentWithCourseEntity

/**
 * author: zhangshuai 6/27/21 10:28 PM
 * email: zhangshuai@dushu365.com
 * mark:
 */
class MyAdapter(private var mList: ArrayList<StudentWithCourseEntity> = arrayListOf()) :
    RecyclerView.Adapter<MyAdapter.ItemViewHolder>() {

    class ItemViewHolder(var dataBinding: ItemMvvmBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val dataBinding = DataBindingUtil.inflate<ItemMvvmBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_mvvm,
            parent,
            false
        )
        return ItemViewHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        if (mList.isNotEmpty()) {
            val result = mList[position]

            holder.itemView.setOnClickListener {
                this.mItemClickListener?.onItemClick(position, result)
            }
            holder.dataBinding.data = result
        }
    }

    override fun getItemCount(): Int = if (mList.size > 0) mList.size else 0

    fun getItem(position: Int) = mList[position]

    private val mItemCallback by lazy {
        object : DiffUtil.ItemCallback<StudentWithCourseEntity>() {
            override fun areItemsTheSame(
                oldItem: StudentWithCourseEntity,
                newItem: StudentWithCourseEntity
            ): Boolean {
                return oldItem.student?.name == newItem.student?.name
            }

            override fun areContentsTheSame(
                oldItem: StudentWithCourseEntity,
                newItem: StudentWithCourseEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    fun setData(newList: List<StudentWithCourseEntity>) {
        if (newList.isNotEmpty()) {
            val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize() = mList.size

                override fun getNewListSize() = newList.size

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return mItemCallback.areItemsTheSame(
                        this@MyAdapter.getItem(oldItemPosition),
                        newList[newItemPosition]
                    )
                }

                override fun areContentsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ): Boolean {
                    return mItemCallback.areContentsTheSame(
                        this@MyAdapter.getItem(oldItemPosition),
                        newList[newItemPosition]
                    )
                }
            })

            mList.clear()
            mList.addAll(newList)
            diffResult.dispatchUpdatesTo(this)
        } else {
            if (mList.isNotEmpty()) {
                notifyItemRangeRemoved(0, mList.size)
            }
            mList.clear()
        }
    }

    private var mItemClickListener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mItemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, data: StudentWithCourseEntity)
    }
}