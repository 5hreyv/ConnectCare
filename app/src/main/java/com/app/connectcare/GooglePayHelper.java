package com.app.connectcare;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

public class GooglePayHelper {

    // Method to get the JSON request for IsReadyToPay
    public static Optional<JSONObject> getIsReadyToPayRequest() {
        try {
            JSONObject json = new JSONObject();
            json.put("apiVersion", 2);
            json.put("apiVersionMinor", 0);

            // Define the allowed payment methods
            JSONObject allowedPaymentMethods = new JSONObject();
            allowedPaymentMethods.put("type", "CARD");

            // Add tokenization information (Optional but important for certain payments)
            JSONObject tokenizationSpecification = new JSONObject();
            tokenizationSpecification.put("type", "PAYMENT_GATEWAY");
            tokenizationSpecification.put("parameters", new JSONObject().put("gateway", "example").put("gatewayMerchantId", "exampleGatewayMerchantId"));
            allowedPaymentMethods.put("tokenizationSpecification", tokenizationSpecification);

            // Set the allowed payment methods to the main JSON object
            JSONArray allowedPaymentMethodsArray = new JSONArray();
            allowedPaymentMethodsArray.put(allowedPaymentMethods);
            json.put("allowedPaymentMethods", allowedPaymentMethodsArray);

            return Optional.of(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    // Method to create the PaymentDataRequest JSON
    public static Optional<JSONObject> getPaymentDataRequest(String amount) {
        try {
            JSONObject paymentDataRequestJson = new JSONObject();
            paymentDataRequestJson.put("apiVersion", 2);
            paymentDataRequestJson.put("apiVersionMinor", 0);

            JSONObject transactionInfo = new JSONObject();
            transactionInfo.put("totalPriceStatus", "FINAL");
            transactionInfo.put("totalPrice", amount);
            transactionInfo.put("currencyCode", "USD");

            paymentDataRequestJson.put("transactionInfo", transactionInfo);

            return Optional.of(paymentDataRequestJson);
        } catch (JSONException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
