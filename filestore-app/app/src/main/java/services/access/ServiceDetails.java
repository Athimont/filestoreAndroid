package services.access;

import android.os.AsyncTask;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.filestore.app.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Map;


/**
 * Cette classe permet d'accédé à la méthode qui permet d'affiché les détails
 * d'un fichier
 */
public class ServiceDetails extends AsyncTask<String,String,String> {

    private final String protocol = "http://";
    private final String host = "fs-6846e6a6-1.ada20f9b.cont.dockerapp.io";
    private final String port = "32776";
    private final String requestPath = "/api/files/";

    MainActivity activity;

    public ServiceDetails(MainActivity a) {
        this.activity = a;
    }

    @Override
    protected String doInBackground(String... fileIds) {
        try {
            String key = fileIds[0];
            HttpClient client = new DefaultHttpClient();
            String url = this.protocol + this.host + ":" + this.port + this.requestPath + key;
            HttpGet getRequest = new HttpGet(url);
            HttpResponse reponse = client.execute(getRequest);
            InputStream is =reponse.getEntity().getContent();
            StringWriter writer = new StringWriter();
            IOUtils.copy(is, writer, "UTF-8");
            String theString = writer.toString();
            return theString;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void onPostExecute(String s) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map;
            map = mapper.readValue(s, new TypeReference<Map<String, String>>(){});

            activity.owner.setText("owner : " + map.get("owner"));
            activity.type.setText("type : " + map.get("type"));
            activity.name.setText("name : " + map.get("name"));
            activity.id.setText("id : " + map.get("id"));
            activity.stream.setText("stream : " + map.get("stream"));
            activity.lastDownload.setText("lastDownload : " + map.get("lastdownload"));
            activity.creation.setText("creation : " + map.get("creation"));
            activity.messageDetails.setText("message : " + map.get("message"));
            activity.downloads.setText("downloads : " + map.get("nbdownloads"));
            activity.length.setText("length : " + map.get("length"));
        } catch (IOException e) {
            Toast.makeText(this.activity, "Failed to get file details", Toast.LENGTH_LONG).show();
        }




    }
}
