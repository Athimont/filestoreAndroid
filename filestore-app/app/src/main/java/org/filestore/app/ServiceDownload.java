package org.filestore.app;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

/**
 * Created by athim on 29/11/2016.
 */

public class ServiceDownload extends AsyncTask<String,String,Boolean> {

    final String protocol = "http://";
    final String host = "10.0.2.2";
    final String port = "8080";
    final String requestPath1 = "/api/files/";
    final String requestPath2 = "/download";

    Activity activity;

    public ServiceDownload(Activity a){
        this.activity = a;
    }

    @Override
    protected Boolean doInBackground(String... fileIds) {
        try {
            String key = fileIds[0];
            HttpClient client = new DefaultHttpClient();
            String url = this.protocol + this.host + ":" + this.port + this.requestPath1 + key +this.requestPath2;
            HttpGet getRequest = new HttpGet(url);
            HttpResponse reponse = client.execute(getRequest);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    protected void onPostExecute(Boolean b) {
        if(b){
            Toast.makeText(this.activity, "Your file has been Downloaded", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this.activity, "Failed to download your file", Toast.LENGTH_LONG).show();
        }

    }
}
