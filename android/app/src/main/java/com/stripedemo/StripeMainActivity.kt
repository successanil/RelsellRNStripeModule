/*
 * Copyright (c) 2019. Relsell Global
 */

/*
 * Copyright (c) 2018. Relsell Global
 */

package com.stripedemo

import android.app.Activity
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.stripe.android.Stripe
import com.stripe.android.TokenCallback
import com.stripe.android.model.Card
import com.stripe.android.model.Token
import com.stripe.android.view.CardInputWidget

class StripeMainActivity : AppCompatActivity() {

    internal var PUBLISHABLE_KEY = "pk_test_Tjs4dEhIDszNd05hkeAW6H0f"
    internal var stripe: Stripe? = null
    internal var mCardInputWidget: CardInputWidget? = null
    internal var paymentButton: Button? = null
    internal var amountToPaytV: TextView? = null
    internal var pb: ProgressBar? = null
    internal var paymentHandler = PaymentHandler()
    internal var loggedInUserId: String? = null
    internal var TAG = StripeMainActivity::class.java.simpleName


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stripe_main)

        mCardInputWidget = findViewById(R.id.card_input_widget)




        stripe = Stripe(this@StripeMainActivity, PUBLISHABLE_KEY)

        paymentButton = findViewById(R.id.payment_button)

//        pb = findViewById(R.id.progress_bar)

        amountToPaytV = findViewById(R.id.amount_topay)

        var bundle = intent.getBundleExtra(Constants.bundle)
        val amount = bundle.getFloat(Constants.amount)
        Log.d("StripeMainAct",""+amount)

        amountToPaytV!!.text = "$" + String.format("%.2f", amount)


        paymentButton!!.setOnClickListener { view ->
            val cardToSave = mCardInputWidget!!.card
            if (cardToSave != null && amount != 0f) {
                doTran(cardToSave, amount)
                paymentButton!!.text = "payement initiated"
                paymentButton!!.isEnabled = false
            }
        }


    }

    fun doTran(card: Card, amount: Float) {
        stripe!!.createToken(
                card,
                object : TokenCallback {
                    override fun onError(error: Exception) {

                        Toast.makeText(this@StripeMainActivity, "Payment was not successful. Please try again. ", Toast.LENGTH_LONG)
                                .show()
                        paymentButton!!.text = "Make Payment"
                        paymentButton!!.isEnabled = true

                    }

                    override fun onSuccess(token: Token) {

                        val gson = Gson()
                        val paymentRequest = PaymentRequest()
                        paymentRequest.stripeToken = token
                        paymentRequest.amount = amount
                        paymentRequest.debug = true
                        paymentRequest.isLiveModeStripeUserId = false
                        paymentRequest.loggedInUserId = loggedInUserId


                        val paymentJsonStr = gson.toJson(paymentRequest)
//                        VinoCellaApp.printLog(TAG, paymentJsonStr)

                        val url = "payment url"
                        //String url = BuildConfig.add_balance_url;


                        try {

                            PaymentTask(url, paymentJsonStr, paymentHandler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
                        } catch (e: Exception) {
                            Toast.makeText(this@StripeMainActivity, "Payment was not successful. Please try again. ", Toast.LENGTH_LONG)
                                    .show()
                            paymentButton!!.text = "Make Payment"
                            paymentButton!!.isEnabled = true
                        }


                    }
                }
        )
    }

    inner class PaymentHandler : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)

            if (msg != null && msg.what == Constants.PAYMENT_SUCCESS_CODE) {
                Toast.makeText(this@StripeMainActivity, "Payment Successful", Toast.LENGTH_LONG).show()
                setResult(Activity.RESULT_OK)
                finish()
            } else if (msg != null && msg.what == Constants.PAYMENT_FAILURE_CODE) {
                Toast.makeText(this@StripeMainActivity, "Payment Not Successful", Toast.LENGTH_LONG).show()
                setResult(Activity.RESULT_CANCELED)
                finish()
            }

        }
    }


}
