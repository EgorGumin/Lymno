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
    public EditText email_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        email_edit = (EditText) findViewById(R.id.forgotPass_email);
        Button help = (Button) findViewById(R.id.receiveCode);
        help.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //Кнопка восстановления пароля. Нужно запретить использование некоторых знаков в логине и пароле.
            //Например, точку с запятой.
            case R.id.receiveCode:
                String email = email_edit.getText().toString();
                String query = "login=" + email;
                String allQuery = Request.serverIP + "api/users/recover?" +  query;
                new ForgotPassReq().execute(allQuery);
                break;

            default:
                break;
        }
    }

    public class ForgotPassReq extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return Request.GET(urls[0]);
        }
        protected void onPostExecute(String res) {
            String method = "";
            String result = "";
            try {
                JSONObject dataJsonObj = new JSONObject(res);
                result = dataJsonObj.getString("Result");
                method = dataJsonObj.getString("Function");
                if ("recover Success".equals(method +" "+ result)) {
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
