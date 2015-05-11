package com.lymno.quest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Colored on 02.04.2015.
 */
public class SignIn extends ActionBarActivity implements View.OnClickListener{
    public Button signin_button;
    public EditText loginEdit;
    public EditText passwordEdit;
    public TextView register;
    public TextView forgot_pass;

    SharedPreferences cache;
    private String responseString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cache = getPreferences(MODE_PRIVATE);
        String storedToken = cache.getString("IDToken", "");
        if (storedToken != "") {
            Intent intent = new Intent(this, QuestList.class);
            this.startActivity (intent);
            this.finishActivity (0);
        }

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

    Handler toastHandler = new Handler();
    Runnable toastRunnable = new Runnable() {public void run() {Toast.makeText(SignIn.this,"Неверный пароль или ошибка", Toast.LENGTH_LONG).show();}};

    private class SignInRequest extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String urlString = params[0];
            String res = "";
            InputStream responseStream = null;

            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                responseStream = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader responseReader = new BufferedReader(new InputStreamReader(responseStream));
                responseString = responseReader.readLine();
                responseStream.close();

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                return ex.getMessage();
            }

            try {
                JSONObject JSONSignInResponse = new JSONObject(responseString);
                String responseResult = JSONSignInResponse.getString("Result");
                String responseMethod = JSONSignInResponse.getString("Function");

                if ("entrance Success".equals(responseMethod + " " + responseResult)) {

                    cache = getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor ed = cache.edit();
                    ed.putString("IDToken", "sometokenvaluehere");
                    ed.apply();

                    Intent intent = new Intent(getBaseContext(), QuestList.class);
                    startActivity(intent);
                }
                else {
                    toastHandler.post(toastRunnable);
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }

            return res;
        }

        protected void onPostExecute(String result) {

        }
    }

    public class SignInRequest1 extends AsyncTask<String, Void, String> {
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

                    cache = getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor ed = cache.edit();
                    ed.putString("ID", "sometokenvaluehere");
                    ed.commit();

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
