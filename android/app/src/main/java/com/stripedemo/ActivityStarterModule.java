/*
 * Copyright (c) 2019. Relsell Global
 */

package com.stripedemo;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

class ActivityStarterModule extends ReactContextBaseJavaModule {

    private static final String E_ACTIVITY_DOES_NOT_EXIST = "ACTIVITY DOESNT EXIST";
    private static int STRIPE_ACTIVITY_INVOKE_CODE = 9001;
    private static String PAYMENT_CANCELLED = "PAYMENT_CANCELLED";
    private Promise mPickerPromise;
    private static String TAG = ActivityStarterModule.class.getSimpleName();



    @Override
    public String getName() {
        return "ActivityStarter";
    }

    @ReactMethod
    void navigateToExample(Promise promise) {
        Activity currentActivity = getCurrentActivity();
        Intent intent = new Intent(currentActivity, StripeDemoActivity.class);
        if (currentActivity == null) {
            promise.reject(E_ACTIVITY_DOES_NOT_EXIST, "Activity doesn't exist");
            return;
        }
        mPickerPromise = promise;
        currentActivity.startActivityForResult(intent,STRIPE_ACTIVITY_INVOKE_CODE);
    }


    private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {

        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
            if (requestCode == STRIPE_ACTIVITY_INVOKE_CODE) {
                if (mPickerPromise != null) {
                    if (resultCode == Activity.RESULT_CANCELED) {
                        Log.v(TAG,"Payment activity Cancelled");
                        mPickerPromise.reject(PAYMENT_CANCELLED, "Payment was cancelled");
                    } else if (resultCode == Activity.RESULT_OK) {
//                        Uri uri = intent.getData();
//
//                        if (uri == null) {
//                            mPickerPromise.reject(E_NO_IMAGE_DATA_FOUND, "No image data found");
//                        } else {
//                            mPickerPromise.resolve(uri.toString());
//                        }
                    }

                    mPickerPromise = null;
                }
            }
        }
    };

    ActivityStarterModule(ReactApplicationContext reactContext) {
        super(reactContext);
        reactContext.addActivityEventListener(mActivityEventListener);
    }

}