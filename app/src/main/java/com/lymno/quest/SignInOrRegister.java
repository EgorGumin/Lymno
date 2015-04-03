package com.lymno.quest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Colored on 01.04.2015.
 */
public class SignInOrRegister extends ActionBarActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_or_register);
        Button goToSign = (Button) findViewById(R.id.goToSign);
        goToSign.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goToSign:
                Intent intent = new Intent(this, SignIn.class);
                startActivity(intent);
                break;
            /*
            case R.id.http_btn:
                http_btn.setClickable(false);
                String allQuery = Request.serverIP + "api/users/registration?login=Material4&password=kool&nickname=MAt&gender=false";
                myText.setText(allQuery);
                Toast.makeText(this, allQuery, Toast.LENGTH_LONG).show();
                new Register().execute(allQuery);
                break;
            */
            default:
                break;
        }
    }
}
