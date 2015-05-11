package com.lymno.quest;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Colored on 02.04.2015.
 */
public class SignIn extends ActionBarActivity implements View.OnClickListener{
    public Button signin_button;
    public EditText loginEdit;
    public EditText passwordEdit;
    public TextView register;
    public TextView forgot_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        signin_button = (Button) findViewById(R.id.signin_button);
        signin_button.setOnClickListener(this);
        loginEdit = (EditText) findViewById(R.id.login);
        passwordEdit = (EditText) findViewById(R.id.password);
        forgot_pass = (TextView) findViewById(R.id.forgot_pass);
        forgot_pass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //Кнопка входа в аккаунт. Нужно запретить использование некоторых знаков в логине и пароле.
            //Например, точку с запятой.
            case R.id.signin_button:
                String email = loginEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                String query = "login=" + email + "&password=" + password;
                String allQuery = Request.serverIP + "api/users/entrance?" +  query;
                //Toast.makeText(this, allQuery, Toast.LENGTH_LONG).show();
                new SignInRequest().execute(allQuery);
                break;


            case R.id.forgot_pass:
                Intent forgot_pass_intent = new Intent(this, ForgotPassword.class);
                startActivity(forgot_pass_intent);
                break;

            default:
                break;
        }
    }

    public class SignInRequest extends AsyncTask<String, Void, String> {
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

                //emailEdit.setText(method + result);
                if ("entrance Success".equals(method +" "+ result)) {
                    Intent intent = new Intent(getBaseContext(), QuestList.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getBaseContext(), "Неверный пароль или ошибка.", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }
}
