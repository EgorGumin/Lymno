package com.lymno.quest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Registration extends ActionBarActivity implements View.OnClickListener{
    EditText emailEdit;
    EditText passwordEdit;
    EditText confirmPass;
    EditText nicknameEdit;
    Button registerBtn;
    String email;
    String password;
    String nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        emailEdit = (EditText) findViewById(R.id.reg_email);
        passwordEdit = (EditText) findViewById(R.id.reg_password);
        confirmPass = (EditText) findViewById(R.id.confirm_password);
        nicknameEdit = (EditText) findViewById(R.id.name);

        registerBtn = (Button) findViewById(R.id.reg_btn);
        registerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //Кнопка регистрации. Нужно запретить использование некоторых знаков в логине и пароле.
            //Например, точку с запятой.
            case R.id.reg_btn:
                email = emailEdit.getText().toString();
                password = passwordEdit.getText().toString();
                nickname = nicknameEdit.getText().toString();
                if (email.equals("") || password.equals("") || nickname.equals("")) {
                    Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_LONG).show();
                    break;
                }
                if (!password.equals(confirmPass.getText().toString())){
                    Toast.makeText(this, "Пароли не совпадают.", Toast.LENGTH_LONG).show();
                    break;
                }

                String query = "login=" + email + "&password=" + password + "&nickname=" + nickname + "&gender=1";
                String allQuery = Request.serverIP + "api/users/registration?" + query;
                new Register().execute(allQuery);
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
                if ("registration Fail".equals(method +" "+ result)) {
                    Toast.makeText(getBaseContext(), "Такой логин уже занят или произошла ошибка.", Toast.LENGTH_LONG).show();
                }
                else {
                    SharedPreferences cache = getSharedPreferences("cache", MODE_PRIVATE);
                    SharedPreferences.Editor ed = cache.edit();
                    ed.putString("IDToken", result);
                    ed.apply();

                    Intent intent = new Intent(getBaseContext(), QuestList.class);
                    startActivity(intent);
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }

}