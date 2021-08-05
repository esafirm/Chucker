package com.chuckerteam.chucker.sample

import android.os.Bundle
import android.os.StrictMode
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chucker.featureflag.api.FeatureFlagModule
import com.chucker.featureflag.api.store.ObservedFeatureFlagStore
import com.chucker.featureflag.api.store.PersistentFeatureFlagStore
import com.chuckerteam.chucker.api.Chucker
import com.chuckerteam.chucker.api.extramodule.ExtraModuleRegistry
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
                passedStore = ObservedFeatureFlagStore(
                    PersistentFeatureFlagStore(applicationContext)
                ) { featureName, isEnabled ->
                    SampleFeatureFlag.valueOf(featureName).isEnabled = isEnabled
                },
                initialFlags = { store ->
                    SampleFeatureFlag.values().forEach {
                        store.set(it.name, false)
                    }
                },
                onLoad = { store ->
                    store.getAll()
                        .forEach {
                            SampleFeatureFlag.valueOf(it.first).isEnabled = it.second
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
                    "${it.name} : ${it.isEnabled}"
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
