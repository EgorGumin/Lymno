package com.lymno.quest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Colored on 02.04.2015.
 */
public class SignIn extends ActionBarActivity implements View.OnClickListener{
    public Button signin_button;
    public EditText loginEdit;
    public EditText passwordEdit;
    public TextView forgot_pass;

    private final String magicRequest = "api/users/entrance?";
    SharedPreferences cache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cache = getPreferences(MODE_PRIVATE);
        String storedToken = cache.getString("IDToken", "");
        if (!storedToken.equals("")) {
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
                try {
                    email = URLEncoder.encode(email, "UTF-8");
                    password = URLEncoder.encode(password, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String query = magicRequest + "login=" + email + "&password=" + password;
                new SignInRequest().execute(query);
                break;


            case R.id.forgot_pass:
                Intent forgot_pass_intent = new Intent(this, ForgotPassword.class);
                startActivity(forgot_pass_intent);
                break;

            default:
                break;
        }
    }

    private class SignInRequest extends BasicRequest {
        protected void onPostExecute(String result) {
            try {
                JSONObject JSONSignInResponse = new JSONObject(result);
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
                    Toast.makeText(SignIn.this,"Неверный пароль или ошибка", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }
}
