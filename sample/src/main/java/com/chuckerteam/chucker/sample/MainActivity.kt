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
                val bundle = Bundle().apply {
                    putString("startDate", "2021-10-30")
                    putInt("departSelectedPrice", 538100)
                    putString("vertical", "flight")
                    putString("checkout_step", "1")
                    putString("originCity", "JKT")
                    putString("destinationCity", "DPS")
                    putString("endDate", "")
                    putString("journey", "depart")
                    putString("departLowestPrice", "538100")
                    putLong("orderId", 700125314L)
                    putString("infant", "0")
                    putString("transit", "0")
                    putString("passenger", "1")
                    putString("originAirport", "JKT")
                    putString("paymentGroup", "promote_group")
                    putString("specialCondition", "")
                    putString("screenName", "paymentMethodList")
                    putString("totalPayment", "538100")
                    putString("returnLowestPrice", "0")
                    putString("returnHighestPrice", "0")
                    putInt("roundTrip", 0)
                    putString("type", "")
                    putString("adult", "1")
                    putString("af_id", "1632826207617-8131621373172889803")
                    putString("child", "0")
                    putInt("showSeatLeftInfo", 0)
                    putString("variant", "autoOffAntiGalau")
                    putString("departAirline", "QG")
                    putString("returnAirline", "")
                    putString("departKeyword", "undefined")
                    putString("paymentService", "bni")
                    putString("totalTixPointEarned", "808")
                    putInt("useInsurance", 0)
                    putString("trackingId", "UA-122503490-1")
                    putString("paymentMethod", "virtualAccount")
                    putString("departHighestPrice", "1709100")
                    putString("paymentTimeLeft", "224")
                    putString("dev_key", "23yxb4TKEcPG2DZbpmfJWH")
                    putString("returnSelectedPrice", "0")
                    putString("seatClass", "economy")
                    putString("destinationAirport", "DPS")
                    putString("destinationKeyword", "undefined")
                    putParcelableArrayList("items", arrayListOf(
                        Bundle().apply {
                            putString("item_name", "JKTC-CPSC")
                            putString("item_brand", "citilink indonesia")
                            putLong("quantity", 1L)
                            putString("item_variant", "adult")
                            putDouble("price", 538100.0)
                            putString("item_category", "flight")
                            putString("currency", "IDR")
                            putString("item_id", "HLP-DPS")
                        }
                    ))
                }
                val map = hashMapOf<String, Any?>()
                val iterator = bundle.keySet().iterator()
                while (iterator.hasNext()) {
                    val key = iterator.next()
                    map[key] = bundle.get(key)
                }
                LoggingCollectorImpl().sendLog("tracker", map)
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
