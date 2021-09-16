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

internal class LoggingAdapter(private val copyCallback: (LogViewParam) -> Unit) :
    ListAdapter<LogViewParam, LoggingAdapter.ViewHolder>(DiffCallback) {

    internal class ViewHolder(
        private val binding: ChuckerItemLogBinding,
        private val copyCallback: (LogViewParam) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(value: LogViewParam) {
            binding.tag.text = value.tag
            binding.message.text = value.logText
            binding.timeStamp.text = value.dateText
            binding.copyButton.setOnClickListener {
                copyCallback.invoke(value)
            }
        }
    }

    internal object DiffCallback : DiffUtil.ItemCallback<LogViewParam>() {
        override fun areItemsTheSame(oldItem: LogViewParam, newItem: LogViewParam): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: LogViewParam, newItem: LogViewParam): Boolean {
            return oldItem == newItem
        }

        // Overriding function is empty on purpose to avoid flickering by default animator
        override fun getChangePayload(oldItem: LogViewParam, newItem: LogViewParam) = Unit
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