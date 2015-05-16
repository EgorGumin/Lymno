package com.lymno.quest;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Gumin Egor on 01.04.2015.
 */

public class Request {

    public static String serverIP = "http://lymnologic.azurewebsites.net/"; //217.197.4.107
    public static String result;
    public static String resultInput = "fstinit";

    public static String GET(String url){
        InputStream inputStream = null;
        //String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET Request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    // convert inputstream to String
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        resultInput = ""; //тут было перекрытие стринг ризалт
        while((line = bufferedReader.readLine()) != null)
            resultInput += line;

        inputStream.close();
        return resultInput;

    }

    public static class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        //@Override
        //protected void onPostExecute(String res) {
        //Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
        //result.setText(res);
        //}
    }



}
