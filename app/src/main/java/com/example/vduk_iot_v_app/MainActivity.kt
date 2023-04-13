package com.example.vduk_iot_v_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.vduk_iot_v_app.databinding.ActivityMainBinding
import com.vodafone.smartlife.vpartner.domain.usecases.VPartnerLib
import com.vodafone.smartlife.vpartner.util.VisualMode
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        const val PARTNER_PASSWORD = ""
        const val GRANT_ID = ""
        const val CLIENT_ID = ""
        const val PARTNER_CODE = ""
        const val PRODUCT_CODE = ""
        const val PARTNER_LOGO = "ic_partner.png"
        const val PRODUCT_ID = ""
    }

    private val vPartnerLibIntegration = VPartnerLib(
        partnerPassword = PARTNER_PASSWORD,
        clientId = CLIENT_ID,
        grantId = GRANT_ID,
        partnerCode = PARTNER_CODE,
        partnerLogo = PARTNER_LOGO,
        sponsorLogo = "",
        locale = "",
        useDeeplink = false,
        visualMode = VisualMode.LightMode
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButtons()
    }

    private fun setupButtons() {
        binding.btnPartnerAddDevice.setOnClickListener {
            openSdkWithEntryPointAddDevice()
        }

        binding.btnPartnerManageSubscriptions.setOnClickListener {
            openSdkWithEntryPointManageSubscriptions()
        }
    }

    private fun openSdkWithEntryPointAddDevice() {
        vPartnerLibIntegration.addDevice(
            this,
            productId = binding.editTextImei.text.toString().ifEmpty { PRODUCT_ID },
            productCode = binding.editTextProdcutCode.text.toString().ifEmpty { PRODUCT_CODE },
            ::partnerStatusCallback
        )
    }

    private fun openSdkWithEntryPointManageSubscriptions() {
        vPartnerLibIntegration.manageSubscriptions(this)
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    private fun partnerStatusCallback(status: String) {
        println("Onboarding status: " + JSONObject(status).getString("status"))
    }
}
