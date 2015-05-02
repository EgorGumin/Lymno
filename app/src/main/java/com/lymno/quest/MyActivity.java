package com.lymno.quest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class MyActivity extends ActionBarActivity implements View.OnClickListener {
    private EditText myText;
    private Button send;
    Button http_btn;
    Button gotosignpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        myText = (EditText) findViewById(R.id.editText);
        myText.setText("home/api/gui/quest/all");

        send = (Button) findViewById(R.id.button);
        send.setText("Send");

        http_btn = (Button) findViewById(R.id.http_btn);
        http_btn.setOnClickListener(this);

        gotosignpage = (Button) findViewById(R.id.gotosignpage);
        gotosignpage.setOnClickListener(this);


        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String sndtest = "";
                Request.HttpAsyncTask sendtest = new Request.HttpAsyncTask();
                sendtest.execute(Request.serverIP + myText.getText().toString());
                try {
                    sndtest = sendtest.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                myText.setText(""); //Очищаем наше поле ввода
                Toast.makeText(getBaseContext(), sndtest, Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.gotosignpage:
                Intent intent = new Intent(this, SignInOrRegister.class);
                startActivity(intent);
                break;

            case R.id.http_btn:
                http_btn.setClickable(false);
                Intent goToCards = new Intent(this, QuestList.class);
                startActivity(goToCards);
                http_btn.setClickable(true);
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
