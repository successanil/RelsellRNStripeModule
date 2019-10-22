/*
 * Copyright (c) 2019. Relsell Global
 */

package com.stripedemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.facebook.react.bridge.*
import com.google.gson.Gson


internal class ActivityStarterModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    private var mPickerPromise: Promise? = null


    private val mActivityEventListener = object : BaseActivityEventListener() {

        override fun onActivityResult(activity: Activity?, requestCode: Int, resultCode: Int, intent: Intent?) {
            if (requestCode == Constants.STRIPE_ACTIVITY_INVOKE_CODE) {
                if (mPickerPromise != null) {
                    if (resultCode == Activity.RESULT_CANCELED) {
                        Log.v(TAG, "Payment activity Cancelled")
                        mPickerPromise!!.reject(PAYMENT_CANCELLED, "Payment was cancelled")
                    } else if (resultCode == Activity.RESULT_OK) {
                        //                        Uri uri = intent.getData();
                        //
                        //                        if (uri == null) {
                        //                            mPickerPromise.reject(E_NO_IMAGE_DATA_FOUND, "No image data found");
                        //                        } else {
                        //                            mPickerPromise.resolve(uri.toString());
                        //                        }
                    }

                    mPickerPromise = null
                }
            }
        }
    }


    override fun getName(): String {
        return "ActivityStarter"
    }

    @ReactMethod
    fun navigateToExample(promise: Promise) {
        val currentActivity = currentActivity
        val intent = Intent(currentActivity, StripeDemoActivity::class.java)
        if (currentActivity == null) {
            promise.reject(E_ACTIVITY_DOES_NOT_EXIST, "Activity doesn't exist")
            return
        }
        mPickerPromise = promise
        currentActivity.startActivityForResult(intent, Constants.STRIPE_ACTIVITY_INVOKE_CODE)
    }

    @ReactMethod
    fun navigateToStripeMain(paymentConfig:String,promise: Promise) {
        val currentActivity = currentActivity
        var gson = Gson()
        var paymentRequest = gson.fromJson(paymentConfig,PaymentRequest::class.java)
        val intent = Intent(currentActivity, StripeMainActivity::class.java)
        if (currentActivity == null) {
            promise.reject(E_ACTIVITY_DOES_NOT_EXIST, "Activity doesn't exist")
            return
        }
        mPickerPromise = promise
        var bundle = Bundle()
        bundle.putParcelable(Constants.paymentRequest,paymentRequest)
        intent.putExtra(Constants.bundle,bundle)
        currentActivity.startActivityForResult(intent, Constants.STRIPE_MAIN_ACTIVITY_INVOKE_CODE)
    }

    init {
        reactContext.addActivityEventListener(mActivityEventListener)
    }

    companion object {

        private val E_ACTIVITY_DOES_NOT_EXIST = "ACTIVITY DOESNT EXIST"
        private val PAYMENT_CANCELLED = "PAYMENT_CANCELLED"
        private val TAG = ActivityStarterModule::class.java.simpleName
    }

}