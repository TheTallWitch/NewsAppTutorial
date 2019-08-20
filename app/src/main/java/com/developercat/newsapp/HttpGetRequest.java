package com.developercat.newsapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpGetRequest extends AsyncTask<String, Void, String> {

    private ProgressDialog progressDialog;
    private WeakReference<Context> context;
    private ResultListener listener;

    public HttpGetRequest(Context cntx, ResultListener listener) {
        context = new WeakReference<Context>(cntx);
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context.get());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        //on background thread, must do main work
        String stringUrl = strings[0];

        try {
            URL url = new URL(stringUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setReadTimeout(30000);
            connection.setConnectTimeout(30000);
            connection.connect();

            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(streamReader);

            StringBuilder stringBuilder = new StringBuilder();
            String inputLine = "";

            while ((inputLine = reader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }

            reader.close();
            streamReader.close();

            String result = stringBuilder.toString();
            return result;

        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        //on main thread, hide progress dialog
        progressDialog.hide();
        listener.onResult(result);
    }

    public interface ResultListener {
        public void onResult(String result);
    }
}