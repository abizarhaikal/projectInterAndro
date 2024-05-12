package com.example.myintermediate.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myintermediate.data.remote.ListStoryItem
import com.example.myintermediate.databinding.ItemHomeBinding
import com.example.myintermediate.view.DetailActivity

class StoryAdapter :ListAdapter<ListStoryItem, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(val binding: ItemHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listItem : ListStoryItem) {
            Glide.with(itemView.context)
                .load(listItem.photoUrl)
                .into(binding.ivStory)

            binding.tvName.text = listItem.name
            binding.tvDescription.text = listItem.description

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_ID, listItem.id.toString())
                itemView.context.startActivity(intent)

                Toast.makeText(itemView.context, "Kamu memilih ${listItem.id}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryAdapter.MyViewHolder {
        val binding = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryAdapter.MyViewHolder, position: Int) {
        val listItem = getItem(position)
        holder.bind(listItem)
    }

    companion object {
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<ListStoryItem>(){
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }

}