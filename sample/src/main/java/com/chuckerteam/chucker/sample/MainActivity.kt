package com.chuckerteam.chucker.sample

import android.os.Bundle
import android.os.StrictMode
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chuckerteam.chucker.api.Chucker
import com.chuckerteam.chucker.api.extramodule.ExtraModuleRegistry
import com.chuckerteam.chucker.internal.featureflag.DefaultFeatureFlagStore
import com.chuckerteam.chucker.internal.featureflag.FeatureFlagModule
import com.chuckerteam.chucker.sample.databinding.ActivityMainSampleBinding

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

        ExtraModuleRegistry.addExtraModule(
            FeatureFlagModule(
                passedStore = DefaultFeatureFlagStore(applicationContext),
                initialFlags = { store ->
                    SampleFeatureFlag.values().forEach {
                        store.set(it, false)
                    }
                }
            )
        )

        with(mainBinding) {
            setContentView(root)
            doHttp.setOnClickListener {
                for (task in httpTasks) {
                    task.run()
                }
            }

            launchChuckerDirectly.visibility = if (Chucker.isOp) View.VISIBLE else View.GONE
            launchChuckerDirectly.setOnClickListener { launchChuckerDirectly() }

            showAllFlags?.setOnClickListener {
                val text = SampleFeatureFlag.values().joinToString(separator = "\n") {
                    "${it.name} : ${it.isEnabled()}"
                }
                Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
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
