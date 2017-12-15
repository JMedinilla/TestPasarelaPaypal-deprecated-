package com.ncatz.jmed.pasarelatest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.NO_NETWORK:
                bundle.putBoolean("network", true);
                bundle.putBoolean("sandbox", false);
                break;
            case R.id.SANDBOX:
                bundle.putBoolean("network", false);
                bundle.putBoolean("sandbox", true);
                break;
            case R.id.PRODUCTION:
                bundle.putBoolean("network", false);
                bundle.putBoolean("sandbox", false);
                break;
        }
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
