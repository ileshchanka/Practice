package info.igorek.practice.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import info.igorek.practice.databinding.ItemImageBinding
import info.igorek.practice.databinding.ItemTextBinding

class MyRecyclerAdapter : ListAdapter<MyRecyclerType, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MyRecyclerType>() {
            override fun areItemsTheSame(oldItem: MyRecyclerType, newItem: MyRecyclerType) = oldItem == newItem
            override fun getChangePayload(oldItem: MyRecyclerType, newItem: MyRecyclerType) = Any()
            override fun areContentsTheSame(oldItem: MyRecyclerType, newItem: MyRecyclerType) = oldItem == newItem
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is MyRecyclerType.ImageItem -> ViewType.IMAGE.ordinal
            is MyRecyclerType.TextItem -> ViewType.TEXT.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.TEXT.ordinal -> {
                TextViewHolder(
                    ItemTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }

            ViewType.IMAGE.ordinal -> {
                ImageViewHolder(
                    ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }

            else -> throw ClassCastException("Unknown viewType $viewType")
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        return when (holder) {
            is TextViewHolder -> {
                holder.bind(
                    getItem(position) as MyRecyclerType.TextItem
                )
            }

            is ImageViewHolder -> {
                holder.bind(
                    getItem(position) as MyRecyclerType.ImageItem
                )
            }

            else -> {}
        }
    }

    inner class ImageViewHolder(private val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: MyRecyclerType.ImageItem) {
            binding.imageItem.setImageResource(model.resId)
        }
    }

    inner class TextViewHolder(private val binding: ItemTextBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: MyRecyclerType.TextItem) {
            binding.textItem.text = model.text
        }
    }
}

enum class ViewType {
    IMAGE,
    TEXT
}