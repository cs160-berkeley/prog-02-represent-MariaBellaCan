package com.example.mariacan.campaignupdates2016;

import android.os.AsyncTask;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mariacan on 3/10/16.
 */
public class URLRequestHandler extends AsyncTask<String, Void, String>{

    Exception urlException = null;

    @Override
    protected String doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        URL urlReq = null;
        //JSONObject jsonObject = null;
        String response = "";
        InputStream inputStream = null;

        try{
            urlReq = new URL(params[0]);
            urlConnection = (HttpURLConnection) urlReq.openConnection();
            urlConnection.connect();

            inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null){
                response += line + "\n";
            }

            //return response;
            //jsonObject = (JSONObject) new JSONTokener(response).nextValue();
        } catch (Exception e){
            e.printStackTrace();
            this.urlException = e;

        } finally {
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch(IOException ioException){

                }
            }
            if (urlConnection != null){
                urlConnection.disconnect();
            }

        }
        //System.out.println(jsonObject);
        //return jsonObject;
        return response;
    }
}
