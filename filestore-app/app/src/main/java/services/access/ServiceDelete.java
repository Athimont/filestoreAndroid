package services.access;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

/**
 * Cette classe permet l'appel de le méthode delete du web service.
 */
public class ServiceDelete extends AsyncTask<String,String,Boolean> {

    private final String protocol = "http://";
    private final String host = "fs-6846e6a6-1.ada20f9b.cont.dockerapp.io";
    private final String port = "32776";
    private final String requestPath = "/api/files/";

    Activity activity;

    public ServiceDelete(Activity a){
        this.activity = a;
    }

    @Override
    protected Boolean doInBackground(String... fileIds) {
        try {
            String key = fileIds[0];
            HttpClient client = new DefaultHttpClient();
            String url = this.protocol + this.host + ":" + this.port + this.requestPath + key;
            HttpDelete deleteRequest = new HttpDelete(url);
            HttpResponse reponse = client.execute(deleteRequest);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    protected void onPostExecute(Boolean b) {
        if(b){
            Toast.makeText(this.activity, "Your file has been Deleted", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this.activity, "Failed to delete your file", Toast.LENGTH_LONG).show();
        }

    }
}
