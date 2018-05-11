package com.simprints.simodkadapter.activities.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.simprints.libsimprints.Constants.*
import com.simprints.libsimprints.Identification

class MainActivity : AppCompatActivity(), MainContract.View {

    companion object {
        private const val REGISTER_REQUEST_CODE = 97
        private const val IDENTIFY_REQUEST_CODE = 98
        private const val VERIFY_REQUEST_CODE = 99
    }

    override lateinit var presenter: MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = MainPresenter(this, intent.action).apply { start() }
    }

    override fun returnActionErrorToClient() {
        setResult(SIMPRINTS_INVALID_INTENT_ACTION, intent)
        finish()
    }

    override fun requestRegisterCallout() {
        val registerIntent = Intent(SIMPRINTS_REGISTER_INTENT).apply { putExtras(intent) }
        startActivityForResult(registerIntent, REGISTER_REQUEST_CODE)
    }

    override fun requestIdentifyCallout() {
        val registerIntent = Intent(SIMPRINTS_IDENTIFY_INTENT).apply { putExtras(intent) }
        startActivityForResult(registerIntent, IDENTIFY_REQUEST_CODE)
    }

    override fun requestVerifyCallout() {
        val registerIntent = Intent(SIMPRINTS_VERIFY_INTENT).apply { putExtras(intent) }
        startActivityForResult(registerIntent, VERIFY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK || data == null)
            setResult(resultCode, data).let { finish() }
        else
            when (requestCode) {
                REGISTER_REQUEST_CODE -> presenter.processRegistration(data.getParcelableExtra(SIMPRINTS_REGISTRATION))
                IDENTIFY_REQUEST_CODE -> presenter.processIdentification(data.getParcelableArrayListExtra<Identification>(SIMPRINTS_IDENTIFICATIONS))
                VERIFY_REQUEST_CODE -> presenter.processVerification(data.getParcelableExtra(SIMPRINTS_VERIFICATION))
                else -> presenter.processReturnError()
            }
    }

}
