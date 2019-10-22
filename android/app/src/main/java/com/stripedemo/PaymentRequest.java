/*
 * Copyright (c) 2019. Relsell Global
 */

/*
 * Copyright (c) 2018. Relsell Global
 */

package com.stripedemo;

import android.os.Parcel;
import android.os.Parcelable;

import com.stripe.android.model.Token;

public class PaymentRequest implements Parcelable {
    public Token stripeToken;
    public float amount;
    public boolean debug;
    public boolean isLiveModeStripeUserId;
    public String paymentUrl;

    protected PaymentRequest(Parcel in) {
        amount = in.readFloat();
        debug = in.readByte() != 0;
        isLiveModeStripeUserId = in.readByte() != 0;
        paymentUrl = in.readString();
    }

    public static final Creator<PaymentRequest> CREATOR = new Creator<PaymentRequest>() {
        @Override
        public PaymentRequest createFromParcel(Parcel in) {
            return new PaymentRequest(in);
        }

        @Override
        public PaymentRequest[] newArray(int size) {
            return new PaymentRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(amount);
        dest.writeByte((byte) (debug ? 1 : 0));
        dest.writeByte((byte) (isLiveModeStripeUserId ? 1 : 0));
        dest.writeString(paymentUrl);
    }
}
