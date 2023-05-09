package com.mobile.reverleaf;

import android.app.Application;

import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.NotNull;
import com.paypal.checkout.PayPalCheckout;
import com.paypal.checkout.approve.Approval;
import com.paypal.checkout.approve.OnApprove;
import com.paypal.checkout.config.CheckoutConfig;
import com.paypal.checkout.config.Environment;
import com.paypal.checkout.config.SettingsConfig;
import com.paypal.checkout.createorder.CreateOrder;
import com.paypal.checkout.createorder.CreateOrderActions;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.OrderIntent;
import com.paypal.checkout.createorder.UserAction;
import com.paypal.checkout.order.Amount;
import com.paypal.checkout.order.AppContext;
import com.paypal.checkout.order.BreakDown;
import com.paypal.checkout.order.CaptureOrderResult;
import com.paypal.checkout.order.OnCaptureComplete;
import com.paypal.checkout.order.OrderRequest;
import com.paypal.checkout.order.PurchaseUnit;

import java.util.ArrayList;
import java.util.function.Consumer;

public class PaypalManager extends Application {

    static boolean mIsInitialized = false;

    public static void InitializePaypal(Application _application)
    {
        if(mIsInitialized) return;

        mIsInitialized = true;
        PayPalCheckout.setConfig(new CheckoutConfig(
                _application,
                "AeXbQkfflCCDZDueQxLNh7YINdIfEA9I0I7GwxDKgPUjgj4nt4DR-MlelZGp2gV3DdWwXsUeBwVgoCaV",
                Environment.SANDBOX,
                CurrencyCode.EUR,
                UserAction.PAY_NOW,
                null,
                new SettingsConfig(true, false),
                "com.mobile.reverleaf://paypalpay"
        ));
    }

    public static CreateOrder CreatePaymentOrder(float _price)
    {
        CreateOrder _order = new CreateOrder()
        {
            @Override
            public void create(@NotNull CreateOrderActions createOrderActions)
            {
                ArrayList _purchases = new ArrayList();
                Amount _amount = new Amount(CurrencyCode.EUR,Float.toString(_price), null);
                PurchaseUnit _purchase = new PurchaseUnit(null, "Abonnement Reverleaf",_amount);
                _purchases.add(_purchase);

                AppContext _context = new AppContext.Builder().userAction(UserAction.PAY_NOW).build();
                OrderRequest _order = new OrderRequest(OrderIntent.CAPTURE, _context, _purchases);
                createOrderActions.create(_order, (CreateOrderActions.OnOrderCreated) null);
            }
        };

        return _order;
    }

    public static OnApprove CreateApprovalFeedback(Consumer<CaptureOrderResult> _onPaypalPaid)
    {
        OnCaptureComplete _captureComplete = new OnCaptureComplete() {
            @Override
            public void onCaptureComplete(@NonNull CaptureOrderResult captureOrderResult) {
                _onPaypalPaid.accept(captureOrderResult);
            }
        };

        OnApprove _approval = new OnApprove() {
            @Override
            public void onApprove(@NonNull Approval approval) {
                approval.getOrderActions().capture(_captureComplete);
            }
        };

        return _approval;
    }
}
