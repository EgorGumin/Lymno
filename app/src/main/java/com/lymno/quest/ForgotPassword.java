package com.lymno.quest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPassword extends ActionBarActivity implements View.OnClickListener {
    private EditText etForgotPassEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        etForgotPassEmail = (EditText) findViewById(R.id.etForgotPassEmail);
        Button receiveCode =  (Button) findViewById(R.id.btnReceiveCode);
        receiveCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnReceiveCode:
                String email = etForgotPassEmail.getText().toString();
                new ForgotPassReq().execute(Request.serverIP + "api/users/recover?" +  "login=" + email);
                break;
        }
    }

    public class ForgotPassReq extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return Request.GET(urls[0]);
        }
        protected void onPostExecute(String res) {
            try {
                JSONObject dataJsonObj = new JSONObject(res);
                if (dataJsonObj.getString("Result").equals("Success")) {
                    Toast.makeText(getBaseContext(), "Письмо отправлено.", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getBaseContext(), "Пользователя с таким email не существует.", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }
}
