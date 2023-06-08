package info.igorek.practice.contentprovider

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import info.igorek.practice.contentprovider.SongAdapter.SongViewHolder
import info.igorek.practice.databinding.ItemSongBinding

class SongAdapter(
//    val onItemClick: (message: String) -> Unit,
) : ListAdapter<Song, SongViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Song>() {
            override fun areItemsTheSame(oldItem: Song, newItem: Song) = oldItem == newItem
            override fun getChangePayload(oldItem: Song, newItem: Song) = Any()
            override fun areContentsTheSame(oldItem: Song, newItem: Song) = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        return SongViewHolder(
            ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        return holder.bind(getItem(position))
    }

    inner class SongViewHolder(private val binding: ItemSongBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: Song) {
            with(binding) {
                textviewSongName.text = model.title
                textviewSongArtist.text = model.artist
                textviewSongGenre.text = model.genre
            }
        }
    }
}