package com.lymno.quest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

public class SignInOrRegister extends ActionBarActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_or_register);
        Button goToSign = (Button) findViewById(R.id.goToSign);
        goToSign.setOnClickListener(this);
        Button goToReg = (Button) findViewById(R.id.stageQuestion);
        goToReg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goToSign:
                Intent intent = new Intent(this, SignIn.class);
                startActivity(intent);
                break;

            case R.id.stageQuestion:
                Intent regIntent = new Intent(this, Registration.class);
                startActivity(regIntent);
                break;

            default:
                break;
        }
    }
}
