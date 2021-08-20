package com.chucker.activity.internal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.chuckerteam.chucker.databinding.ChuckerActivityInfoBinding
import com.chuckerteam.chucker.databinding.ChuckerListItemInfoBinding

internal class ActivityInfoActivity : AppCompatActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ChuckerActivityInfoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        title = "Activity Info"

        createInfoItems()
    }

    private fun createInfoItems() {
        val info = intent.extras?.getSerializable(EXTRA_ACTIVITY_INFO) as? ActivityInfo
                ?: error("Activity info is not available")

        val fragments = info.fragments.joinToString("\n")

        binding.container.run {
            addView(createInfoView("Name", "${info.packageName}.${info.simpleName}"))
            addView(createInfoView("Fragments", fragments))

            info.otherInfo.forEach {
                addView(createInfoView(it.key, it.value))
            }
        }
    }

    private fun createInfoView(label: String, value: String): View {
        val binding = ChuckerListItemInfoBinding.inflate(layoutInflater)
        binding.txtLabel.text = label
        binding.txtValue.text = value
        return binding.root
    }

    companion object {

        private const val EXTRA_ACTIVITY_INFO = "Extra.ActivityInfo"

        fun createIntent(context: Context, info: ActivityInfo): Intent {
            return Intent(context, ActivityInfoActivity::class.java).apply {
                putExtra(EXTRA_ACTIVITY_INFO, info)
            }
        }

        fun start(context: Context, info: ActivityInfo) {
            context.startActivity(createIntent(context, info))
        }
    }
}