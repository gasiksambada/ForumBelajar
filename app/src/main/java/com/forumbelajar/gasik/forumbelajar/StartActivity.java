package com.forumbelajar.gasik.forumbelajar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Gasik on 6/17/2016.
 */
public class StartActivity extends Activity implements View.OnClickListener {

    Button bLogin,bRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_start);

        bLogin = (Button) findViewById(R.id.login);
        bRegister = (Button) findViewById(R.id.register);
        bLogin.setOnClickListener(this);
        bRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.login:
                Intent intent_login = new Intent(this, LoginActivity.class);
                startActivity(intent_login);
                break;

            case R.id.register:
                Intent intent_register = new Intent(this, RegisterActivity.class);
                startActivity(intent_register);
                break;
        }
    }
}
