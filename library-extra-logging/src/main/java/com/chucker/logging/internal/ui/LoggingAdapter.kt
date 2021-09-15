package com.chucker.logging.internal.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chucker.logging.databinding.ChuckerItemLogBinding
import com.chucker.logging.internal.data.entity.LogData
import org.json.JSONObject
import java.text.SimpleDateFormat

internal class LoggingAdapter(private val copyCallback: (LogData) -> Unit) :
    ListAdapter<LogData, LoggingAdapter.ViewHolder>(DiffCallback) {

    internal class ViewHolder(
        private val binding: ChuckerItemLogBinding,
        private val copyCallback: (LogData) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(value: LogData) {
            binding.tag.text = value.tag
            binding.message.text = try {
                JSONObject(value.logString).toString(2)
            } catch (ignored: Exception) {
                value.logString
            }
            binding.timeStamp.text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(value.timeStamp)
            binding.copyButton.setOnClickListener {
                copyCallback.invoke(value)
            }
        }
    }

    internal object DiffCallback : DiffUtil.ItemCallback<LogData>() {
        override fun areItemsTheSame(oldItem: LogData, newItem: LogData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: LogData, newItem: LogData): Boolean {
            return oldItem == newItem
        }

        // Overriding function is empty on purpose to avoid flickering by default animator
        override fun getChangePayload(oldItem: LogData, newItem: LogData) = Unit
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ChuckerItemLogBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            copyCallback
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}