package com.chuckerteam.chucker.sample

import android.os.Bundle
import android.os.StrictMode
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chucker.logging.api.LoggingCollectorImpl
import com.chuckerteam.chucker.api.Chucker
import com.chuckerteam.chucker.sample.databinding.ActivityMainSampleBinding
import com.chuckerteam.chucker.sample.extras.SampleFragment

private val interceptorTypeSelector = InterceptorTypeSelector()

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainSampleBinding

    private val client by lazy {
        createOkHttpClient(applicationContext, interceptorTypeSelector)
    }

    private val httpTasks by lazy {
        listOf(HttpBinHttpTask(client), DummyImageHttpTask(client), PostmanEchoHttpTask(client))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainSampleBinding.inflate(layoutInflater)

        with(mainBinding) {
            setContentView(root)
            doHttp.setOnClickListener {
                for (task in httpTasks) {
                    task.run()
                }
            }

            launchChuckerDirectly.visibility = if (Chucker.isOp) View.VISIBLE else View.GONE
            launchChuckerDirectly.setOnClickListener { launchChuckerDirectly() }

            // Extra module FeatureFlag
            showAllFlags?.setOnClickListener {
                val text = SampleFeatureFlag.values().joinToString(separator = "\n") {
                    "${it.name} : ${it.isEnabled}"
                }
                Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
            }

            // Extra module ActivityInfo
            addFragment?.setOnClickListener {
                supportFragmentManager.beginTransaction()
                    .add(SampleFragment(), SampleFragment::class.java.simpleName)
                    .commit()
                Toast.makeText(
                    application,
                    "Fragment added, please trigger onResume() to see effect",
                    Toast.LENGTH_SHORT
                ).show()
            }

            interceptorTypeLabel.movementMethod = LinkMovementMethod.getInstance()
            useApplicationInterceptor.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    interceptorTypeSelector.value = InterceptorType.APPLICATION
                }
            }
            useNetworkInterceptor.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    interceptorTypeSelector.value = InterceptorType.NETWORK
                }
            }

            addLog?.setOnClickListener {
                val map = mapOf(
                    "event" to "click",
                    "product" to "my product"
                )
                LoggingCollectorImpl().sendLog("tag1", "test")
                Toast.makeText(applicationContext, map.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build()
        )
    }

    private fun launchChuckerDirectly() {
        // Optionally launch Chucker directly from your own app UI
        startActivity(Chucker.getLaunchIntent(this))
    }
}
