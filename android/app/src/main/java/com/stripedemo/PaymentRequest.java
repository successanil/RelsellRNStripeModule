/*
 * Copyright (c) 2019. Relsell Global
 */

/*
 * Copyright (c) 2018. Relsell Global
 */

package com.stripedemo;

import com.stripe.android.model.Token;

public class PaymentRequest {
    public Token stripeToken;
    public float amount;
    public float shippingCost;
    public boolean debug;
    public boolean isLiveModeStripeUserId;
    public String loggedInUserId;
}
