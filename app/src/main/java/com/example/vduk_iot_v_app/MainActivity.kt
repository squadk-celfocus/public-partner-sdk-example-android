package com.example.vduk_iot_v_app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vduk_iot_v_app.Const.CLIENT_ID
import com.example.vduk_iot_v_app.Const.GRANT_ID
import com.example.vduk_iot_v_app.databinding.ActivityMainBinding
import com.vodafone.smartlife.vpartner.domain.usecases.VPartnerLib

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val vPartnerLibIntegration = VPartnerLib(
        clientId = CLIENT_ID, // defined by partner
        grantId = GRANT_ID, // defined by partner
        partnerCode = PARTNER_CODE,
        partnerLogo = PARTNER_LOGO
    )

    companion object {
        const val PARTNER_CODE = "DUMMY_PARTNER"
        const val PRODUCT_CODE = "DUMMY_PARTNER_TRACKER1"
        const val PARTNER_LOGO = "ic_partner.png"
        const val PRODUCT_ID = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButtons()
    }

    private fun setupButtons() {

        binding.switchPartnerLogo.setOnCheckedChangeListener { _, b ->
            if (b)
                vPartnerLibIntegration.storePartnerLogo(PARTNER_LOGO)
            else
                vPartnerLibIntegration.storePartnerLogo("")
        }

        binding.btnLogoutIdtm.setOnClickListener {
            vPartnerLibIntegration.logout(this)
            Toast.makeText(this, "Logout successful", Toast.LENGTH_LONG).show()
        }

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
            productId = if (binding.editTextImei.text.toString()
                .isEmpty()
            ) PRODUCT_ID else binding.editTextImei.text.toString(),
            productCode = if (binding.editTextProdcutCode.text.toString()
                .isEmpty()
            ) PRODUCT_CODE else binding.editTextProdcutCode.text.toString()
        )

        vPartnerLibIntegration.storePartnerCode(
            if (binding.editTextPartnerCode.text.toString()
                .isEmpty()
            ) PARTNER_CODE else binding.editTextPartnerCode.text.toString()
        )
    }

    private fun openSdkWithEntryPointManageSubscriptions() {
        vPartnerLibIntegration.manageSubscriptions(this)

        vPartnerLibIntegration.storePartnerCode(
            if (binding.editTextPartnerCode.text.toString()
                .isEmpty()
            ) PARTNER_CODE else binding.editTextPartnerCode.text.toString()
        )
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}
