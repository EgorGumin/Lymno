package com.lymno.quest;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lime on 03.04.2015.
 */
public class Registration extends ActionBarActivity implements View.OnClickListener{
    EditText email_edit;
    EditText password_edit;
    Button register_button;
    String email;
    String password;
    //RadioGroup gender = (RadioGroup) findViewById(R.id.gender);
    EditText nickname = (EditText) findViewById(R.id.name);
    EditText confirmPass = (EditText) findViewById(R.id.confirm_password);
    RadioButton male = (RadioButton) findViewById(R.id.male);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        email_edit = (EditText) findViewById(R.id.reg_email);
        password_edit = (EditText) findViewById(R.id.reg_password);
        register_button = (Button) findViewById(R.id.reg_btn);
        register_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //Кнопка регистрации. Нужно запретить использование некоторых знаков в логине и пароле.
            //Например, точку с запятой.
            case R.id.reg_btn:
                    if (male.isPressed()) {
                        Toast.makeText(getBaseContext(), "MALE", Toast.LENGTH_LONG).show();
                    }
                    email = email_edit.getText().toString();
                    password = password_edit.getText().toString();
                    String query = "login=" + email + "&password=" + password + "&nickname=" + nickname + "&gender=false";
                    String allQuery = Request.serverIP + "api/users/registration?" + query;
                    Toast.makeText(this, allQuery, Toast.LENGTH_LONG).show();
                    new Register().execute(allQuery);
                    break;
                    default:
                        break;
                }
        }

    public class Register extends AsyncTask<String, Void, String> {
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
                email_edit.setText(method + result);
                if ("registration Success".equals(method +" "+ result)) {
                    //Intent intent = new Intent(getBaseContext(), Notifications.class);
                    //startActivity(intent);
                    Toast.makeText(getBaseContext(), "NORMAS", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getBaseContext(), "Такой логин уже занят или произошла ошибка.", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }

}
