package com.lymno.quest;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Roman Belkov on 13.05.2015.
 */

public class BasicRequest extends AsyncTask<String, String, String> {

    private final String serverIP = "http://simpleas123.azurewebsites.net/";

    String urlString;
    String resultingJSONString;
    InputStream responseStream;

    @Override
    protected String doInBackground(String... params) {
        urlString = params[0];

        try {
            URL url = new URL(serverIP + urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            responseStream = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader responseReader = new BufferedReader(new InputStreamReader(responseStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = responseReader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
            resultingJSONString = stringBuilder.toString();
            responseStream.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return ex.getMessage();
        }

        return resultingJSONString;
    }
}
