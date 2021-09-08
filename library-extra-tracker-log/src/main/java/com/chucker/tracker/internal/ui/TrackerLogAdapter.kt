package com.chucker.tracker.internal.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chucker.tracker.databinding.ChuckerItemLogBinding

internal class TrackerLogAdapter : ListAdapter<String, TrackerLogAdapter.ViewHolder>(DiffCallback) {

    internal class ViewHolder(private val binding: ChuckerItemLogBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(value: String) {
            binding.root.text = value
        }
    }

    internal object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        // Overriding function is empty on purpose to avoid flickering by default animator
        override fun getChangePayload(oldItem: String, newItem: String) = Unit
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ChuckerItemLogBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}