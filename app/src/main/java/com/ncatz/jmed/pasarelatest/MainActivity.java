package com.ncatz.jmed.pasarelatest;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Request para onActivityResult
    private static final int REQUEST_PAYPAL = 333;

    //Configuración del vendedor
    private PayPalConfiguration configuration;

    //Productos
    private PayPalPayment PAYPAL_KEBAB;
    private PayPalPayment PAYPAL_POLLO;
    private PayPalPayment PAYPAL_PAELLA;
    private PayPalPayment PAYPAL_HAMBURGUESA;
    private PayPalPayment PAYPAL_RAVIOLI;
    private PayPalPayment PAYPAL_PIZZA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                boolean network = bundle.getBoolean("network");
                boolean sandbox = bundle.getBoolean("sandbox");

                initConfig(network, sandbox);
                initProducts();
                initService();
            } else {
                finish();
            }
        } else {
            finish();
        }
    }

    private void initConfig(boolean network, boolean sandbox) {
        if (network) {
            configuration = new PayPalConfiguration()
                    .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
                    .clientId("")
                    .merchantName("")
                    .merchantPrivacyPolicyUri(Uri.parse(""))
                    .merchantUserAgreementUri(Uri.parse(""))
                    .rememberUser(false)
                    .acceptCreditCards(true);
        } else {
            if (sandbox) {
                configuration = new PayPalConfiguration()
                        .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                        .clientId("ARX8i_XD6PMKR2zegWGSzYzvy_E9XWTlGq5ypiANCnWhUtQcnMH8eJ17p8IGFgip0hW3SiNVp0j1fO2y")
                        .merchantName("Javier Medinilla")
                        .merchantPrivacyPolicyUri(Uri.parse("https://www.google.com/"))
                        .merchantUserAgreementUri(Uri.parse("https://www.google.com/"))
                        .rememberUser(false)
                        .acceptCreditCards(true)
                        .defaultUserEmail("javimedinilla-buyer@gmail.com");
            } else {
                configuration = new PayPalConfiguration()
                        .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
                        .clientId("ARhuQpacWKb-eXTvFwzYYa6Y0Fbg8YXHi_tpLcIr00twPpwgEnP4aM-BCsSgsEegqWRXtABTdA-gp9u4")
                        .merchantName("Javier Medinilla")
                        .merchantPrivacyPolicyUri(Uri.parse("https://www.google.com/"))
                        .merchantUserAgreementUri(Uri.parse("https://www.google.com/"))
                        .rememberUser(false)
                        .acceptCreditCards(true);
            }
        }
    }

    private void initProducts() {
        PAYPAL_KEBAB = new PayPalPayment(
                new BigDecimal("5.00"),
                "EUR",
                "Kebab XL completo",
                PayPalPayment.PAYMENT_INTENT_SALE
        );
        PAYPAL_POLLO = new PayPalPayment(
                new BigDecimal("7.50"),
                "EUR",
                "Pollo frito con patatas",
                PayPalPayment.PAYMENT_INTENT_SALE
        );
        PAYPAL_PAELLA = new PayPalPayment(
                new BigDecimal("12.40"),
                "EUR",
                "Paella",
                PayPalPayment.PAYMENT_INTENT_SALE
        );
        PAYPAL_HAMBURGUESA = new PayPalPayment(
                new BigDecimal("6.40"),
                "EUR",
                "Hamburguesa completa",
                PayPalPayment.PAYMENT_INTENT_SALE
        );
        PAYPAL_RAVIOLI = new PayPalPayment(
                new BigDecimal("8.20"),
                "EUR",
                "Raviolis con queso",
                PayPalPayment.PAYMENT_INTENT_SALE
        );
        PAYPAL_PIZZA = new PayPalPayment(
                new BigDecimal("6.00"),
                "EUR",
                "Pizza vegetal",
                PayPalPayment.PAYMENT_INTENT_SALE
        );
    }

    private void initService() {
        Intent service = new Intent(this, PayPalService.class);
        service.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        startService(service);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        switch (v.getId()) {
            case R.id.pay1:
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, PAYPAL_KEBAB);
                break;
            case R.id.pay2:
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, PAYPAL_POLLO);
                break;
            case R.id.pay3:
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, PAYPAL_PAELLA);
                break;
            case R.id.pay4:
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, PAYPAL_HAMBURGUESA);
                break;
            case R.id.pay5:
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, PAYPAL_RAVIOLI);
                break;
            case R.id.pay6:
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, PAYPAL_PIZZA);
                break;
        }
        startActivityForResult(intent, REQUEST_PAYPAL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PAYPAL) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        String resultado;
                        resultado = confirmation.toJSONObject().toString();
                        resultado += "\n\n\n";
                        resultado += confirmation.getPayment().toJSONObject().toString();
                        Log.v("PayTag", resultado);

                        /*
                            Aquí habría que enviar este resultado al servidor, y comprobar allí,
                            usando la cuenta de Paypal y el ID de la transacción, que verdaderamente
                            existe esa transacción y devolver si el resultado ha sido favorable
                            desde allí, en lugar de comprobarlo desde este objeto devuelto directamente
                        */
                    } catch (Exception e) {
                        Log.v("PayTag", "Fallo inesperado y totalmente raro en cuanto al formato del JSON de Paypal");
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.v("PayTag", "Datos entregador erróneos");
            }
        }
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
}
