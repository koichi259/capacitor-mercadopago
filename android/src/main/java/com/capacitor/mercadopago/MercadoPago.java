package com.capacitor.mercadopago;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.mercadopago.android.px.core.MercadoPagoCheckout;
import com.mercadopago.android.px.model.Payment;
import com.mercadopago.android.px.model.exceptions.MercadoPagoError;


import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_CANCELED;


@NativePlugin(
        requestCodes = {MercadoPago.REQUEST_CODE}
)
public class MercadoPago extends Plugin {

  private MercadoPagoCheckout checkout2 = null;
  public static final int REQUEST_CODE = 1;

  @PluginMethod()
  public void checkout(PluginCall call) {

    saveCall(call);
    String publicKey = call.getString("publicKey");
    String preferenceId = call.getString("preferenceId");

    checkout2 = new MercadoPagoCheckout.Builder(publicKey, preferenceId).build();
    checkout2.startPayment(getContext(), REQUEST_CODE);
  }

  @RequiresApi(api = Build.VERSION_CODES.N)
  @Override
  protected void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
    super.handleOnActivityResult(requestCode, resultCode, data);
    PluginCall savedCall = getSavedCall();

    if (savedCall == null) {
      return;
    }
    if (requestCode == REQUEST_CODE) {
      if (resultCode == MercadoPagoCheckout.PAYMENT_RESULT_CODE) {
        final Payment payment = (Payment) data.getSerializableExtra(MercadoPagoCheckout.EXTRA_PAYMENT_RESULT);

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.serializeNulls().create();

        JSObject ret = new JSObject();
        
        String json = gson.toJson(payment);
        String actualKey = "";
        JsonReader jsonReader = new JsonReader(new StringReader(json));
        try {
          while (jsonReader.hasNext()) {
            JsonToken nextToken = jsonReader.peek();
            if (nextToken == JsonToken.BEGIN_OBJECT) {
              jsonReader.beginObject();
            } else if (nextToken == JsonToken.END_OBJECT) {
              jsonReader.endObject();
            } else if (JsonToken.NAME.equals(nextToken)) {
              actualKey = jsonReader.nextName();
            } else if (JsonToken.STRING.equals(nextToken)) {
              ret.put(actualKey, jsonReader.nextString());
            } else if (JsonToken.NUMBER.equals(nextToken)) {
              ret.put(actualKey, jsonReader.nextLong());
            } else if (JsonToken.BOOLEAN.equals(nextToken)) {
              ret.put(actualKey, jsonReader.nextBoolean());
            } else if (JsonToken.NULL.equals(nextToken)) {
              ret.put(actualKey, JSONObject.NULL);
              jsonReader.nextNull();
            } else {
              jsonReader.skipValue();
            }
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
        savedCall.resolve(ret);
      } else if (resultCode == RESULT_CANCELED) {
        if (data != null && data.getExtras() != null
                && data.getExtras().containsKey(MercadoPagoCheckout.EXTRA_ERROR)) {
          final MercadoPagoError mercadoPagoError =
                  (MercadoPagoError) data.getSerializableExtra(MercadoPagoCheckout.EXTRA_ERROR);
          savedCall.reject(mercadoPagoError.getMessage());
        } else {
          savedCall.reject("user cancelled checkout");
        }
      }
    }
  }
}