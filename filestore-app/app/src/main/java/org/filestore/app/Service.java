package org.filestore.app;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Service extends AsyncTask<Message,String,Boolean>{

    private Activity activity;

    final String protocol = "http://";
    final String host = "10.0.2.2";
    final String port = "8080";
    final String requestPath = "/api/files";
    final String url = protocol + host + ":" + port + requestPath;

    Service(Activity a){
        this.activity = a;
    }

	@Override
	protected Boolean doInBackground(Message... messages) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost (url);

            MultipartEntityBuilder entity = MultipartEntityBuilder.create();
            entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            Message m = messages[0];

            StringBody owner = new StringBody(m.getSender(), ContentType.TEXT_PLAIN);
            entity.addPart("owner",owner);

            for(String s : m.getReceivers()){
                StringBody receiver = new StringBody(s, ContentType.TEXT_PLAIN);
                entity.addPart("receivers",receiver);
            }

            InputStreamBody file = new InputStreamBody(m.getFile(), ContentType.MULTIPART_FORM_DATA);
            entity.addPart("file",file);

            StringBody file_name = new StringBody(m.getFile_name(), ContentType.TEXT_PLAIN);
            entity.addPart("file_name",file_name);

            StringBody message = new StringBody(m.getMessage(), ContentType.TEXT_PLAIN);
            entity.addPart("message",message);

            postRequest.setEntity(entity.build());
            HttpResponse reponse = client.execute(postRequest);
            return true;
        } catch (IOException e) {
            return false;
        }
	}

	@Override
	protected void onPostExecute(Boolean b) {
        if(b){
            Toast.makeText(this.activity, "Your file has been uploaded", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this.activity, "Failed to upload your file", Toast.LENGTH_LONG).show();
        }

	}
}
